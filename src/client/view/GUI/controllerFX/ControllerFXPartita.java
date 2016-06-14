package client.view.GUI.controllerFX;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerFXPartita extends GestoreFlussoFinestra implements Initializable{
    private int idGiocatore;
    @FXML
    ImageView immagineMappa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setIdGiocatore(int id){
        idGiocatore = id;
    }
}
