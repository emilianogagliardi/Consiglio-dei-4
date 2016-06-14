package client.view.GUI.controllerFX;

import interfaccecondivise.InterfacciaController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;


public class ControllerFXMosse extends GestoreFlussoFinestra implements Initializable{
    private InterfacciaController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setController(InterfacciaController controller){
        this.controller = controller;
    }
}
