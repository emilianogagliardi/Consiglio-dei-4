package client.view.GUI.controllerFX;

import client.ComunicazioneSceltaMappa;
import client.ComunicazioneSceltaMappaRMI;
import client.ComunicazioneSceltaMappaSocket;
import client.view.GUI.GestoreFlussoFinestra;
import client.view.eccezioni.SingletonNonInizializzatoException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import client.strutturedati.ListaCircolare;

import java.net.URL;
import java.util.ResourceBundle;


public class ControllerFXGallery extends GestoreFlussoFinestra implements Initializable{
    private static boolean isSocketClient;
    private ComunicazioneSceltaMappa setterMappa;
    private ListaCircolare<Image> immagini;
    @FXML
    private Button bottoneSinistra, bottoneDestra;
    @FXML
    private Button bottoneConferma;
    @FXML
    private ImageView vistaMappa;

    public ControllerFXGallery(){
        try{
            if (isSocketClient) {
                setterMappa = ComunicazioneSceltaMappaSocket.getInstance();
            }else{
                setterMappa = ComunicazioneSceltaMappaRMI.getInstance();
            }
        }catch(SingletonNonInizializzatoException e){
            e.printStackTrace();
        }
    }

    public void setIsSocketClient(){
        isSocketClient = true;
    }

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
                new Image(getClass().getClassLoader().getResourceAsStream("img0.jpg")),
                new Image(getClass().getClassLoader().getResourceAsStream("img1.jpg")),
                new Image(getClass().getClassLoader().getResourceAsStream("img2.jpg")),
                new Image(getClass().getClassLoader().getResourceAsStream("img3.jpg")),
                new Image(getClass().getClassLoader().getResourceAsStream("img4.jpg"))
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

    //TODO da gestire in socket e RMI
    private void setAzioneBottoneConferma() {
        bottoneConferma.setOnAction(e -> {
            int id = immagini.getPosizioneCorrente();
            setterMappa.comunicaSceltaMappa(id);
        });
    }
}
