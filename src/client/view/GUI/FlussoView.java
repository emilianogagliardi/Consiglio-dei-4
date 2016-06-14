package client.view.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.view.GUI.controllerFX.GestoreFlussoFinestra;

import java.io.IOException;

public class FlussoView extends Application {
    Stage finestraAttuale;

    @Override
    public void start(Stage primaryStage) throws IOException{
        finestraAttuale = primaryStage;
        setFinestraDaFXML("mappegallery.fxml");
    }

    public void setFinestraDaFXML(String nomeFile) throws IOException{
        //assegna la scena
        FXMLLoader loader = new FXMLLoader();
        Parent root =  loader.load(getClass().getResource("/"+nomeFile).openStream());
        GestoreFlussoFinestra controllerFX = loader.getController();
        controllerFX.setFlusso(this);
        finestraAttuale.setTitle("Council Of Four");
        finestraAttuale.setResizable(false);
        finestraAttuale.setScene(new Scene(root));
        finestraAttuale.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
