package client.view.GUI;

import classicondivise.Vendibile;
import interfaccecondivise.InterfacciaController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
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
    private Label prezzoPermit1, prezzoPermit2, prezzoPermit3, prezzoPolitica1, prezzoPolitica2, prezzoPolitica3, prezzoAiutanti1, prezzoAiutanti2, prezzoAiutanti3;
    @FXML
    private HBox permit1, permit2, permit3, politica1, politica2, politica3, aiutanti1;
    @FXML
    private ImageView imgAiutante1, imgAiutante2, imgAiutante3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImmaginiAiutanti();
    }

    private void setImmaginiAiutanti(){
        Image img = new Image(getClass().getClassLoader().getResourceAsStream("bonus_aiutante.png"));
        imgAiutante1.setImage(img);
        imgAiutante2.setImage(img);
        imgAiutante3.setImage(img);
    }

    void updateVetrina(List<Vendibile> inVendita){

    }

    public void setController(InterfacciaController controller){this.controller = controller;}
}
