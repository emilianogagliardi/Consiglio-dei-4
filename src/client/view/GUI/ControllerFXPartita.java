package client.view.GUI;

import classicondivise.Colore;
import classicondivise.IdBalcone;
import classicondivise.bonus.Bonus;
import classicondivise.bonus.BonusAiutanti;
import classicondivise.bonus.NullBonus;
import client.view.GUI.customevent.ShowViewGiocoEvent;
import client.view.eccezioni.SingletonNonInizializzatoException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerFXPartita extends GestoreFlussoFinestra implements Initializable{
    private int idGiocatore;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView immagineMappa, cartaCollinaCoperta, cartaMontagnaCoperta, cartaCostaCoperta;
    @FXML
    private HBox balconeRe, balconeCollina, balconeCosta, balconeMontagna;
    @FXML
    private TextArea areaNotifiche;
    @FXML
    private HBox bonusArkon, bonusBurgen, bonusCastrum, bonusDorful, bonusEsti, bonusFramek, bonusGraden, bonusIndur,
            bonusHellar, bonusKultos, bonusLyram, bonusNaris, bonusMerkatim, bonusOsium;
    @FXML
    private HBox emporiArkon, emporiBurgen, emporiCastrum, emporiDorful, emporiEsti, emporiFramek, emporiGraden, emporiIndur,
            emporiHellar, emporiKultos, emporiLyram, emporiNaris, emporiMerkatim, emporiOsium;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        List<Node> nodi = scegliBalcone(idBalcone).getChildren();
        List<ImageView> imageViewConsiglieri = new ArrayList<>();
        nodi.forEach(nodo -> imageViewConsiglieri.add((ImageView) nodo));
        imageViewConsiglieri.get(0).setImage(scegliImmagine(colore1));
        imageViewConsiglieri.get(1).setImage(scegliImmagine(colore2));
        imageViewConsiglieri.get(2).setImage(scegliImmagine(colore3));
        imageViewConsiglieri.get(3).setImage(scegliImmagine(colore4));
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
    private Image scegliImmagine(String colore) {
        Image image;
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
        return image;
    }



    //TODO
    public void updateBonusCittà(Bonus bonus){
        int i = 0;

        while (!(bonus instanceof NullBonus)){
            if (bonus instanceof BonusAiutanti) {

            }
        }
    }

    //TODO sono già pronte le immagini degli empori
    private void updateEmporiCittà(){

    }

    public void nuovoMessaggio(String messaggio) {
        areaNotifiche.appendText(messaggio);
    }

    //permette di firare eventi al root pane dall'esterno
    public Parent getRootPane(){
        return rootPane;
    }
}
