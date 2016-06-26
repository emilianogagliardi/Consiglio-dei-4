package client.view.GUI;

import classicondivise.IdBalcone;
import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXApplication extends Application {
    InterfacciaController controller;
    private Stage finestraPrincipale;
    private Stage finestraSecodaria;
    private boolean isSocketClient;
    private Scene scenaGioco;
    private Scene scenaMossa;

    @Override
    public void start(Stage primaryStage) throws IOException{
        GUIView.initGUIView(this);
        initScenaPartita();
        finestraPrincipale = primaryStage;
        setFinestraDaFXML("login.fxml");
    }

    void setController(InterfacciaController controller){
        this.controller = controller;
    }

    private void setFinestraDaFXML(String nomeFile) throws IOException{
        //assegna la scena
        FXMLLoader loader = new FXMLLoader();
        Parent root =  loader.load(getClass().getResource("/"+nomeFile).openStream());
        GestoreFlussoFinestra controllerFX = loader.getController();
        controllerFX.setApplication(this);
        finestraPrincipale.setTitle("Council Of Four");
        finestraPrincipale.setResizable(false);
        finestraPrincipale.setScene(new Scene(root));
        finestraPrincipale.show();
    }

    private void initScenaPartita() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/viewgioco.fxml").openStream());
        scenaGioco = new Scene(root);
        ControllerFXPartita controllerFXPartita = loader.getController();
        controllerFXPartita.setApplication(this);
        try {
            GUIView.getInstance().setControllerFXPartita(controllerFXPartita);
        } catch (SingletonNonInizializzatoException e) {
            e.printStackTrace();
        }
    }

    void showSceltaMappa() throws IOException {
        setFinestraDaFXML("mappegallery.fxml");
    }

    void showFinestraGioco() throws IOException {
        finestraPrincipale.setScene(scenaGioco);
        finestraPrincipale.setResizable(true);
        finestraPrincipale.show();
    }

    void showMossaAcquistaPermesso(){
        FXMLLoader loader = new FXMLLoader();
        try {
            Parent root = loader.load(getClass().getClassLoader().getResource("acquistapermesso.fxml"));
            scenaMossa = new Scene(root);
            finestraSecodaria = new Stage();
            finestraSecodaria.setScene(scenaMossa);
            finestraSecodaria.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void showMossaCostruzioneEmporio(){
        FXMLLoader loader = new FXMLLoader();
        try {
            Parent root = loader.load(getClass().getClassLoader().getResource("costruzioneconpermit.fxml"));
            scenaMossa = new Scene(root);
            finestraSecodaria = new Stage();
            finestraSecodaria.setScene(scenaMossa);
            finestraSecodaria.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void showMossaEleggiConsigliere(IdBalcone balcone){
        FXMLLoader loader = new FXMLLoader();
        try {
            Parent root = loader.load(getClass().getClassLoader().getResource("eleggiconsigliere.fxml").openStream());
            ControllerFXEleggiConsigliere controllerFXEleggiConsigliere = loader.getController();
            controllerFXEleggiConsigliere.setController(controller);
            controllerFXEleggiConsigliere.setBalcone(balcone);
            scenaMossa = new Scene(root);
            finestraSecodaria = new Stage();
            finestraSecodaria.setScene(scenaMossa);
            finestraSecodaria.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void showMossaCostruzioneRe(){
        FXMLLoader loader = new FXMLLoader();
        try {
            Parent root = loader.load(getClass().getClassLoader().getResource("costruzionere.fxml"));
            scenaMossa = new Scene(root);
            finestraSecodaria = new Stage();
            finestraSecodaria.setScene(scenaMossa);
            finestraSecodaria.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void chiudiFinestraSecondaria(){
        if (finestraSecodaria.isShowing()) finestraSecodaria.close();
    }

    void setIsSocketClient(boolean isSocketClient){
        this.isSocketClient = isSocketClient;
    }

    boolean isSocketClient(){return isSocketClient;}

    public static void main(String[] args) {
        launch(args);
    }
}
