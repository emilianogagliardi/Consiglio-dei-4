package client.view.GUI;

import classicondivise.IdBalcone;
import classicondivise.Colore;
import client.view.eccezioni.SingletonNonInizializzatoException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ControllerFXPartita extends GestoreFlussoFinestra implements Initializable{
    private int idGiocatore;
    @FXML
    AnchorPane rootPane;
    @FXML
    ImageView immagineMappa, cartaCollinaCoperta, cartaMontagnaCoperta, cartaCostaCoperta;
    @FXML
    ImageView consigliere1_costa, consigliere2_costa, consigliere3_costa, consigliere4_costa;
    @FXML
    ImageView consigliere1_collina, consigliere2_collina, consigliere3_collina, consigliere4_collina;
    @FXML
    ImageView consigliere1_montagna, consigliere2_montagna, consigliere3_montagna, consigliere4_montagna;
    @FXML
    ImageView consigliere1_re, consigliere2_re, consigliere3_re, consigliere4_re;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            idGiocatore = GUIView.getInstance().getIdGiocatore();
            GUIView.getInstance().setControllerFXPartita(this);
            inizializzaImmagine();
        } catch (RemoteException | SingletonNonInizializzatoException e) {
            e.printStackTrace();
        }
        inizializzaImmaginiCarte();
    }

    private void inizializzaImmagine() throws SingletonNonInizializzatoException {
        //inizializza
        int idMappa = GUIView.getInstance().getIdMappa();
        String nomeFile = "mappa"+idMappa+"_gioco.jpg";
        Image immagine = new Image(getClass().getClassLoader().getResourceAsStream(nomeFile));
        immagineMappa.setImage(immagine);
    }

    private void inizializzaImmaginiCarte() {
        cartaCollinaCoperta.setImage(new Image(getClass().getClassLoader().getResourceAsStream("retro_carta_collina.jpg")));
        cartaCostaCoperta.setImage(new Image(getClass().getClassLoader().getResourceAsStream("retro_carta_costa.jpg")));
        cartaMontagnaCoperta.setImage(new Image(getClass().getClassLoader().getResourceAsStream("retro_carta_montagna.jpg")));
    }

    public void updateBalcone(String idBalcone, String colore1, String colore2, String colore3, String colore4) {
        ImageView[] balcone;
        switch (IdBalcone.valueOf(idBalcone)){
            case COLLINA:
                balcone = new ImageView[]{consigliere1_collina, consigliere2_collina, consigliere3_collina, consigliere4_collina};
                break;
            case COSTA:
                balcone = new ImageView[]{consigliere1_costa, consigliere2_costa, consigliere3_costa, consigliere4_costa};
                break;
            case MONTAGNA:
                balcone = new ImageView[]{consigliere1_costa, consigliere2_costa, consigliere3_montagna, consigliere4_montagna};
                break;
            case RE:
                balcone = new ImageView[]{consigliere1_re, consigliere2_re, consigliere3_re, consigliere4_re};
                break;
            default:
                throw new IllegalArgumentException();
        }
        Colore[] colori = {Colore.valueOf(colore1), Colore.valueOf(colore2), Colore.valueOf(colore3), Colore.valueOf(colore4)};
        for (int i = 0; i < balcone.length; i++) {
            Image image;
            switch(colori[i]){
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
            balcone[i].setImage(image);
        }
    }
}
