import java.util.ArrayList;
package Model.Carte;
/**
 * Created by riccardo on 18/05/16.
 */
public class CartaPermessoCostruzione extends CartaConBonus {
    ArrayList<Città> città = new ArrayList<>(1);

    public CartaPermessoCostruzione (Città... città) {
        for (int i = 0; i < città.length; i++) {
            this.città.add(città[i]);
        }
    }

    public ArrayList<Città> getCittà(){
        return città;
    }

}
