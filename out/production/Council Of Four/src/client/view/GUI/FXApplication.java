package client.view.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXApplication extends Application {
    private Stage finestraAttuale;
    private boolean isSocketClient;

    @Override
    public void start(Stage primaryStage) throws IOException{
        finestraAttuale = primaryStage;
        setFinestraDaFXML("login.fxml");
    }

    void setFinestraDaFXML(String nomeFile) throws IOException{
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

    Stage getStage(){return finestraAttuale;}

    void setIsSocketClient(boolean isSocketClient){
        this.isSocketClient = isSocketClient;
    }

    boolean isSocketClient(){return isSocketClient;}

    public static void main(String[] args) {
        launch(args);
    }
}
