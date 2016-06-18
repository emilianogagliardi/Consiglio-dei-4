package client.view.GUI;

import client.view.eccezioni.SingletonNonInizializzatoException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ControllerFXPartita extends GestoreFlussoFinestra implements Initializable{
    private int idGiocatore;

    @FXML
    ImageView immagineMappa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            idGiocatore = GUIView.getInstance().getIdGiocatore();
            inizializzaImmagine();
        } catch (RemoteException | SingletonNonInizializzatoException e) {
            e.printStackTrace();
        }
    }

    private void inizializzaImmagine() throws SingletonNonInizializzatoException {
        int idMappa = GUIView.getInstance().getIdMappa();
        String nomeFile = "mappa"+idMappa+"_gioco.jpg";
        Image immagine = new Image(getClass().getClassLoader().getResourceAsStream(nomeFile));
        immagineMappa.setImage(immagine);
        System.out.println(immagineMappa.getFitWidth()+"x"+immagineMappa.getFitHeight());
    }
}
