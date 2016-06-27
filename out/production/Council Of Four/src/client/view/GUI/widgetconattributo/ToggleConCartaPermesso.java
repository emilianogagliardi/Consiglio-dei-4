package client.view.GUI.widgetconattributo;

import classicondivise.carte.CartaPermessoCostruzione;
import javafx.scene.control.ToggleButton;

/**
 * Created by emilianogagliardi on 27/06/16.
 */
public class ToggleConCartaPermesso extends ToggleButton{
    private CartaPermessoCostruzione carta;

    public ToggleConCartaPermesso(CartaPermessoCostruzione carta){this.carta = carta;}

    public CartaPermessoCostruzione getCarta(){return carta;}
}
