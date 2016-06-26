package client.view.GUI;

import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXApplication extends Application {
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
        try {
            GUIView.getInstance().setControllerFXPartita(controllerFXPartita);
        } catch (SingletonNonInizializzatoException e) {
            e.printStackTrace();
        }
    }

    void initControllerMosse(InterfacciaController controller) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/mosse.fxml").openStream());
        ControllerFXMosse controllerFXMosse = loader.getController();
        controllerFXMosse.setController(controller);
        scenaMossa = new Scene(root);
        try{
            GUIView.getInstance().setControllerFXMosse(controllerFXMosse);
        }catch (SingletonNonInizializzatoException e) {
            e.printStackTrace();
        }
    }

    void passaAControllerMosse(){
        finestraSecodaria = new Stage();
        finestraSecodaria.setTitle("Scegli la tua mossa");
        finestraSecodaria.setScene(scenaMossa);
        finestraSecodaria.initOwner(finestraPrincipale);
        finestraSecodaria.show();
    }

    void fineMossa(){
        finestraSecodaria.close();
    }

    void showSceltaMappa() throws IOException {
        setFinestraDaFXML("mappegallery.fxml");
    }

    void showFinestraGioco() throws IOException {
        finestraPrincipale.setScene(scenaGioco);
        finestraPrincipale.setResizable(true);
        finestraPrincipale.show();
    }

    void setIsSocketClient(boolean isSocketClient){
        this.isSocketClient = isSocketClient;
    }

    boolean isSocketClient(){return isSocketClient;}

    public static void main(String[] args) {
        launch(args);
    }
}
