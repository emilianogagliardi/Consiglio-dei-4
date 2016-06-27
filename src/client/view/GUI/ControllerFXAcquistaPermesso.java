package client.view.GUI;

import classicondivise.IdBalcone;
import client.view.GUI.widgetconattributo.ToggleConColore;
import interfaccecondivise.InterfacciaController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by emilianogagliardi on 27/06/16.
 */
public class ControllerFXAcquistaPermesso extends GestoreFlussoFinestra implements Initializable {
    private IdBalcone regione;
    private InterfacciaController controller;
    @FXML
    private HBox hBoxPolitica;
    @FXML
    private RadioButton radioSinistra, radioDestra;
    @FXML
    private Label labelMessaggioMonete;
    @FXML
    private Button btnConferma;
    private ArrayList<ToggleConColore> selezionati = new ArrayList<>();

    void setController(InterfacciaController controller){this.controller = controller;}

    public void setRegione(IdBalcone regione){
        if (regione==IdBalcone.RE) throw new IllegalArgumentException();
        this.regione = regione;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UtilityControllerFX.setAzioneBottoniSoddisfaConsiglio(hBoxPolitica, selezionati, labelMessaggioMonete);
        labelMessaggioMonete.setAlignment(Pos.CENTER);
        radioSinistra.setOnMouseClicked(event -> radioDestra.setSelected(false));
        radioDestra.setOnMouseClicked(event -> radioSinistra.setSelected(false));
        setAzioneBtnConferma();
    }


    private void setAzioneBtnConferma(){
        btnConferma.setOnMouseClicked(event ->{
            List<String> colori = new ArrayList<>();
            selezionati.forEach((ToggleConColore btn) -> colori.add(btn.getColore()));
            if(!colori.isEmpty() && (radioSinistra.isSelected() || radioDestra.isSelected())){
                int carta;
                if(radioDestra.isSelected()) carta = 2;
                else carta = 1;
                try {
                    controller.acquistareTesseraPermessoCostruzione(regione.toString(), colori, carta);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                super.getApplication().chiudiFinestraSecondaria();
            }
        });
    }
}
