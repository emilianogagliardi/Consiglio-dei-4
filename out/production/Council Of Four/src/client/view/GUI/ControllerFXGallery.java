package client.view.GUI;

import client.ComunicazioneSceltaMappa;
import client.ComunicazioneSceltaMappaRMI;
import client.ComunicazioneSceltaMappaSocket;
import client.strutturedati.ListaCircolare;
import client.view.eccezioni.SingletonNonInizializzatoException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;


public class ControllerFXGallery extends GestoreFlussoFinestra implements Initializable{
    private ComunicazioneSceltaMappa setterMappa;
    private ListaCircolare<Image> immagini;
    @FXML
    private Button bottoneSinistra, bottoneDestra;
    @FXML
    private Button bottoneConferma;
    @FXML
    private ImageView vistaMappa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inizializzaListaCircolareImmagini();
        vistaMappa.setImage(immagini.getCorrente());
        setImmaginiBottoni();
        setAzioniBottoniScorrimento();
        setAzioneBottoneConferma();
    }

    private void setImmaginiBottoni() {
        Image destra = new Image(getClass().getClassLoader().getResourceAsStream("freccia_destra.png"), 20.0, 20.0, true, true);
        Image sinstra = new Image(getClass().getClassLoader().getResourceAsStream("freccia_sinistra.png"), 20.0, 20.0, true, true);
        bottoneDestra.setGraphic(new ImageView(destra));
        bottoneSinistra.setGraphic(new ImageView(sinstra));
    }

    private void inizializzaListaCircolareImmagini(){
        immagini = new ListaCircolare<>(
                new Image(getClass().getClassLoader().getResourceAsStream("mappa1_gallery.jpg"))
        );
    }

    private void setAzioniBottoniScorrimento() {
        bottoneDestra.setOnAction(e -> {
            immagini.scorriDestra();
            vistaMappa.setImage(immagini.getCorrente());
        });
        bottoneSinistra.setOnAction(e -> {
            immagini.scorriSinistra();
            vistaMappa.setImage(immagini.getCorrente());
        });
    }

    private void setAzioneBottoneConferma() {
        bottoneConferma.setOnAction(e -> {
            try {
                if (super.getApplication().isSocketClient()) {
                    setterMappa = ComunicazioneSceltaMappaSocket.getInstance();
                } else {
                    setterMappa = ComunicazioneSceltaMappaRMI.getInstance();
                }
                int id = immagini.getPosizioneCorrente();
                setterMappa.comunicaSceltaMappa(id);  //setta la mappa nel server
            }catch(SingletonNonInizializzatoException ex) {
                ex.printStackTrace();
            }
        });
    }
}