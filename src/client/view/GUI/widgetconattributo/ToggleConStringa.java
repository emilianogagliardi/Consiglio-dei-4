package client.view.GUI.widgetconattributo;

import javafx.scene.control.ToggleButton;

/**
 * Created by emilianogagliardi on 27/06/16.
 */
public class ToggleConStringa extends ToggleButton{
    private String stringa;

    public ToggleConStringa(String stringa){
        this.stringa = stringa;
    }

    public String getStringa(){return stringa;}
}
