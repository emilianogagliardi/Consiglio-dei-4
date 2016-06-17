package client.view.GUI.controllerFX;

import client.ComunicazioneSceltaMappaRMI;
import client.ComunicazioneSceltaMappaSocket;
import client.view.CostantiClient;
import client.view.GUI.GUIView;
import client.view.GUI.GestoreFlussoFinestra;
import client.view.SocketPolling;
import client.view.SocketProxyController;
import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaController;
import interfaccecondivise.InterfacciaLoggerRMI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.Scanner;

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
                if (socketButton.isSelected()) {
                    apriConnessioneSocket();
                }else{
                    apriConnessioneRMI();
                }
                labelLogin.setText("Attendi altri giocatori...");
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
            Socket socket = new Socket(CostantiClient.IP_SERVER, CostantiClient.SOCKET_PORT);
            Scanner in = new Scanner(socket.getInputStream()); //get dell'idGicatore assegnato da server
            int id = Integer.parseInt(in.nextLine());
            assegnaIdGiocatore(id);
            assegnaController(new SocketProxyController(socket)); //necessario comunicazione client -> server
            //inizializza la gui view
            GUIView view = getNewGUIView();
            new Thread(new SocketPolling(view, socket)).start(); //necessario alla comunicazione server -> client
            view.setIdGiocatore(id);
            ComunicazioneSceltaMappaSocket.init(socket);
            super.getApplication().setIsSocketClient(true);
        }catch(IOException e) {
            e.printStackTrace();
            super.setNuovoStep("erroreconnessione.fxml");
        }
    }

    private void apriConnessioneRMI() {
        try {
            Registry registry = LocateRegistry.getRegistry(CostantiClient.IP_SERVER, CostantiClient.REGISTRY_PORT);
            InterfacciaLoggerRMI loggerRMI = (InterfacciaLoggerRMI) registry.lookup(CostantiClient.CHIAVE_LOGGER);
            //effettua login ottenendo l'id del giocatore
            GUIView view = getNewGUIView();
            int id = loggerRMI.login(view); //passa la view per rendere possibile la comunicazione server -> client
            view.setIdGiocatore(id);
            //TODO non è qui che deve essere lookuppato il controller, verrà bindato solo quando tutti i giocatori hanno loggato
            //String chiaveController = loggerRMI.getChiaveController();
            //assegnaController((InterfacciaController) registry.lookup(chiaveController)); //ottiene un riferimento al controller remoto, per comunicazione client -> server
            String chiaveSceltaMappa = loggerRMI.getChiaveSceltaMappa();
            ComunicazioneSceltaMappaRMI.init(chiaveSceltaMappa);
            System.out.println("chiavesceltamappa = " + chiaveSceltaMappa);
            super.getApplication().setIsSocketClient(false);
        }catch( NotBoundException | IOException e){
            e.printStackTrace();
            super.setNuovoStep("erroreconnessione.fxml");
        }
    }

    //assegna l'id ottenuto dal server al controller fx che si occupa delle update (controllerFXPartita
    private void assegnaIdGiocatore(int id) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.load(getClass().getClassLoader().getResource("viewgioco.fxml").openStream());
        ControllerFXPartita controllerFXPartita = loader.getController();
        controllerFXPartita.setIdGiocatore(id);
    }

    //TODO non è il momento giusto per gettare e assegare il controller, in questo punto è necessario solamente ottenere la stringa
    //TODO non so se è possibile assegnare il proxy controller in questo modo, potrebbe scazzare
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

    //inizializza e torna la GUIView
    private GUIView getNewGUIView() {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.load(getClass().getClassLoader().getResource("mosse.fxml").openStream());
            ControllerFXMosse controllerFXMosse = loader.getController();
            loader = new FXMLLoader();
            loader.load(getClass().getClassLoader().getResource("viewgioco.fxml"));
            ControllerFXPartita controllerFXPartita = loader.getController();
            GUIView.initGUIView(controllerFXMosse, controllerFXPartita, super.getApplication());
            try {
                GUIView view = GUIView.getInstance();
                return view;
            } catch (SingletonNonInizializzatoException e) {
                e.printStackTrace();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
