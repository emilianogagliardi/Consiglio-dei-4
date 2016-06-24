package client.view.GUI.utility;

import classicondivise.carte.CartaPermessoCostruzione;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Created by emilianogagliardi on 24/06/16.
 */
public class CreatoreCartaPermesso {
    public void creaPermit(CartaPermessoCostruzione carta, StackPane stackPane) {
        ImageView imgView = new ImageView();
        stackPane.getChildren().add(imgView);
        imgView.setImage(new Image(getClass().getClassLoader().getResourceAsStream("permesso_vuota.png"), 75, 90, true, true));
        Label citta = new Label ();
        citta.setLayoutY(10);
        StackPane.setAlignment(citta, Pos.TOP_CENTER);
        StackPane.setMargin(citta, new Insets(10));
        final String[] stringa = {""}; //escamotage per modificare la stringa nell'espressione lambda
        carta.getCittà().forEach(nome -> stringa[0] += nome.toString().substring(0,1) + "/");
        //scrive nel label la stringa del tipo C/D/A/ eliminando l'ultimo slash
        citta.setText(stringa[0].substring(0,stringa[0].length()-1));
        stackPane.getChildren().add(citta);
        //TODO aggiungere i bonus alle carte permesso
    }

    private void addImmaginiBonus(){

    }
}
