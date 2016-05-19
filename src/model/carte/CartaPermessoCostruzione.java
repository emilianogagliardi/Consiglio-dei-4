package model.carte;

import java.util.ArrayList;
import model.Città;
import model.bonus.Bonus;

public class CartaPermessoCostruzione extends CartaConBonus {
    ArrayList<Città> città = new ArrayList<>(1);

    public CartaPermessoCostruzione (Bonus bonus, Città... città) {
        super(bonus);
        for (int i = 0; i < città.length; i++) {
            this.città.add(città[i]);
        }
    }

    public ArrayList<Città> getCittà(){
        return città;
    }

}
