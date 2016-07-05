package client.view.GUI;

import classicondivise.Vendibile;
import classicondivise.carte.CartaPermessoCostruzione;
import client.view.GUI.utility.UtilityGUI;
import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by emilianogagliardi on 28/06/16.
 */
public class ControllerFXVetrina extends GestoreFlussoFinestra implements Initializable {
    InterfacciaController controller;
    @FXML
    private Button btnConferma;
    @FXML
    private ToggleButton acquistaPermit1, acquistaPermit2, acquistaPermit3, acquistaPolitica1, acquistaPolitica2, acquistaPolitica3, acquistaAiutanti1, acquistaAiutanti2, acquistaAiutanti3;
    @FXML
    private Label prezzoPermit1, prezzoPermit2, prezzoPermit3, prezzoPolitica1, prezzoPolitica2, prezzoPolitica3, prezzoAiutanti1, prezzoAiutanti2, prezzoAiutanti3, aiutanti1, aiutanti2, aiutanti3;
    @FXML
    private HBox permit1, permit2, permit3, politica1, politica2, politica3;
    @FXML
    private ImageView imgAiutante1, imgAiutante2, imgAiutante3;

    private ArrayList<Integer> idAvversari;
    private HashMap<Integer, HBox> permitIdGiocatore = new HashMap<>();
    private HashMap<Integer, Label> prezzoPermitIdGiocatore = new HashMap<>();
    private HashMap<Integer, HBox> politicaIdGiocatore = new HashMap<>();
    private HashMap<Integer, Label> prezzoPoliticaIdGiocatore = new HashMap<>();
    private HashMap<Integer, Label> aiutantiIdGiocatore = new HashMap<>();
    private HashMap<Integer, Label> prezzoAiutantiIdGiocatore = new HashMap<>();
    private HashMap<Integer, Vendibile> idGiocatoreVenduti = new HashMap<>();
    private HashMap<Integer, ToggleButton> btnAcquistaPermitIdGiocatore = new HashMap<>();
    private HashMap<Integer, ToggleButton> btnAcquistaPoliticaIdGiocatore = new HashMap<>();
    private HashMap<Integer, ToggleButton> btnAcquistaAiutantiIdGiocatore = new HashMap<>();
    private HashMap<Integer, Vendibile> permitVenduteIdGiocatore = new HashMap<>();
    private HashMap<Integer, Vendibile> politicaVenduteIdGiocatore = new HashMap<>();
    private HashMap<Integer, Vendibile> aiutantiVendutiIdGiocatore = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inizializzaHashMap();
        setImmaginiAiutanti();
        setAzioneBtnConferma();
    }

    private void inizializzaHashMap(){
        idAvversari = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            try {
                if (i != GUIView.getInstance().getIdGiocatore()) {
                    idAvversari.add(i);
                }
            }catch (RemoteException | SingletonNonInizializzatoException e) {
                e.printStackTrace();
            }
        }
        permitIdGiocatore.put(idAvversari.get(0), permit1);
        permitIdGiocatore.put(idAvversari.get(1), permit2);
        permitIdGiocatore.put(idAvversari.get(2), permit3);
        prezzoPermitIdGiocatore.put(idAvversari.get(0), prezzoPermit1);
        prezzoPermitIdGiocatore.put(idAvversari.get(1), prezzoPermit2);
        prezzoPermitIdGiocatore.put(idAvversari.get(2), prezzoPermit3);
        politicaIdGiocatore.put(idAvversari.get(0), politica1);
        politicaIdGiocatore.put(idAvversari.get(1), politica2);
        politicaIdGiocatore.put(idAvversari.get(2), politica3);
        prezzoPoliticaIdGiocatore.put(idAvversari.get(0), prezzoPolitica1);
        prezzoPoliticaIdGiocatore.put(idAvversari.get(1), prezzoPolitica2);
        prezzoPoliticaIdGiocatore.put(idAvversari.get(2), prezzoPolitica3);
        aiutantiIdGiocatore.put(idAvversari.get(0), aiutanti1);
        aiutantiIdGiocatore.put(idAvversari.get(1), aiutanti2);
        aiutantiIdGiocatore.put(idAvversari.get(2), aiutanti3);
        prezzoAiutantiIdGiocatore.put(idAvversari.get(0), prezzoAiutanti1);
        prezzoAiutantiIdGiocatore.put(idAvversari.get(1), prezzoAiutanti2);
        prezzoAiutantiIdGiocatore.put(idAvversari.get(2), prezzoAiutanti3);
        btnAcquistaAiutantiIdGiocatore.put(idAvversari.get(0), acquistaAiutanti1);
        btnAcquistaAiutantiIdGiocatore.put(idAvversari.get(1), acquistaAiutanti2);
        btnAcquistaAiutantiIdGiocatore.put(idAvversari.get(2), acquistaAiutanti3);
        btnAcquistaPermitIdGiocatore.put(idAvversari.get(0), acquistaPermit1);
        btnAcquistaPermitIdGiocatore.put(idAvversari.get(1), acquistaPermit2);
        btnAcquistaPermitIdGiocatore.put(idAvversari.get(2), acquistaPermit3);
        btnAcquistaPoliticaIdGiocatore.put(idAvversari.get(0), acquistaPolitica1);
        btnAcquistaPoliticaIdGiocatore.put(idAvversari.get(1), acquistaPolitica2);
        btnAcquistaPoliticaIdGiocatore.put(idAvversari.get(2), acquistaPolitica3);
    }

    private void setImmaginiAiutanti(){
        Image img = new Image(getClass().getClassLoader().getResourceAsStream("bonus_aiutante.png"));
        imgAiutante1.setImage(img);
        imgAiutante2.setImage(img);
        imgAiutante3.setImage(img);
    }

    public void setVetrina(List<Vendibile> inVendita){
        aiutanti1.setText("0");
        aiutanti2.setText("0");
        aiutanti3.setText("0");
        try {
            int id = GUIView.getInstance().getIdGiocatore();
            inVendita.stream().filter((Vendibile oggettoInVendita) -> oggettoInVendita.getIdGiocatore() != id).forEach((Vendibile oggettoInVendita) ->{
                //TODO togliere questa riga
                System.out.println("ricevuto oggetto vendibile da giocatore con id "+oggettoInVendita.getIdGiocatore());
                UtilityGUI utilityGUI = new UtilityGUI();
                switch (oggettoInVendita.getIdVendibile()){
                    case CARTE_PERMESSO_COSTRUZIONE:
                        List<CartaPermessoCostruzione>  cartePermesso = (List<CartaPermessoCostruzione>) oggettoInVendita.getOggetto();
                        cartePermesso.forEach((cartaPermesso) ->{
                            StackPane stackPane = new StackPane();
                            utilityGUI.creaPermit(cartaPermesso, 80, 66, stackPane, false);
                            HBox boxPermit = permitIdGiocatore.get(oggettoInVendita.getIdGiocatore());
                            boxPermit.getChildren().add(stackPane);
                        });
                        prezzoPermitIdGiocatore.get(oggettoInVendita.getIdGiocatore()).setText(String.format("Prezzo: %d", oggettoInVendita.getPrezzo()));
                        permitVenduteIdGiocatore.put(oggettoInVendita.getIdGiocatore(), oggettoInVendita);
                        btnAcquistaPermitIdGiocatore.get(oggettoInVendita.getIdGiocatore()).setDisable(false);
                        break;
                    case CARTE_POLITICA:
                        List<String> cartaPolitica = (List<String>) oggettoInVendita.getOggetto();
                        HBox boxPolitica = politicaIdGiocatore.get(oggettoInVendita.getIdGiocatore());
                        utilityGUI.addPoliticaInHBox(boxPolitica, cartaPolitica);
                        prezzoPoliticaIdGiocatore.get(oggettoInVendita.getIdGiocatore()).setText(String.format("Prezzo: %d", oggettoInVendita.getPrezzo()));
                        politicaVenduteIdGiocatore.put(oggettoInVendita.getIdGiocatore(), oggettoInVendita);
                        btnAcquistaPoliticaIdGiocatore.get(oggettoInVendita.getIdGiocatore()).setDisable(false);
                        break;
                    case AIUTANTI:
                        Label aiutanti = aiutantiIdGiocatore.get(oggettoInVendita.getIdGiocatore());
                        aiutanti.setText(String.format("%d", (Integer) oggettoInVendita.getOggetto()));
                        prezzoAiutantiIdGiocatore.get(oggettoInVendita.getIdGiocatore()).setText(String.format("Prezzo: %d", oggettoInVendita.getPrezzo()));
                        aiutantiVendutiIdGiocatore.put(oggettoInVendita.getIdGiocatore(), oggettoInVendita);
                        btnAcquistaAiutantiIdGiocatore.get(oggettoInVendita.getIdGiocatore()).setDisable(false);
                        break;
                    default: throw new IllegalArgumentException();
                }
            });
        } catch (RemoteException | SingletonNonInizializzatoException e) {
            e.printStackTrace();
        }
    }

    private void setAzioneBtnConferma(){
        btnConferma.setOnMouseClicked(event -> {
            ArrayList<Vendibile> daComprare = new ArrayList<Vendibile>();
            if (acquistaAiutanti1.isSelected()){
                daComprare.add(aiutantiVendutiIdGiocatore.get(idAvversari.get(0)));
            }
            if (acquistaAiutanti2.isSelected()){
                daComprare.add(aiutantiVendutiIdGiocatore.get(idAvversari.get(1)));
            }
            if(acquistaAiutanti3.isSelected()){
                daComprare.add(aiutantiVendutiIdGiocatore.get(idAvversari.get(2)));
            }
            if(acquistaPermit1.isSelected()){
                daComprare.add(permitVenduteIdGiocatore.get(idAvversari.get(0)));
            }
            if (acquistaPermit2.isSelected()){
                daComprare.add(permitVenduteIdGiocatore.get(idAvversari.get(1)));
            }
            if (acquistaPermit3.isSelected()){
                daComprare.add(permitVenduteIdGiocatore.get(idAvversari.get(2)));
            }
            if(acquistaPolitica1.isSelected()){
                daComprare.add(politicaVenduteIdGiocatore.get(idAvversari.get(0)));
            }
            if (acquistaPolitica2.isSelected()){
                daComprare.add(politicaVenduteIdGiocatore.get(idAvversari.get(1)));
            }
            if (acquistaPolitica3.isSelected()){
                daComprare.add(politicaVenduteIdGiocatore.get(idAvversari.get(2)));
            }
            try {
                controller.compra(daComprare);
                controller.passaTurno();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            super.getApplication().chiudiFinestraSecondaria();
        });
    }

    public void setController(InterfacciaController controller){this.controller = controller;}
}
