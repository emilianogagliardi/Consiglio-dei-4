package client.view.GUI;

import classicondivise.Colore;
import classicondivise.IdBalcone;
import classicondivise.bonus.Bonus;
import classicondivise.carte.CartaPermessoCostruzione;
import client.view.GUI.customevent.ShowViewGiocoEvent;
import client.view.GUI.utility.RenderPercorsoNobilta;
import client.view.GUI.utility.UtilityGUI;
import client.view.GiocatoreView;
import client.view.RiservaPartitaView;
import client.view.eccezioni.SingletonNonInizializzatoException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import java.lang.reflect.Field;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class ControllerFXPartita extends GestoreFlussoFinestra implements Initializable{
    @FXML
    private AnchorPane rootPane, anchorInScroll;
    @FXML
    private HBox hBoxPolitica, hBoxPermit;
    @FXML
    private ImageView immagineMappa, cartaCollinaCoperta, cartaMontagnaCoperta, cartaCostaCoperta;
    @FXML
    private StackPane cartaCosta1, cartaCosta2, cartaCollina1, cartaCollina2, cartaMontagna1, cartaMontagna2;
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
    private ImageView imageViewMonete, imageViewAiutanti, imageViewPunti, imageViewEmpori, imgTabellaPunti, imgTabellaMonete, imgTabellaAiutanti, imgTabellaNCarte, imgTabellaEmpori;
    @FXML
    private Label moneteGiocatore, puntiVittoriaGiocatore, aiutantiGiocatore, emporiGiocatore;
    @FXML
    private Label labelId1Giocatore, labelId2Giocatore, labelId3Giocatore, nomeGiocatore;
    @FXML
    private Label puntiAvversario1, puntiAvversario2, puntiAvversario3;
    @FXML
    private Label moneteAvversario1, moneteAvversario2, moneteAvversario3;
    @FXML
    private Label aiutantiAvversario1, aiutantiAvversario2, aiutantiAvversario3;
    @FXML
    private Label numCarteAvversario1, numCarteAvversario2, numCarteAvversario3;
    @FXML
    private Label numEmporiAvversario1, numEmporiAvversario2, numEmporiAvversario3;
    @FXML
    private HBox cartePermitAvversario1, cartePermitAvversario2, cartePermitAvversario3;
    @FXML
    private ImageView consigliereArancioneGioco, consigliereAzzurroGioco, consigliereBiancoGioco, consigliereNeroGioco, consigliereRosaGioco, consigliereViolaGioco, aiutanteGioco;
    @FXML
    private Label numeroConsiglieriArancioneGioco, numeroConsiglieriAzzurroGioco, numeroConsiglieriBiancoGioco, numeroConsiglieriNeroGioco, numeroConsiglieriRosaGioco, numeroConsiglieriViolaGioco, numeroAiutantiPartita;
    @FXML
    private ImageView percorsoNobilta1, percorsoNobilta2;
    @FXML
    private ImageView aRe, bRe, cRe, dRe, eRe, fRe, gRe, hRe, iRe, jRe, kRe, lRe, mRe, nRe, oRe;
    @FXML
    private VBox pedinePos0, pedinePos1, pedinePos2, pedinePos3, pedinePos4, pedinePos5, pedinePos6, pedinePos7, pedinePos8, pedinePos9, pedinePos10, pedinePos11, pedinePos12, pedinePos13, pedinePos14, pedinePos15, pedinePos16, pedinePos17, pedinePos18, pedinePos19, pedinePos20;
    @FXML
    private HBox bonusNobilta0, bonusNobilta1, bonusNobilta2, bonusNobilta3, bonusNobilta4, bonusNobilta5, bonusNobilta6, bonusNobilta7, bonusNobilta8, bonusNobilta9, bonusNobilta10, bonusNobilta11, bonusNobilta12, bonusNobilta13, bonusNobilta14, bonusNobilta15, bonusNobilta16, bonusNobilta17, bonusNobilta18, bonusNobilta19, bonusNobilta20;
    @FXML
    private Button bottoneAiuto;
    @FXML
    private ImageView posizioneRe;
    //utility
    private HashMap<Integer, Label> idAvversarioLabelMonete = new HashMap<>();
    private HashMap<Integer, Label> idAvversarioLabelAiutanti = new HashMap<>();
    private HashMap<Integer, Label> idAvversarioLabelPunti = new HashMap<>();
    private HashMap<Integer, Label> idAvversarioLabelNCarte = new HashMap<>();
    private HashMap<Integer, Label> idAvversarioLabelNEmpori = new HashMap<>();
    private HashMap<Integer, HBox> idAvversarioHBoxhCartePermit = new HashMap<>();
    private RenderPercorsoNobilta renderNobilita;
    //identifica id e colore giocatore
    private HashMap<Integer, ColoreGiocatore> idGiocatoreColore;
    //popover per guidare il giocatore
    private PopOver popEleggiConsigliere, popCostruisciEmporio, popCostruisciEmporioRe, popAcquistaPermesso, popCambiareTessere, popIngaggiareAiutante;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //assegna l'handler di mostra mappa all'evento di start del gioco
        inizializzaImmagineMappa();
        //inizializza le immagini di retro delle carte permit
        inizializzaImmaginiCarte();
        //il giocatore non può scrivere in area notifiche
        areaNotifiche.setEditable(false);
        inizializzaImmaginiRisorseGiocatori();
        inizializzaImmaginiConsieglieriGioco();
        inizializzaImmaginiBalconiDiLegno();
        inizializzaPercorsoNobiltà();
        inizializzaRenderNobilta();
        inizializzaHashMapNomi();
        inizializzaIdGiocatoreColore();
        inizializzaBottoneAiuto();
    }

    private void inizializzaRenderNobilta(){
        List<VBox> pedineNobilta = new ArrayList<>();
        for (int i = 0; i <= 20; i++){
            try {
                Field fieldVBox = getClass().getDeclaredField("pedinePos"+i);
                VBox vBox = (VBox) fieldVBox.get(this);
                pedineNobilta.add(vBox);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        renderNobilita = new RenderPercorsoNobilta(pedineNobilta);
    }

    private void inizializzaBottoneAiuto(){
        inizializzaPopOver();
        bottoneAiuto.setOnMouseClicked(event -> {
            if (popAcquistaPermesso.isShowing()) nascondiPopOver();
            else mostraPopOver();
        });
    }

    private void inizializzaIdGiocatoreColore(){
        idGiocatoreColore = new HashMap<>();
        idGiocatoreColore.put(0, ColoreGiocatore.ROSSO);
        idGiocatoreColore.put(1, ColoreGiocatore.BLU);
        idGiocatoreColore.put(2, ColoreGiocatore.GIALLO);
        idGiocatoreColore.put(3, ColoreGiocatore.VERDE);
    }

    private void inizializzaHashMapNomi(){
        //inizializza
        rootPane.addEventHandler(ShowViewGiocoEvent.SHOW_IMAGE, event -> {
            try {
                int myId = GUIView.getInstance().getIdGiocatore();
                Integer[] tuttiGliIdArray = {0, 1, 2, 3};
                ArrayList<Integer> tuttiGliId = new ArrayList<>(3);
                nomeGiocatore.setText("Giocatore"+myId);
                nomeGiocatore.setTextFill(UtilityGUI.convertiColore(idGiocatoreColore.get(myId)));
                Arrays.stream(tuttiGliIdArray).filter((Integer id) -> id != myId).forEach(id -> tuttiGliId.add(id));
                labelId1Giocatore.setText("Giocatore" + tuttiGliId.get(0));
                labelId1Giocatore.setTextFill(UtilityGUI.convertiColore(idGiocatoreColore.get(tuttiGliId.get(0))));
                labelId2Giocatore.setText("Giocatore" + tuttiGliId.get(1));
                labelId2Giocatore.setTextFill(UtilityGUI.convertiColore(idGiocatoreColore.get(tuttiGliId.get(1))));
                labelId3Giocatore.setText("Giocatore" + tuttiGliId.get(2));
                labelId3Giocatore.setTextFill(UtilityGUI.convertiColore(idGiocatoreColore.get(tuttiGliId.get(2))));
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
                idAvversarioLabelNEmpori.put(tuttiGliId.get(0),numEmporiAvversario1);
                idAvversarioLabelNEmpori.put(tuttiGliId.get(1),numEmporiAvversario2);
                idAvversarioLabelNEmpori.put(tuttiGliId.get(2),numEmporiAvversario3);
                idAvversarioHBoxhCartePermit.put(tuttiGliId.get(0), cartePermitAvversario1);
                idAvversarioHBoxhCartePermit.put(tuttiGliId.get(1), cartePermitAvversario2);
                idAvversarioHBoxhCartePermit.put(tuttiGliId.get(2), cartePermitAvversario3);
            } catch (SingletonNonInizializzatoException | RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void inizializzaPopOver(){
        popAcquistaPermesso = new PopOver(new Label("Clicca su una carta permesso per\ncominciare la fase di acquisto"));
        popCostruisciEmporioRe = new PopOver(new Label("Clicca sul re per costruire \nun emporio tramite l'aiuto del re"));
        popCostruisciEmporio = new PopOver(new Label("Clicca per utilizzare una carta\npermesso e costruire un emporio"));
        popEleggiConsigliere = new PopOver(new Label("Clicca per eleggere un consigliere\nguadgnando 4 monete o mandare un\n aiutante ad eleggere un consigliere"));
        popCambiareTessere = new PopOver(new Label("Clicca su un mazzo per \ncambiare le carte permesso"));
        popIngaggiareAiutante = new PopOver(new Label("Clicca per ingaggiare un aiutante\no usare tre aiutanti per compiere\nun'azione principale aggiuntiva"));
        popAcquistaPermesso.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
        popEleggiConsigliere.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
        popCambiareTessere.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
        popIngaggiareAiutante.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
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

    private void inizializzaPercorsoNobiltà() {
        percorsoNobilta1.setImage(new Image(getClass().getClassLoader().getResourceAsStream("percorso_nobilta1.png")));
        percorsoNobilta2.setImage(new Image(getClass().getClassLoader().getResourceAsStream("percorso_nobilta2.png")));
    }

    private void inizializzaImmaginiRisorseGiocatori() {
        Image monete = new Image(getClass().getClassLoader().getResourceAsStream("bonus_monete.png"));
        Image aiutanti = new Image(getClass().getClassLoader().getResourceAsStream("bonus_aiutante.png"));
        Image punti = new Image(getClass().getClassLoader().getResourceAsStream("bonus_puntivittoria.png"));
        Image empori = new Image(getClass().getClassLoader().getResourceAsStream("icona_emporio.png"));
        imageViewAiutanti.setImage(aiutanti);
        imgTabellaAiutanti.setImage(aiutanti);
        imageViewMonete.setImage(monete);
        imgTabellaMonete.setImage(monete);
        imageViewPunti.setImage(punti);
        imgTabellaPunti.setImage(punti);
        imgTabellaEmpori.setImage(empori);
        imageViewEmpori.setImage(empori);
        imgTabellaNCarte.setImage(new Image(getClass().getClassLoader().getResourceAsStream("bonus_cartapolitica.png")));
    }

    private void inizializzaImmaginiConsieglieriGioco(){
        consigliereArancioneGioco.setImage(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_arancione.png")));
        consigliereAzzurroGioco.setImage(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_azzurro.png")));
        consigliereBiancoGioco.setImage(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_bianco.png")));
        consigliereNeroGioco.setImage(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_nero.png")));
        consigliereRosaGioco.setImage(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_rosa.png")));
        consigliereViolaGioco.setImage(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_viola.png")));
        aiutanteGioco.setImage(new Image(getClass().getClassLoader().getResourceAsStream("bonus_aiutante.png")));
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

    void updateBalcone(String idBalcone, String colore1, String colore2, String colore3, String colore4) {
        Platform.runLater(() -> {
            boolean isBalconeRe = IdBalcone.valueOf(idBalcone).equals(IdBalcone.RE);
            List<Node> nodi = scegliBalcone(idBalcone).getChildren();
            List<ImageView> imageViewConsiglieri = new ArrayList<>();
            nodi.forEach(nodo -> imageViewConsiglieri.add((ImageView) nodo));
            imageViewConsiglieri.get(0).setImage(scegliImmagineConsigliere(colore1, isBalconeRe));
            imageViewConsiglieri.get(1).setImage(scegliImmagineConsigliere(colore2, isBalconeRe));
            imageViewConsiglieri.get(2).setImage(scegliImmagineConsigliere(colore3, isBalconeRe));
            imageViewConsiglieri.get(3).setImage(scegliImmagineConsigliere(colore4, isBalconeRe));
        });
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


    public void updateBonusCittà(String nomeCittà, Bonus bonus){
        Platform.runLater(() -> {
            HBox hBoxBonus;
            Class c = this.getClass();
            try {
                //ottiene l'attributo "bonusNomecittà" di this.getClass, per evitare uno switch case di 15 case.
                Field fieldHBox = c.getDeclaredField("bonus" + nomeCittà.substring(0, 1).toUpperCase() + nomeCittà.substring(1).toLowerCase());
                hBoxBonus = (HBox) fieldHBox.get(this);
                UtilityGUI utility = new UtilityGUI();
                utility.addImmaginiBonus(hBoxBonus, 30, 30, 15, bonus);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    //friendly
    void updateMoneteGiocatore(int monete) {
        Platform.runLater(() ->moneteGiocatore.setText(monete+""));
    }

    //friendly
    void updateMoneteAvversario(int id, int monete){
        Platform.runLater(() ->idAvversarioLabelMonete.get(id).setText(monete+""));
    }

    //friendly
    void updateAiutantiGiocatore(int aiutanti){
        Platform.runLater(() -> {
            try {
                aiutantiGiocatore.setText(aiutanti + "");
            } catch (Exception e) {
                System.out.println("lanciata un eccezione in uupdate miei aiutanti");
            }
            //memorizza informazioni
            GiocatoreView.getInstance().setAiutanti(aiutanti);
        });
    }

    //friendly
    void updateAiutantiAvversari(int id, int aiutanti) {
        Platform.runLater(() -> idAvversarioLabelAiutanti.get(id).setText(aiutanti+""));
    }

    //friendly
    void updatePuntiVittoriaGiocatore(int punti) {
        Platform.runLater(() -> puntiVittoriaGiocatore.setText(punti+""));
    }

    //friendly
    void updatePuntiVittoriaAvversario(int id, int punti) {
        Platform.runLater(() -> idAvversarioLabelPunti.get(id).setText(punti+""));
    }

    //friendly
    void updateCartePoliticaGiocatore(List<String> carte){
        Platform.runLater(() -> {
            //memorizza le informazioni sulle carte
            GiocatoreView.getInstance().setCartePolitica(carte);
            if (!hBoxPolitica.getChildren().isEmpty())
                hBoxPolitica.getChildren().remove(0, hBoxPolitica.getChildren().size());
            UtilityGUI utilityGUI = new UtilityGUI();
            utilityGUI.addPoliticaInHBox(hBoxPolitica, carte);
        });
    }

    void updateEmporiGiocatore(int numEmpori) {
        Platform.runLater(() ->emporiGiocatore.setText(numEmpori+""));
    }

    void updateEmporiAvversario(int id, int numEmpori) {
        Platform.runLater(() -> idAvversarioLabelNEmpori.get(id).setText(numEmpori+""));
    }

    //friendly
    void updateCartePoliticaAvversari(int id, int numCarte){
        Platform.runLater(() -> idAvversarioLabelNCarte.get(id).setText(numCarte+""));
    }

    //friendly
    void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori){
        Platform.runLater(() -> {
            try {
                Field fieldHBox = getClass().getDeclaredField("empori" + nomeCittà.substring(0, 1).toUpperCase() + nomeCittà.substring(1));
                HBox box = (HBox) fieldHBox.get(this);
                idGiocatori.forEach((Integer id) -> {
                    ImageView imgView = (ImageView) box.getChildren().get(id);
                    ColoreGiocatore colore = idGiocatoreColore.get(id);
                    String nomeFile = "emporio_" + colore.toString().toLowerCase() + ".png";
                    imgView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(nomeFile)));
                });
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    //friendly
    void updateCartePermessoRegione(String regione, CartaPermessoCostruzione carta1, CartaPermessoCostruzione carta2) {
        Platform.runLater(() -> {
            //get degli attributi di questa classe cartaRegione1 e cartaRegione2
            try {
                Field fieldPane1 = getClass().getDeclaredField("carta" + regione.substring(0, 1).toUpperCase() + regione.substring(1).toLowerCase() + 1);
                StackPane pane1 = (StackPane) fieldPane1.get(this);
                Field fieldPane2 = getClass().getDeclaredField("carta" + regione.substring(0, 1).toUpperCase() + regione.substring(1).toLowerCase() + 2);
                StackPane pane2 = (StackPane) fieldPane2.get(this);
                UtilityGUI utilityGUI = new UtilityGUI();
                utilityGUI.creaPermit(carta1, 90, 75, pane1, true);
                utilityGUI.creaPermit(carta2, 90, 75, pane2, true);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    //friendly
    void updateCartePermessoGiocatore(int id, List<CartaPermessoCostruzione> carte){
        Platform.runLater(() -> {
            HBox hBox;
            double h;
            double w;
            UtilityGUI utility = new UtilityGUI();
            try {
                if (id == GUIView.getInstance().getIdGiocatore()) {
                    hBox = hBoxPermit;
                    hBox.getChildren().clear();
                    h = 90;
                    w = 75;
                    //memorizza informazione sulle carte
                    GiocatoreView.getInstance().setCartePermesso(carte);
                    carte.forEach(carta -> {
                        StackPane stackPane = new StackPane();
                        utility.creaPermit(carta, h, w, stackPane, false);
                        hBox.getChildren().add(stackPane);
                    });
                } else{
                    hBox = idAvversarioHBoxhCartePermit.get(id);
                    hBox.getChildren().clear();
                    h = 60;
                    w = 50;
                    carte.forEach(carta -> {
                        StackPane stackPane = new StackPane();
                        utility.creaPermit(carta, h, w, stackPane, false);
                        hBox.getChildren().add(stackPane);
                    });
                }
            } catch (SingletonNonInizializzatoException | RemoteException e) {
                e.printStackTrace();
            }
        });
    }


    //friendly
    void updateConsiglieriGioco(List<String> colori){
        Platform.runLater(() -> {
            RiservaPartitaView.getInstance().setConsiglieri(colori);
            HashMap<Colore, Label> labelColoreNumeroConsiglieri = new HashMap<>();
            labelColoreNumeroConsiglieri.put(Colore.ARANCIONE, numeroConsiglieriArancioneGioco);
            labelColoreNumeroConsiglieri.put(Colore.AZZURRO, numeroConsiglieriAzzurroGioco);
            labelColoreNumeroConsiglieri.put(Colore.BIANCO, numeroConsiglieriBiancoGioco);
            labelColoreNumeroConsiglieri.put(Colore.NERO, numeroConsiglieriNeroGioco);
            labelColoreNumeroConsiglieri.put(Colore.ROSA, numeroConsiglieriRosaGioco);
            labelColoreNumeroConsiglieri.put(Colore.VIOLA, numeroConsiglieriViolaGioco);
            labelColoreNumeroConsiglieri.forEach((colore, label) -> label.setText("0"));
            colori.stream().forEach((String colore) -> labelColoreNumeroConsiglieri.get(Colore.valueOf(colore)).setText((Integer.parseInt(labelColoreNumeroConsiglieri.get(Colore.valueOf(colore)).getText()) + 1) + ""));
        });
    }

    void updateAiutantiGioco(int aiutanti) {
        Platform.runLater(() -> {
            //memorizza il numero di aiutanti nel pool della partita
            RiservaPartitaView.getInstance().setAiutanti(aiutanti);
            numeroAiutantiPartita.setText(aiutanti + "");
        });
    }

    //friendly
    void updateBonusNobilta(List<Bonus> bonus) {
        Platform.runLater(() -> {
            UtilityGUI utilityGUI = new UtilityGUI();
            int i = 0;
            for (Bonus b : bonus) {
                try {
                    Field fieldHBox = getClass().getDeclaredField("bonusNobilta" + i);
                    HBox boxBonus = (HBox) fieldHBox.get(this);
                    utilityGUI.addImmaginiBonus(boxBonus, 30, 30, 15, b);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                i++;
            }
        });
    }

    //friendly
    void updatePosizionePercorsoNobilta(int id, int pos){
        Platform.runLater(() -> renderNobilita.update(idGiocatoreColore.get(id), pos));
    }

    //friendly
    void updateRe(String citta) {
        Platform.runLater(() -> {
            try {
                Field fieldImg = getClass().getDeclaredField(citta.substring(0, 1).toLowerCase() + "Re");
                ImageView img = (ImageView) fieldImg.get(this);
                img.setImage(new Image(getClass().getClassLoader().getResourceAsStream("re.png")));
                img.setFitWidth(80);
                posizioneRe = img;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    //friendly
    void nuovoMessaggio(String messaggio) {
        Platform.runLater(()-> {
            areaNotifiche.appendText(messaggio);
            areaNotifiche.appendText("\n");
        });
    }

    //friendly
    void eseguiTurno(){
        //per elezione di consiglieri
        balconeCollina.setOnMouseClicked(event -> Platform.runLater(() -> super.getApplication().showMossaEleggiConsigliere(IdBalcone.COLLINA)));
        balconeMontagna.setOnMouseClicked(event -> Platform.runLater(() -> super.getApplication().showMossaEleggiConsigliere(IdBalcone.MONTAGNA)));
        balconeCosta.setOnMouseClicked(event -> Platform.runLater(() -> super.getApplication().showMossaEleggiConsigliere(IdBalcone.COSTA)));
        balconeRe.setOnMouseClicked(event -> Platform.runLater(() -> super.getApplication().showMossaEleggiConsigliere(IdBalcone.RE)));
        //per acquisto carte permesso
        cartaCostaCoperta.setOnMouseClicked(event -> Platform.runLater(() -> super.getApplication().showMossaAcquistaPermesso(IdBalcone.COSTA)));
        cartaCollinaCoperta.setOnMouseClicked(event -> Platform.runLater(() -> super.getApplication().showMossaAcquistaPermesso(IdBalcone.COLLINA)));
        cartaMontagnaCoperta.setOnMouseClicked(event -> Platform.runLater(() -> super.getApplication().showMossaAcquistaPermesso(IdBalcone.MONTAGNA)));
    }

    void fineTurno(){
        Platform.runLater(() -> super.getApplication().chiudiFinestraSecondaria());
    }

    private void mostraPopOver(){
        Platform.runLater(() -> {
            popAcquistaPermesso.show(cartaCollina2);
            popCostruisciEmporioRe.show(posizioneRe);
            popCostruisciEmporio.show(hBoxPermit);
            popEleggiConsigliere.show(balconeCosta);
            popCambiareTessere.show(cartaCostaCoperta);
            popIngaggiareAiutante.show(imageViewAiutanti);
        });
    }

    private void nascondiPopOver(){
        Platform.runLater(() -> {
            popAcquistaPermesso.hide();
            popCostruisciEmporioRe.hide();
            popCostruisciEmporio.hide();
            popEleggiConsigliere.hide();
            popCambiareTessere.hide();
            popIngaggiareAiutante.hide();
        });
    }

    //permette di firare eventi al root pane dall'esterno
    //friendly
    Parent getRootPane(){
        return rootPane;
    }
}
