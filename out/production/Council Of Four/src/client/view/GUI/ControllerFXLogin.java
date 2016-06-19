package client.view.GUI;

import client.ComunicazioneSceltaMappaRMI;
import client.ComunicazioneSceltaMappaSocket;
import client.view.CostantiClient;
import client.view.SocketPollingView;
import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaLoggerRMI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

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
                startButton.setDisable(true);
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
            /*ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //get dell'idGicatore assegnato da server
            int id = ois.readInt();
            //inizializza la gui view*/
            GUIView.initGUIView(super.getApplication());
            new Thread(new SocketPollingView(GUIView.getInstance(), socket)).start(); //necessario alla comunicazione server -> client
            ComunicazioneSceltaMappaSocket.init(socket);
            super.getApplication().setIsSocketClient(true);
        }catch(IOException | SingletonNonInizializzatoException e) {
            e.printStackTrace();
            super.setNuovoStep("erroreconnessione.fxml");
        }
    }

    /*
        l'apertura della comunicaione in rmi comporta il lookup dell'oggetto di login bindato da server.
        Dal logger deve essere ricavato il nome con cui viene bindato l'oggetto remoto di comunicazione di scelta
        della mappa, e loggetto remoto controller del gioco.
    */
    private void apriConnessioneRMI() {
        try {
            Registry registry = LocateRegistry.getRegistry(CostantiClient.IP_SERVER, CostantiClient.REGISTRY_PORT);
            InterfacciaLoggerRMI loggerRMI = (InterfacciaLoggerRMI) registry.lookup(CostantiClient.CHIAVE_LOGGER);
            //inizializza la gui view con un valore fittizio di id giocatore
            GUIView.initGUIView(super.getApplication());
            /*
            //effettua login ottenendo l'id del giocatore
            int id = loggerRMI.login(GUIView.getInstance()); //passa la view per rendere possibile la comunicazione server -> client
            //assegna il giusto id ottenuto tramite il login
            GUIView.getInstance().setIdGiocatore(id);*/
            loggerRMI.login(GUIView.getInstance()); //passa la view per rendere possibile la comunicazione server -> client
            ComunicazioneSceltaMappaRMI.init(loggerRMI.getChiaveSceltaMappa());
            super.getApplication().setIsSocketClient(false);
        }catch( NotBoundException | IOException | SingletonNonInizializzatoException e){
            e.printStackTrace();
            super.setNuovoStep("erroreconnessione.fxml");
        }
    }
}
