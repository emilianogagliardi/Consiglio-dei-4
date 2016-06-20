package client.view.GUI;

import client.view.eccezioni.SingletonNonInizializzatoException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXApplication extends Application {
    private Stage finestraAttuale;
    private boolean isSocketClient;
    private Scene scenaGioco;

    @Override
    public void start(Stage primaryStage) throws IOException{
        GUIView.initGUIView(this);
        initScenaPartita();
        finestraAttuale = primaryStage;
        setFinestraDaFXML("login.fxml");
    }

    private void setFinestraDaFXML(String nomeFile) throws IOException{
        //assegna la scena
        FXMLLoader loader = new FXMLLoader();
        Parent root =  loader.load(getClass().getResource("/"+nomeFile).openStream());
        GestoreFlussoFinestra controllerFX = loader.getController();
        controllerFX.setApplication(this);
        finestraAttuale.setTitle("Council Of Four");
        finestraAttuale.setResizable(false);
        finestraAttuale.setScene(new Scene(root));
        finestraAttuale.show();
    }

    private void initScenaPartita() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/viewgioco.fxml").openStream());
        scenaGioco = new Scene(root);
        ControllerFXPartita controllerFXPartita = loader.getController();
        try {
            GUIView.getInstance().setControllerFXPartita(controllerFXPartita);
        } catch (SingletonNonInizializzatoException e) {
            e.printStackTrace();
        }
    }

    public void showSceltaMappa() throws IOException {
        setFinestraDaFXML("mappegallery.fxml");
    }

    public void showFinestraGioco() throws IOException {
        finestraAttuale.setScene(scenaGioco);
        finestraAttuale.setResizable(true);
        finestraAttuale.show();
    }

    void setIsSocketClient(boolean isSocketClient){
        this.isSocketClient = isSocketClient;
    }

    boolean isSocketClient(){return isSocketClient;}

    public static void main(String[] args) {
        launch(args);
    }
}
