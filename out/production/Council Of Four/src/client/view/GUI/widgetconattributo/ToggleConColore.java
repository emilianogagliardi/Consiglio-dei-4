package client.view.GUI.widgetconattributo;

import classicondivise.Colore;
import javafx.scene.control.ToggleButton;

/**
 * Created by emilianogagliardi on 27/06/16.
 */
public class ToggleConColore extends ToggleButton{
    private String colore;

    public ToggleConColore(String colore) throws IllegalArgumentException{
        Colore.valueOf(colore);
        this.colore = colore;
    }

    public String getColore(){return colore;}
}
