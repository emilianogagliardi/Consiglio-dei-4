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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.lang.reflect.Field;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class ControllerFXPartita extends GestoreFlussoFinestra implements Initializable{
    @FXML
    private AnchorPane rootPane, anchorInScroll;
    @FXML
    private HBox hBoxPolitica;
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
    @FXML
    private ImageView imageViewMonete, imageViewAiutanti, imageViewPunti, imgTabellaPunti, imgTabellaMonete, imgTabellaAiutanti, imgTabellaNCarte;
    @FXML
    private Label moneteGiocatore, puntiVittoriaGiocatore, aiutantiGiocatore;
    @FXML
    private Label labelId1Giocatore, labelId2Giocatore, labelId3Giocatore;
    @FXML
    private Label puntiAvversario1, puntiAvversario2, puntiAvversario3;
    @FXML
    private Label moneteAvversario1, moneteAvversario2, moneteAvversario3;
    @FXML
    private Label aiutantiAvversario1, aiutantiAvversario2, aiutantiAvversario3;
    @FXML
    private Label numCarteAvversario1, numCarteAvversario2, numCarteAvversario3;
    //utility
    private HashMap<Integer, Label> idAvversarioLabelMonete = new HashMap<>();
    private HashMap<Integer, Label> idAvversarioLabelAiutanti = new HashMap<>();
    private HashMap<Integer, Label> idAvversarioLabelPunti = new HashMap<>();
    private HashMap<Integer, Label> idAvversarioLabelNCarte = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //assegna l'handler di mostra mappa all'evento di start del gioco
        inizializzaImmagineMappa();
        //inizializza le immagini di retro delle carte permit
        inizializzaImmaginiCarte();
        //il giocatore non può scrivere in area notifiche
        areaNotifiche.setEditable(false);
        inizializzaImmaginiRisorseGiocatori();
        inizializzaImmaginiBalconiDiLegno();
        inizializzaHashMap();
    }

    private void inizializzaHashMap(){
        //inizializza
        rootPane.addEventHandler(ShowViewGiocoEvent.SHOW_IMAGE, new EventHandler<ShowViewGiocoEvent>() {
            @Override
            public void handle(ShowViewGiocoEvent event) {
                try {
                    int myId = GUIView.getInstance().getIdGiocatore();
                    Integer[] tuttiGliIdArray = {0, 1, 2, 3};
                    ArrayList<Integer> tuttiGliId = new ArrayList<>(3);
                    Arrays.stream(tuttiGliIdArray).filter((Integer id) -> id != myId).forEach(id -> tuttiGliId.add(id));
                    labelId1Giocatore.setText("Giocatore" + tuttiGliId.get(0));
                    labelId2Giocatore.setText("Giocatore" + tuttiGliId.get(1));
                    labelId3Giocatore.setText("Giocatore" + tuttiGliId.get(2));
                    idAvversarioLabelMonete.put(tuttiGliId.get(0), moneteAvversario1);
                    idAvversarioLabelMonete.put(tuttiGliId.get(1), moneteAvversario2);
                    idAvversarioLabelMonete.put(tuttiGliId.get(2), moneteAvversario3);
                    idAvversarioLabelPunti.put(tuttiGliId.get(0), puntiAvversario1);
                    idAvversarioLabelPunti.put(tuttiGliId.get((1)), puntiAvversario2);
                    idAvversarioLabelPunti.put(tuttiGliId.get(2), puntiAvversario3);
                    idAvversarioLabelAiutanti.put(tuttiGliId.get(0),aiutantiAvversario1);
                    idAvversarioLabelAiutanti.put(tuttiGliId.get(1),aiutantiAvversario2);
                    idAvversarioLabelAiutanti.put(tuttiGliId.get(2),aiutantiAvversario3);
                    idAvversarioLabelNCarte.put(tuttiGliId.get(0),numCarteAvversario1);
                    idAvversarioLabelNCarte.put(tuttiGliId.get(1),numCarteAvversario2);
                    idAvversarioLabelNCarte.put(tuttiGliId.get(2),numCarteAvversario3);
                    System.out.println(idAvversarioLabelAiutanti);
                } catch (SingletonNonInizializzatoException | RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //l'algoritmo di caricamento deve essere eguito sullo show, nel momento il cui si conosce già quale deve essere l'immagine da mostrare
    private void inizializzaImmagineMappa(){
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
        cartaCollinaCoperta.setImage(new Image(getClass().getClassLoader().getResourceAsStream("retro_carta_collina.png")));
        cartaCostaCoperta.setImage(new Image(getClass().getClassLoader().getResourceAsStream("retro_carta_costa.png")));
        cartaMontagnaCoperta.setImage(new Image(getClass().getClassLoader().getResourceAsStream("retro_carta_montagna.png")));
    }

    private void inizializzaImmaginiRisorseGiocatori() {
        Image monete = new Image(getClass().getClassLoader().getResourceAsStream("bonus_monete.png"));
        Image aiutanti = new Image(getClass().getClassLoader().getResourceAsStream("bonus_aiutante.png"));
        Image punti = new Image(getClass().getClassLoader().getResourceAsStream("bonus_puntivittoria.png"));
        imageViewAiutanti.setImage(aiutanti);
        imgTabellaAiutanti.setImage(aiutanti);
        imageViewMonete.setImage(monete);
        imgTabellaMonete.setImage(monete);
        imageViewPunti.setImage(punti);
        imgTabellaPunti.setImage(punti);
        imgTabellaNCarte.setImage(new Image(getClass().getClassLoader().getResourceAsStream("bonus_cartapolitica.png")));
    }

    private void inizializzaImmaginiBalconiDiLegno(){
        Image img = new Image(getClass().getClassLoader().getResourceAsStream("balcone_legno.png"));
        double altezzaRelativaBalcone = 0.5;
        HBox[] balconi = {balconeCosta, balconeMontagna, balconeRe, balconeCollina};
        Arrays.stream(balconi).forEach((HBox balcone) -> {
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(((ImageView)balcone.getChildren().get(0)).getFitWidth()*balcone.getChildren().size());
            imageView.setFitHeight(((ImageView)balcone.getChildren().get(0)).getFitHeight()*altezzaRelativaBalcone);
            imageView.setX(balcone.getLayoutX());
            double y = balcone.getLayoutY() + (1-altezzaRelativaBalcone) *((ImageView)balcone.getChildren().get(0)).getFitHeight();
            imageView.setY(y);
            anchorInScroll.getChildren().add(imageView);
        });
    }

    public void updateBalcone(String idBalcone, String colore1, String colore2, String colore3, String colore4) {
        boolean isBalconeRe = IdBalcone.valueOf(idBalcone).equals(IdBalcone.RE);
        List<Node> nodi = scegliBalcone(idBalcone).getChildren();
        List<ImageView> imageViewConsiglieri = new ArrayList<>();
        nodi.forEach(nodo -> imageViewConsiglieri.add((ImageView) nodo));
        imageViewConsiglieri.get(0).setImage(scegliImmagineConsigliere(colore1, isBalconeRe));
        imageViewConsiglieri.get(1).setImage(scegliImmagineConsigliere(colore2, isBalconeRe));
        imageViewConsiglieri.get(2).setImage(scegliImmagineConsigliere(colore3, isBalconeRe));
        imageViewConsiglieri.get(3).setImage(scegliImmagineConsigliere(colore4, isBalconeRe));
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
    private Image scegliImmagineConsigliere(String colore, boolean isBalconeRe) {
        String nomeImg = "consigliere_" + colore.toLowerCase();
        if(isBalconeRe) nomeImg += "_re";
        nomeImg += ".png";
        return new Image(getClass().getClassLoader().getResourceAsStream(nomeImg));
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

    //friendly
    void updateMoneteGiocatore(int monete) {
        moneteGiocatore.setText(monete+"");
    }

    //friendly
    void updateMoneteAvversario(int id, int monete){
        idAvversarioLabelMonete.get(id).setText(monete+"");
    }

    //friendly
    void updateAiutantiGiocatore(int aiutanti){
            aiutantiGiocatore.setText(aiutanti+"");
    }

    //friendly
    void updateAiutantiAvversari(int id, int aiutanti) {
        idAvversarioLabelAiutanti.get(id).setText(aiutanti+"");
    }

    //friendly
    void updatePuntiVittoriaGiocatore(int punti) {
        puntiVittoriaGiocatore.setText(punti+"");
    }

    //friendly
    void updatePuntiVittoriaAvversario(int id, int punti) {
        idAvversarioLabelPunti.get(id).setText(punti+"");
    }

    //friendly
    void updateCartePoliticaGiocatore(List<String> carte){
        if (!hBoxPolitica.getChildren().isEmpty()) hBoxPolitica.getChildren().remove(0, hBoxPolitica.getChildren().size());
        ArrayList<ImageView> imgViews = new ArrayList<>();
        carte.forEach(colore ->{
            ImageView img = new ImageView();
            String nomeFile = "politica_"+colore.toLowerCase()+".png";
            img.setImage(new Image(getClass().getClassLoader().getResourceAsStream(nomeFile)));
            imgViews.add(img);
        });
        imgViews.forEach(imageView -> hBoxPolitica.getChildren().add(imageView));
    }

    //friendly
    void updateCartePoliticaAvversari(int id, int numCarte){
        idAvversarioLabelNCarte.get(id).setText(numCarte+"");
    }

    //friendly
    void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori){

    }

    //friendly
    void nuovoMessaggio(String messaggio) {
        areaNotifiche.appendText(messaggio);
        areaNotifiche.appendText("\n");
    }

    //permette di firare eventi al root pane dall'esterno
    //friendly
    Parent getRootPane(){
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
