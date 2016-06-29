package client.view.GUI;

import classicondivise.IdBalcone;
import interfaccecondivise.InterfacciaController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/**
 * Created by emilianogagliardi on 27/06/16.
 */
public class ControllerFXCambiaCarte extends GestoreFlussoFinestra implements Initializable{
    private IdBalcone regione;
    private InterfacciaController controller;
    @FXML
    private Button btnConferma;
    @FXML
    private Label label;

    public void setRegione(IdBalcone regione){
        if(regione==IdBalcone.RE) throw new IllegalArgumentException();
        this.regione = regione;
        label.setText("Vuoi rimescolare il mazzo di carte permesso\n di "+ regione.toString().substring(0,1)+ regione.toString().substring(1, regione.toString().length()).toLowerCase() +" e scoprirne due nuove\n pagando un aiutante?");
    }

    public void setController(InterfacciaController controller){this.controller = controller;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnConferma.setOnMouseClicked(event -> {
            try {
                controller.cambiareTesserePermessoCostruzione(regione.toString());
            }catch (RemoteException e){
                e.printStackTrace();
            }
            super.getApplication().chiudiFinestraSecondaria();
        });
    }
}
