package client.view.GUI.utility;

import client.view.GUI.ColoreGiocatore;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.List;

/**
 * Created by emilianogagliardi on 25/06/16.
 */
public class RenderPercorsoNobilta {
    private HashMap<ColoreGiocatore, Integer> posizioneGiocatore;
    List<VBox> vBoxPedine;

    public RenderPercorsoNobilta(List<VBox> vBoxPedine){
        posizioneGiocatore = new HashMap<>();
        this.vBoxPedine = vBoxPedine;
        rendering();
    }

    public void update(ColoreGiocatore colore, int pos){
        if (posizioneGiocatore.containsKey(colore))
            posizioneGiocatore.remove(colore);
        posizioneGiocatore.put(colore, pos);
        rendering();
    }

    private void rendering(){
        //rimuove tutto
        vBoxPedine.forEach(vb -> vb.getChildren().clear());
        //ridisegna tutto
        posizioneGiocatore.forEach((colore, pos) -> vBoxPedine.get(pos).getChildren().add(new Circle(10, UtilityGUI.convertiColore(colore))));
    }
}
