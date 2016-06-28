package client.view.GUI;

import classicondivise.carte.CartaPermessoCostruzione;
import client.view.GUI.utility.UtilityGUI;
import client.view.GUI.widgetconattributo.ToggleConCartaPermesso;
import client.view.GUI.widgetconattributo.ToggleConColore;
import client.view.GiocatoreView;
import interfaccecondivise.InterfacciaController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by emilianogagliardi on 27/06/16.
 */
public class ControllerFXVendi extends GestoreFlussoFinestra implements Initializable {
    @FXML
    private Label numeroAiutanti, prezzoAiutanti, prezzoPolitica, prezzoPermit;
    @FXML
    private ImageView imgAiutanti;
    @FXML
    private Button btnConferma, btnPiuAiutanti, btnMenoAiutanti, btnPiuPrezzoAiutanti, btnMenoPrezzoAiutanti, btnPiuPrezzoPolitica, btnMenoPrezzoPolitica, btnPiuPrezzoPermit, btnMenoPrezzoPermit;
    @FXML
    private HBox hBoxPermit, hBoxPolitica;

    private InterfacciaController controller;

    public void setController(InterfacciaController controller){this.controller = controller;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numeroAiutanti.setText(String.format("%d", 0));
        inizializzaBottoni(btnPiuAiutanti, btnMenoAiutanti, numeroAiutanti, GiocatoreView.getInstance().getAiutanti());
        inizializzaBottoni(btnPiuPrezzoAiutanti, btnMenoPrezzoAiutanti, prezzoAiutanti, 20);
        inizializzaBottoni(btnPiuPrezzoPolitica, btnMenoPrezzoPolitica, prezzoPolitica, 20);
        inizializzaBottoni(btnPiuPrezzoPermit, btnMenoPrezzoPermit, prezzoPermit, 20);
        imgAiutanti.setImage(new Image(getClass().getClassLoader().getResourceAsStream("bonus_aiutante.png")));
        setCartePermit();
        setCartePolitica();
        setAzioneBtnConferma();
    }

    private void inizializzaBottoni(Button btnPiu, Button btnMeno, Label numero, int max){
        btnPiu.setOnMouseClicked(event -> {
            if(!(Integer.parseInt(numero.getText()) >= max)){
                numero.setText(String.format("%d", (Integer.parseInt(numero.getText())+1)));
            }
        });
        btnMeno.setOnMouseClicked(event -> {
            if(!(Integer.parseInt(numero.getText()) == 0)) {
                numero.setText(String.format("%d", (Integer.parseInt(numero.getText()) - 1)));
            }
        });
    }

    private void setCartePermit() {
        UtilityGUI utilityGUI = new UtilityGUI();
        GiocatoreView.getInstance().getCartePermesso().forEach(carta -> {
            StackPane stackPane = new StackPane();
            utilityGUI.creaPermit(carta, 95, 75, stackPane, false);
            ToggleConCartaPermesso toggleConCartaPermesso = new ToggleConCartaPermesso(carta);
            toggleConCartaPermesso.setGraphic(stackPane);
            hBoxPermit.getChildren().add(toggleConCartaPermesso);
        });
    }

    private void setCartePolitica(){
        UtilityGUI utilityGUI = new UtilityGUI();
        utilityGUI.addPoliticaInBoxAsToggle(hBoxPolitica, GiocatoreView.getInstance().getCartePolitica());
    }

    private void setAzioneBtnConferma(){
        btnConferma.setOnMouseClicked(event -> {
            try {
                vendiAiutanti();
                vendiPolitica();
                vendiPermit();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void vendiAiutanti() throws RemoteException {
        if(!(Integer.parseInt(numeroAiutanti.getText())==0)){
            int prezzo = Integer.parseInt(prezzoAiutanti.getText());
            int numero = Integer.parseInt(numeroAiutanti.getText());
            if(prezzo > 20) prezzo = 20;
            if (prezzo < 0) prezzo = 0;
            controller.vendiAiutanti(2, 3);
        }
    }

    private void vendiPolitica() throws RemoteException {
        List<String> inVendita = new ArrayList<>();
        if(hBoxPolitica.getChildren().size() != 0){
            hBoxPolitica.getChildren().forEach((Node nodo) -> {
                ToggleConColore toggle = (ToggleConColore) nodo;
                if(toggle.isSelected()) inVendita.add(toggle.getColore());
            });
            int prezzo = Integer.parseInt(prezzoPolitica.getText());
            if(prezzo > 20) prezzo = 20;
            if (prezzo < 0) prezzo = 0;
            controller.vendiCartePolitica(inVendita, prezzo);
        }
    }

    private void vendiPermit() throws RemoteException{
        List<CartaPermessoCostruzione> inVendita = new ArrayList<>();
        if(hBoxPermit.getChildren().size() != 0){
            hBoxPermit.getChildren().forEach((Node nodo) ->{
                ToggleConCartaPermesso toggle = (ToggleConCartaPermesso) nodo;
                if (toggle.isSelected()) inVendita.add(toggle.getCarta());
            });
            int prezzo = Integer.parseInt(prezzoPermit.getText());
            if(prezzo > 20) prezzo = 20;
            if (prezzo < 0) prezzo = 0;
            controller.vendiCartePermesso(inVendita, prezzo);
        }
    }
}
