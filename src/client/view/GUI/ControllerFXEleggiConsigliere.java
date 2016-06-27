package client.view.GUI;

import classicondivise.Colore;
import classicondivise.IdBalcone;
import client.view.RiservaPartitaView;
import interfaccecondivise.InterfacciaController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.reflect.Field;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by emilianogagliardi on 26/06/16.
 */
public class ControllerFXEleggiConsigliere extends GestoreFlussoFinestra implements Initializable{
    private InterfacciaController controller;
    private IdBalcone balcone;
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton btn1, btn2, btn3, btn4, btn5, btn6, comeAzionePrincipale, comeAzioneVeloce;
    @FXML
    private Button bottoneConferma;

    //friendly
    void setController(InterfacciaController controller){this.controller = controller;}
    void setBalcone(IdBalcone balcone){this.balcone = balcone;}


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> consiglieri = RiservaPartitaView.getInstance().getConsiglieri();
        btn1.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_arancione.png"))));
        if (!consiglieri.contains("ARANCIONE")) btn1.setDisable(true);
        btn2.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_azzurro.png"))));
        if (!consiglieri.contains("AZZURRO")) btn2.setDisable(true);
        btn3.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_bianco.png"))));
        if (!consiglieri.contains("BIANCO")) btn3.setDisable(true);
        btn4.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_nero.png"))));
        if (!consiglieri.contains("NERO")) btn4.setDisable(true);
        btn5.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_rosa.png"))));
        if (!consiglieri.contains("ROSA")) btn5.setDisable(true);
        btn6.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("consigliere_viola.png"))));
        if (!consiglieri.contains("VIOLA")) btn6.setDisable(true);
        toggleGroup = new ToggleGroup();
        btn1.setToggleGroup(toggleGroup);
        btn2.setToggleGroup(toggleGroup);
        btn3.setToggleGroup(toggleGroup);
        btn4.setToggleGroup(toggleGroup);
        btn5.setToggleGroup(toggleGroup);
        btn6.setToggleGroup(toggleGroup);
        setAzioneBottoni();
        inizializzaBottoniTipoMossa();
        inizializzaBtnConferma();
    }

    private void  inizializzaBottoniTipoMossa(){
        comeAzionePrincipale.setOnMouseClicked(event -> {
            comeAzioneVeloce.setSelected(false);
        });
        comeAzioneVeloce.setOnMouseClicked(event -> {
            comeAzionePrincipale.setSelected(false);
        });
    }

    private void setAzioneBottoni(){
        for (int i = 1; i < 7; i++){
            Field fieldBtn = null;
            try {
                fieldBtn = getClass().getDeclaredField("btn"+i);
                RadioButton btn = (RadioButton) fieldBtn.get(this);
                btn.setOnMouseClicked(event -> {
                    toggleGroup.getToggles().forEach(otherBtn ->{
                        if(!otherBtn.equals(btn)){
                            otherBtn.setSelected(false);
                        }
                    });
                });
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void inizializzaBtnConferma(){
        bottoneConferma.setOnMouseClicked(event -> {
            RadioButton premuto = (RadioButton) toggleGroup.getSelectedToggle();
            if(premuto != null){
                Colore colore = null;
                if (premuto.equals(btn1))
                    colore = Colore.ARANCIONE;
                else if (premuto.equals(btn2))
                    colore = Colore.AZZURRO;
                else if (premuto.equals(btn3))
                    colore = Colore.BIANCO;
                else if (premuto.equals(btn4))
                    colore = Colore.NERO;
                else if (premuto.equals(btn5))
                    colore = Colore.ROSA;
                else if (premuto.equals(btn6))
                    colore = Colore.VIOLA;
                if(comeAzionePrincipale.isSelected() || comeAzioneVeloce.isSelected()) {
                    try {
                        if (comeAzionePrincipale.isSelected())
                            controller.eleggereConsigliere(balcone.toString(), colore.toString());
                        else controller.mandareAiutanteEleggereConsigliere(balcone.toString(), colore.toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> super.getApplication().chiudiFinestraSecondaria());
                }
            }
        });
    }

}
