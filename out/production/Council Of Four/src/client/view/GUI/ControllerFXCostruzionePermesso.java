package client.view.GUI;

import classicondivise.NomeCittà;
import classicondivise.carte.CartaPermessoCostruzione;
import client.view.GUI.utility.UtilityGUI;
import client.view.GUI.widgetconattributo.ToggleConCartaPermesso;
import client.view.GUI.widgetconattributo.ToggleConStringa;
import client.view.GiocatoreView;
import interfaccecondivise.InterfacciaController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
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

    private ArrayList<ToggleConStringa> toggleCitta;

    InterfacciaController controller;

    void setController(InterfacciaController controller){this.controller = controller;}

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
        setAzioneBtnConferma();
        setAzioneBtnCarte();
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

    private void disabilitaAltri(ToggleConCartaPermesso togglePremuto){
        hBoxPermit.getChildren().forEach((Node nodo) -> {
            ToggleButton otherToggle = (ToggleButton) nodo;
            if (!otherToggle.equals(togglePremuto)) otherToggle.setSelected(false);
        });
    }

    private void mostraInizialiCittaCostruibili(HashSet<NomeCittà> nomiCittà){
        toggleCitta = new ArrayList<>();
        nomiCittà.forEach(nomeCittà -> {
            ToggleConStringa toggleConStringa = new ToggleConStringa(nomeCittà.toString().toUpperCase());
            toggleConStringa.setText(nomeCittà.toString().substring(0,1).toUpperCase()+nomeCittà.toString().substring(1, nomeCittà.toString().length()).toLowerCase());
            toggleCitta.add(toggleConStringa);
            toggleConStringa.setOnMouseClicked(event -> {
                btnBar.getButtons().forEach(btn -> { //disbilita altri
                    ToggleButton other = (ToggleButton) btn;
                    if(!other.equals(toggleConStringa)) other.setSelected(false);
                });
            });
        } );
        btnBar.getButtons().clear();
        toggleCitta.forEach(toggleCitta -> btnBar.getButtons().add(toggleCitta));
    }

    private void setAzioneBtnConferma(){
        btnConferma.setOnMouseClicked(event -> {
            ToggleConStringa selezionato = null;
            if(btnBar.getButtons().size() > 0  && ((ToggleButton)btnBar.getButtons().get(0)).isSelected()) selezionato = (ToggleConStringa) btnBar.getButtons().get(0);
            if(btnBar.getButtons().size() > 1 && ((ToggleButton)btnBar.getButtons().get(1)).isSelected()) selezionato = (ToggleConStringa) btnBar.getButtons().get(1);
            if(btnBar.getButtons().size() > 2 && ((ToggleButton)btnBar.getButtons().get(2)).isSelected()) selezionato = (ToggleConStringa) btnBar.getButtons().get(2);
            if(selezionato != null){
                CartaPermessoCostruzione cartaSelezionata = null;
                for (Node nodo : hBoxPermit.getChildren()){
                    ToggleConCartaPermesso corrente = (ToggleConCartaPermesso) nodo;
                    if (corrente.isSelected()){
                        cartaSelezionata = corrente.getCarta();
                        break;
                    }
                }
                if (cartaSelezionata != null){
                    try {
                        controller.costruireEmporioConTesseraPermessoCostruzione(cartaSelezionata, selezionato.getStringa().toUpperCase());
                        super.getApplication().chiudiFinestraSecondaria();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
