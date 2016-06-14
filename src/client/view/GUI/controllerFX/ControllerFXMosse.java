package client.view.GUI.controllerFX;

import controller.InterfacciaController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by emilianogagliardi on 13/06/16.
 */
public class ControllerFXMosse extends GestoreFlussoFinestra implements Initializable{
    private InterfacciaController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setController(InterfacciaController controller){
        this.controller = controller;
    }
}
