package client.view.GUI;

import client.view.GUI.utility.UtilityGUI;
import client.view.GUI.widgetconattributo.ToggleConColore;
import client.view.GiocatoreView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

/**
 * Created by emilianogagliardi on 27/06/16.
 */
public class UtilityControllerFX {
    public static void setAzioneBottoniSoddisfaConsiglio(HBox hBoxPolitica, ArrayList<ToggleConColore> selezionati, Label labelMessaggioMonete){
        UtilityGUI utilityGUI = new UtilityGUI();
        utilityGUI.addPoliticaInBoxAsToggle(hBoxPolitica, GiocatoreView.getInstance().getCartePolitica());
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
}
