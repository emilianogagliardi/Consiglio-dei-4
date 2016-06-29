package client.view.GUI;

import interfaccecondivise.InterfacciaController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/**
 * Created by emilianogagliardi on 27/06/16.
 */
public class ControllerFXAiutantiOMossa extends GestoreFlussoFinestra implements Initializable{
    private InterfacciaController controller;
    @FXML
    private RadioButton ottieniAiutanti, ottieniMossa;
    @FXML
    private Button btnConferma;
    @FXML
    private Label label;

    public void setController(InterfacciaController controller){
        this.controller = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label.setText("Vuoi ottenere un aiutante pagando 3 monete\no ripetere un azione principale pagando 3 aiutanti?");
        ottieniAiutanti.setOnMouseClicked(event -> ottieniMossa.setSelected(false));
        ottieniMossa.setOnMouseClicked(event -> ottieniAiutanti.setSelected(false));
        btnConferma.setOnMouseClicked(event -> {
            try {
                if (ottieniAiutanti.isSelected()) {
                    controller.ingaggiareAiutante();
                    super.getApplication().chiudiFinestraSecondaria();
                }else if(ottieniMossa.isSelected()){
                    controller.compiereAzionePrincipaleAggiuntiva();
                    super.getApplication().chiudiFinestraSecondaria();
                }
            }catch(RemoteException e){
                e.printStackTrace();
            }
        });
    }


}
