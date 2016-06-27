package client.view.GUI;

import classicondivise.NomeCittà;
import client.view.GUI.widgetconattributo.ToggleConColore;
import interfaccecondivise.InterfacciaController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by emilianogagliardi on 27/06/16.
 */
public class ControllerFxCostruzioneRe extends GestoreFlussoFinestra implements Initializable{
    InterfacciaController controller;
    @FXML
    HBox hBoxPolitica;
    @FXML
    private Label labelMessaggioMonete;
    @FXML
    private ArrayList<ToggleConColore> selezionati = new ArrayList<>();
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button btnConferma;

    public void setController(InterfacciaController controller) {this.controller = controller;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UtilityControllerFX.setAzioneBottoniSoddisfaConsiglio(hBoxPolitica, selezionati, labelMessaggioMonete);
        inizializzaComboBox();
        inizializzaBtnConferma();
    }

    private void inizializzaComboBox(){
        Arrays.stream(NomeCittà.values()).forEach(nome -> comboBox.getItems().add(nome.toString().substring(0,1)+nome.toString().substring(1, nome.toString().length()).toLowerCase()));
    }

    private void inizializzaBtnConferma(){
        btnConferma.setOnMouseClicked(event -> {
            if(comboBox.getValue() != null){
                String nomeCitta = comboBox.getValue().toUpperCase();
                List<String> colori = new ArrayList();
                selezionati.forEach((ToggleConColore toggle) -> colori.add(toggle.getColore()));
                try {
                    controller.costruireEmporioConAiutoRe(colori, nomeCitta);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                super.getApplication().chiudiFinestraSecondaria();
            }
        });
    }
}
