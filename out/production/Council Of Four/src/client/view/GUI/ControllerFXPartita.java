package client.view.GUI;

import classicondivise.IdBalcone;
import classicondivise.bonus.*;
import client.view.GUI.customevent.ShowViewGiocoEvent;
import client.view.eccezioni.SingletonNonInizializzatoException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.lang.reflect.Field;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerFXPartita extends GestoreFlussoFinestra implements Initializable{
    private int idGiocatore;
    @FXML
    private AnchorPane rootPane, anchorInScroll;
    @FXML
    private ImageView immagineMappa, cartaCollinaCoperta, cartaMontagnaCoperta, cartaCostaCoperta;
    @FXML
    private HBox balconeRe, balconeCollina, balconeCosta, balconeMontagna;
    @FXML
    private TextArea areaNotifiche;
    //sono utilizzati ottenendo l'attributo tramite il loro nome, per evitare noiosi switch case
    @FXML
    private HBox bonusArkon, bonusBurgen, bonusCastrum, bonusDorful, bonusEsti, bonusFramek, bonusGraden, bonusIndur,
            bonusHellar, bonusKultos, bonusLyram, bonusNaris, bonusMerkatim, bonusOsium, bonusJuvelar;
    @FXML
    private HBox emporiArkon, emporiBurgen, emporiCastrum, emporiDorful, emporiEsti, emporiFramek, emporiGraden, emporiIndur,
            emporiHellar, emporiKultos, emporiLyram, emporiNaris, emporiMerkatim, emporiOsium;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootPane.setBackground(new Background(new BackgroundImage(new Image(getClass().getClassLoader().getResourceAsStream("sfondo.jpg")), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        try {
            idGiocatore = GUIView.getInstance().getIdGiocatore();
        } catch (RemoteException | SingletonNonInizializzatoException e) {
            e.printStackTrace();
        }
        //assegna l'handler di mostra mappa all'evento di start del gioco
        inizializzaImmagineMappa();
        //inizializza le immagini di retro delle carte permit
        inizializzaImmaginiCarte();
        //il giocatore non può scrivere in area notifiche
        areaNotifiche.setEditable(false);
    }

    //l'algoritmo di caricamento deve essere eguito sullo show, nel momento il cui si conosce già quale deve essere l'immagine da mostrare
    private void inizializzaImmagineMappa(){
        //inizializza
        rootPane.addEventHandler(ShowViewGiocoEvent.SHOW_IMAGE, new EventHandler<ShowViewGiocoEvent>(){
            @Override
            public void handle(ShowViewGiocoEvent event) {
                int idMappa = 0;
                try {
                    idMappa = GUIView.getInstance().getIdMappa();
                } catch (SingletonNonInizializzatoException e) {
                    e.printStackTrace();
                }
                String nomeFile = "mappa"+idMappa+"_gioco.jpg";
                Image immagine = new Image(getClass().getClassLoader().getResourceAsStream(nomeFile));
                immagineMappa.setImage(immagine);
            }
        });
    }

    private void inizializzaImmaginiCarte() {
        cartaCollinaCoperta.setImage(new Image(getClass().getClassLoader().getResourceAsStream("retro_carta_collina.jpg")));
        cartaCostaCoperta.setImage(new Image(getClass().getClassLoader().getResourceAsStream("retro_carta_costa.jpg")));
        cartaMontagnaCoperta.setImage(new Image(getClass().getClassLoader().getResourceAsStream("retro_carta_montagna.jpg")));
    }

    public void updateBalcone(String idBalcone, String colore1, String colore2, String colore3, String colore4) {
        boolean isBalconeRe = IdBalcone.valueOf(idBalcone).equals(IdBalcone.RE);
        List<Node> nodi = scegliBalcone(idBalcone).getChildren();
        List<ImageView> imageViewConsiglieri = new ArrayList<>();
        nodi.forEach(nodo -> imageViewConsiglieri.add((ImageView) nodo));
        imageViewConsiglieri.get(0).setImage(scegliImmagine(colore1, isBalconeRe));
        imageViewConsiglieri.get(1).setImage(scegliImmagine(colore2, isBalconeRe));
        imageViewConsiglieri.get(2).setImage(scegliImmagine(colore3, isBalconeRe));
        imageViewConsiglieri.get(3).setImage(scegliImmagine(colore4, isBalconeRe));
    }

    //metodo di utility per update balcone, sceglie il balcone in base all'id
    private HBox scegliBalcone(String idBalcone) {
        switch (IdBalcone.valueOf(idBalcone)){
            case RE:
                return balconeRe;
            case COLLINA:
                return balconeCollina;
            case COSTA:
                return balconeCosta;
            case MONTAGNA:
                return balconeMontagna;
            default:
                throw new IllegalArgumentException();
        }
    }

    //metodo di utility per update balcone, sceglie l'immagine in base al colore
    //true se balcone del re
    private Image scegliImmagine(String colore, boolean isBalconeRe) {
        String nomeImg = "consigliere_" + colore.toLowerCase();
        if(isBalconeRe) nomeImg += "_re";
        nomeImg += ".png";
        return new Image(getClass().getClassLoader().getResourceAsStream(nomeImg));/*
        switch (Colore.valueOf(colore)) {
            case ARANCIONE:
                image = new Image(getClass().getClassLoader().getResourceAsStream("consigliere_arancione.png"));
                break;
            case AZZURRO:
                image = new Image(getClass().getClassLoader().getResourceAsStream("consigliere_azzurro.png"));
                break;
            case BIANCO:
                image = new Image(getClass().getClassLoader().getResourceAsStream("consigliere_bianco.png"));
                break;
            case NERO:
                image = new Image(getClass().getClassLoader().getResourceAsStream("consigliere_nero.png"));
                break;
            case ROSA:
                image = new Image(getClass().getClassLoader().getResourceAsStream("consigliere_rosa.png"));
                break;
            case VIOLA:
                image = new Image(getClass().getClassLoader().getResourceAsStream("consigliere_viola.png"));
                break;
            default:
                throw new IllegalArgumentException();
        }
        return image;*/
    }



    //TODO
    public void updateBonusCittà(String nomeCittà, Bonus bonus){
        Comparator<Node> piuADestra = (Node n1, Node n2) -> ((Double)n2.getLayoutX()).compareTo(n1.getLayoutX());
        HBox hBoxBonus;
        Class c = this.getClass();
        try {
            //ottiene l'attributo "bonusNomecittà" di this.getClass, per evitare uno switch case di 15 case.
            Field fieldHBox = c.getDeclaredField("bonus" + nomeCittà.substring(0, 1).toUpperCase() + nomeCittà.substring(1).toLowerCase());
            hBoxBonus = (HBox) fieldHBox.get(this);
            List<Node> nodi = hBoxBonus.getChildren();
            //parte ad inserire le immagini dall'ultimo elemento dell'hbox, per questioni grafiche
            List<ImageView> immagini = new ArrayList<>();
            immagini.add((ImageView) nodi.get(0));
            immagini.add((ImageView) nodi.get(1));
            immagini.add((ImageView) nodi.get(2));
            immagini.sort(piuADestra);
            int i = 0;
            while (!(bonus instanceof NullBonus)){
                if (bonus instanceof BonusAiutanti) {
                    immagini.get(i).setImage(new Image(getClass().getClassLoader().getResourceAsStream("bonus_aiutante.png")));
                    scriviNumeroBonus(((BonusAiutanti) bonus).getNumeroAiutanti(), hBoxBonus, immagini.get(i));
                }else if(bonus instanceof BonusMonete) {
                    immagini.get(i).setImage(new Image(getClass().getClassLoader().getResourceAsStream("bonus_monete.png")));
                    scriviNumeroBonus(((BonusMonete) bonus).getNumeroMonete(), hBoxBonus, immagini.get(i));
                }else if(bonus instanceof BonusAvanzaPercorsoNobiltà) {
                    immagini.get(i).setImage(new Image(getClass().getClassLoader().getResourceAsStream("bonus_percorsonobilta.png")));
                    scriviNumeroBonus(((BonusAvanzaPercorsoNobiltà) bonus).getNumeroPosti(), hBoxBonus, immagini.get(i));
                }else if(bonus instanceof BonusRipetiAzionePrincipale) {
                    immagini.get(i).setImage(new Image(getClass().getClassLoader().getResourceAsStream("bonus_ripetiazione.png")));
                }else if(bonus instanceof BonusPescaCartaPolitica) {
                    immagini.get(i).setImage(new Image(getClass().getClassLoader().getResourceAsStream("bonus_cartapolitica.png")));
                    scriviNumeroBonus(((BonusPescaCartaPolitica) bonus).getNumeroCarte(), hBoxBonus, immagini.get(i));
                }else if(bonus instanceof BonusPuntiVittoria){
                    immagini.get(i).setImage(new Image(getClass().getClassLoader().getResourceAsStream("bonus_puntivittoria.png")));
                    scriviNumeroBonus(((BonusPuntiVittoria) bonus).getPuntiVittoria(), hBoxBonus, immagini.get(i));
                }
                bonus = ((RealBonus) bonus).getDecoratedBonus();
                i++;
            }
        }catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }
    }


    public void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori){

    }

    public void nuovoMessaggio(String messaggio) {
        areaNotifiche.appendText(messaggio+"\n");
    }

    //permette di firare eventi al root pane dall'esterno
    public Parent getRootPane(){
        return rootPane;
    }

    private void scriviNumeroBonus(int numero, HBox hBox, ImageView immagine) {
        double x = hBox.getLayoutX();
        double y = hBox.getLayoutY();
        x += immagine.getLayoutX();
        y += immagine.getLayoutY();
        x += immagine.getFitWidth()/2;
        y += immagine.getFitHeight()/2;
        Label label = new Label(numero+"");
        Font font = new Font(15);
        label.setTextFill(Color.WHITESMOKE);
        label.setFont(font);
        anchorInScroll.getChildren().add(label);
        label.setLayoutX(x);
        label.setLayoutY(y);
    }
}
