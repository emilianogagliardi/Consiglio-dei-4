package client.view.GUI.utility;

import classicondivise.bonus.*;
import classicondivise.carte.CartaPermessoCostruzione;
import client.view.GUI.ColoreGiocatore;
import client.view.GUI.widgetconattributo.ToggleConColore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emilianogagliardi on 24/06/16.
 */
public class UtilityGUI {

    public void creaPermit(CartaPermessoCostruzione carta, double hCarta, double wCarta, StackPane stackPane, boolean conBonus) {
        stackPane.setPrefSize(wCarta, hCarta);
        ImageView imgView = new ImageView();
        stackPane.getChildren().add(imgView);
        //StackPane.setAlignment(imgView, Pos.CENTER);
        imgView.setImage(new Image(getClass().getClassLoader().getResourceAsStream("permesso_vuota.png"), wCarta, hCarta, true, true));
        Label citta = new Label ();
        Font font = new Font("Chalkduster", hCarta/8);
        citta.setFont(font);
        final String[] stringa = {""}; //escamotage per modificare la stringa nell'espressione lambda
        carta.getCittà().forEach(nome -> stringa[0] += nome.toString().substring(0,1) + "/");
        //scrive nel label la stringa del tipo C/D/A/ eliminando l'ultimo slash
        citta.setText(stringa[0].substring(0,stringa[0].length()-1));
        stackPane.getChildren().add(citta);
        if(conBonus) {
            StackPane.setAlignment(citta, Pos.TOP_CENTER);
            StackPane.setMargin(citta, new Insets(hCarta/5, wCarta/7, 0, wCarta/7));
            HBox hBox = new HBox();
            addImmaginiBonus(hBox, hCarta / 4, wCarta / 4, (int) hCarta / 10, carta.getBonus());
            stackPane.getChildren().add(hBox);
            StackPane.clearConstraints(hBox);
            StackPane.setAlignment(hBox, Pos.CENTER);
            hBox.setTranslateY(hCarta / 5);
            hBox.setTranslateX(wCarta / 7);
        } else{
            StackPane.setAlignment(citta, Pos.CENTER);
        }
    }

    public void addImmaginiBonus(HBox hBox, double hImg, double wImg, int fontSize, Bonus bonus){
        Image img = null;
        while (!(bonus instanceof NullBonus)){
            StackPane stackPane = new StackPane();
            ImageView imageView = new ImageView();
            stackPane.getChildren().add(imageView);
            if (bonus instanceof BonusAiutanti) {
                img = new Image(getClass().getClassLoader().getResourceAsStream("bonus_aiutante.png"), wImg, hImg, true, true);
                scriviNumeroBonus(((BonusAiutanti) bonus).getNumeroAiutanti(), fontSize, stackPane);
            }else if(bonus instanceof BonusMonete) {
                img = new Image(getClass().getClassLoader().getResourceAsStream("bonus_monete.png"), wImg, hImg, true, true);
                scriviNumeroBonus(((BonusMonete) bonus).getNumeroMonete(), fontSize, stackPane);
            }else if(bonus instanceof BonusAvanzaPercorsoNobiltà) {
                img = new Image(getClass().getClassLoader().getResourceAsStream("bonus_percorsonobilta.png"), wImg, hImg, true, true);
                scriviNumeroBonus(((BonusAvanzaPercorsoNobiltà) bonus).getNumeroPosti(), fontSize, stackPane);
            }else if(bonus instanceof BonusRipetiAzionePrincipale) {
                img = new Image(getClass().getClassLoader().getResourceAsStream("bonus_ripetiazione.png"), wImg, hImg, true, true);
            }else if(bonus instanceof BonusPescaCartaPolitica) {
                img = new Image(getClass().getClassLoader().getResourceAsStream("bonus_cartapolitica.png"), wImg, hImg, true, true);
                scriviNumeroBonus(((BonusPescaCartaPolitica) bonus).getNumeroCarte(), fontSize, stackPane);
            }else if(bonus instanceof BonusPuntiVittoria){
                img = new Image(getClass().getClassLoader().getResourceAsStream("bonus_puntivittoria.png"), wImg, hImg, true, true);
                scriviNumeroBonus(((BonusPuntiVittoria) bonus).getPuntiVittoria(), fontSize, stackPane);
            }
            imageView.setImage(img);
            hBox.getChildren().add(stackPane);
            bonus = ((RealBonus) bonus).getDecoratedBonus();
        }
    }

    //si aspetta uno stack pane contenente una image view
    private void scriviNumeroBonus(int numero, int fontSize, StackPane stackPane) {
        Label label = new Label(numero+"");
        Font font = new Font(fontSize);
        label.setTextFill(Color.WHITESMOKE);
        label.setFont(font);
        stackPane.getChildren().add(label);
        StackPane.clearConstraints(label);
        StackPane.setAlignment(label, Pos.CENTER);
    }

    public void addPoliticaInHBox(HBox hBox, List<String> carte){
        ArrayList<ImageView> imgViews = new ArrayList<>();
        carte.forEach(colore -> {
            ImageView img = new ImageView();
            String nomeFile = "politica_" + colore.toLowerCase() + ".png";
            img.setImage(new Image(getClass().getClassLoader().getResourceAsStream(nomeFile)));
            imgViews.add(img);
        });
        imgViews.forEach(imageView -> hBox.getChildren().add(imageView));
    }

    public void addPoliticaInBoxAsToggle(HBox hBox, List<String> carte){
        ArrayList<ToggleButton> btns = new ArrayList<>();
        carte.forEach(colore -> {
            ToggleConColore toggleButton = new ToggleConColore(colore);
            String nomeFile = "politica_" + colore.toLowerCase() + ".png";
            toggleButton.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(nomeFile))));
            btns.add(toggleButton);
        });
        btns.forEach(btn -> hBox.getChildren().add(btn));
    }

    public static Color convertiColore(ColoreGiocatore coloreGiocatore){
        switch (coloreGiocatore){
            case ROSSO:
                return Color.RED;
            case GIALLO:
                return Color.YELLOW;
            case VERDE:
                return Color.GREEN;
            case BLU:
                return Color.BLUE;
            default: throw new IllegalArgumentException();
        }
    }
}
