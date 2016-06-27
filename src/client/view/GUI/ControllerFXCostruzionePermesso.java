package client.view.GUI;

import classicondivise.NomeCittà;
import classicondivise.carte.CartaPermessoCostruzione;
import client.view.GUI.utility.UtilityGUI;
import client.view.GUI.widgetconattributo.ToggleConCartaPermesso;
import client.view.GiocatoreView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by emilianogagliardi on 27/06/16.
 */
public class ControllerFXCostruzionePermesso extends GestoreFlussoFinestra implements Initializable {
    @FXML
    private HBox hBoxPermit;
    @FXML
    private ButtonBar btnBar;
    @FXML
    private Button btnConferma;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<CartaPermessoCostruzione> carte = GiocatoreView.getInstance().getCartePermesso();
        carte.forEach(carta ->{
            ToggleConCartaPermesso toggle = new ToggleConCartaPermesso(carta);
            StackPane vistaCarta = new StackPane();
            UtilityGUI utilityGUI = new UtilityGUI();
            utilityGUI.creaPermit(carta, 100, 83, vistaCarta, false);
            toggle.setGraphic(vistaCarta);
            hBoxPermit.getChildren().add(toggle);
        });
    }

    private void setAzioneBtnCarte(){
        hBoxPermit.getChildren().forEach((Node nodo) ->{
            ToggleConCartaPermesso toggle = (ToggleConCartaPermesso) nodo;
            toggle.setOnMouseClicked(event -> {
                disabilitaAltri(toggle);
                mostraInizialiCittaCostruibili(toggle.getCarta().getCittà());
            });
        });
    }

    private void disabilitaAltri(ToggleConCartaPermesso toggle){

    }

    private void mostraInizialiCittaCostruibili(HashSet<NomeCittà> nomiCittà){

    }

    private void setAzioneBtnConferma(){

    }

}
