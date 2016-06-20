package client.view.GUI;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            idGiocatore = GUIView.getInstance().getIdGiocatore();
            inizializzaImmagine();
        } catch (RemoteException | SingletonNonInizializzatoException e) {
            e.printStackTrace();
        }
        inizializzaImmaginiCarte();
    }

    private void inizializzaImmagine() throws SingletonNonInizializzatoException {
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
}
