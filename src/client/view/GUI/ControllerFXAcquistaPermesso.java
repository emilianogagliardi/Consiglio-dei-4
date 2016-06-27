package client.view.GUI;

import classicondivise.IdBalcone;
import client.view.GUI.utility.UtilityGUI;
import client.view.GUI.widgetconattributo.ToggleConColore;
import client.view.GiocatoreView;
import interfaccecondivise.InterfacciaController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
        UtilityGUI utilityGUI = new UtilityGUI();
        utilityGUI.addPoliticaInBoxAsToggle(hBoxPolitica, GiocatoreView.getInstance().getCartePolitica());
        setAzioniBottoni();
        setAzioneBtnConferma();
    }

    private void setAzioniBottoni(){
        hBoxPolitica.getChildren().forEach((Node node) ->{
            ToggleConColore btn = (ToggleConColore) node;
            btn.setOnMouseClicked(event -> {
                if (selezionati.size() == 4) {
                    selezionati.get(0).setSelected(false);
                    selezionati.remove(0);
                }
                switch (selezionati.size()){
                    case 0:
                        labelMessaggioMonete.setText("Il soddisfacimento del consiglio richiede un supplemento in monete");
                        break;
                    case 1:
                        labelMessaggioMonete.setText("Il soddisfacimento del consiglio richiede un supplemento di 10 monete, più una per ogni carta jolly");
                        break;
                    case 2:
                        labelMessaggioMonete.setText("Il soddisfacimento del consiglio richiede un supplemento di 7 monete, più una per ogni carta jolly");
                        break;
                    case 3:
                        labelMessaggioMonete.setText("Il soddisfacimento del consiglio richiede un supplemento di 4 monete, più una per ogni carta jolly");
                        break;
                    case 4:
                        labelMessaggioMonete.setText("Il soddisfacimento del consiglio richiede un supplemento di 1 monete, più una per ogni carta jolly");
                        break;
                }
                selezionati.add(btn);
            });
        });
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
