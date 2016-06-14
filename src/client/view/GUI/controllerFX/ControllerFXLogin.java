package client.view.GUI.controllerFX;

import interfaccecondivise.InterfacciaLoggerRMI;
import interfaccecondivise.InterfacciaController;
import client.view.SocketProxyController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import client.view.Costanti;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.Scanner;


//TODO è necessario avere un riferimento alla view

/*
classe che si occupa di gestire la finestra di login, assegnando ad ogni bottone l'eventhandler.
 */
public class ControllerFXLogin extends GestoreFlussoFinestra implements Initializable{
    @FXML
    private ToggleButton socketButton, RMIButton;
    @FXML
    private Button startButton;
    @FXML
    private Label labelLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelLogin.setVisible(false);
        setAzioneBottoneRMI();
        setAzioneBottoneSocket();
        inizializzaStartButton();
    }

    private void setAzioneBottoneSocket(){
        //viene alzato uando bottoneRMI viene premuto
        socketButton.setOnAction(e->{if(RMIButton.isSelected()) RMIButton.setSelected(false);});
    }

    private void setAzioneBottoneRMI(){
        //viene alzato quando BottoneSocket viene premuto
        RMIButton.setOnAction(e->{if(socketButton.isSelected()) socketButton.setSelected(false);});
    }

    /*
        l'azione start button deve richiedere al server l'apertura della
        comunicazione in base a quale dei due bottoni è stato premuto.
     */
    private void inizializzaStartButton() {
        startButton.setOnAction(e -> {
            //questo metodo da per scontato che i toggle button siano premuti in modo mutuamente esclusivo
            if (socketButton.isSelected() || RMIButton.isSelected()) {
                if (socketButton.isSelected())
                    apriConnessioneSocket();
                else
                    apriConnessioneRMI();
                labelLogin.setText("Attendi altri giocatori...");
                try {
                    super.setNuovoStep("mappegallery.fxml");
                }catch(IOException ex) {ex.printStackTrace();}
            }else {
                labelLogin.setText("Nessun metodo di comunicazione selezionato!");
            }
            labelLogin.setVisible(true);
        });
    }

    /*
        l'apertura della connessione in socket comporta la creazione di un socketProxyController,
        nel quale viene wrappato il socket stesso in modo da rendere trasparente al resto del programma
        la comunicazione. Questo proxy viene passato come un controller al controllerFXMosse, che
        permetterà così di comunicare in direzione client -> server
     */
    private void apriConnessioneSocket() {
        try {
            Socket socket = new Socket(Costanti.IP_SERVER, Costanti.SOCKET_PORT);
            Scanner in = new Scanner(socket.getInputStream()); //get dell'idGicatore assegnato da server
            int id = Integer.parseInt(in.nextLine());
            assegnaIdGiocatore(id);
            assegnaController(new SocketProxyController(socket)); //necessario comunicazione client -> server
            //new Thread(new SocketPolling(socket, view)).start(); //necessario alla comunicazione server -> client
        }catch(IOException e) {
            e.printStackTrace();
            try {
                super.setNuovoStep("erroreconnessione.fxml");
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void apriConnessioneRMI() {
        try {
            Registry registry = LocateRegistry.getRegistry(Costanti.IP_SERVER, Costanti.REGISTRY_PORT);
            InterfacciaLoggerRMI loggerRMI = (InterfacciaLoggerRMI) registry.lookup(Costanti.CHIAVE_LOGGER);
            //effettua login ottenendo l'id del giocatore
            //int id = loggerRMI.login(view); //passa la view per rendere possibile la comunicazione server -> client
            String chiaveController = loggerRMI.getChiaveController();
            assegnaController((InterfacciaController) registry.lookup(chiaveController)); //ottiene un riferimento al controller remoto, per comunicazione client -> server
        }catch(RemoteException e){
            e.printStackTrace();
            try {
                super.setNuovoStep("erroreconnessione.fxml");
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }catch (NotBoundException e){
            e.printStackTrace();
            try {
                super.setNuovoStep("erroreconnessione.fxml");
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    //assegna l'id ottenuto dal server al controller fx che si occupa delle update (controllerFXPartita
    private void assegnaIdGiocatore(int id) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.load(getClass().getClassLoader().getResource("viewgioco.fxml").openStream());
        ControllerFXPartita controllerFXPartita = loader.getController();
        controllerFXPartita.setIdGiocatore(id);
    }

    //assegnamento del controller a controllerFXMosse
    private void assegnaController(InterfacciaController controller) {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.load(getClass().getClassLoader().getResource("mosse.fxml").openStream());
            ControllerFXMosse controllerFXMosse = loader.getController();
            controllerFXMosse.setController(controller);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
