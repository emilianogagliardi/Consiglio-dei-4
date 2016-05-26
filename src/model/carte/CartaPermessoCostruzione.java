package model.carte;

import java.util.ArrayList;


import model.Città;
import model.bonus.Bonus;

public class CartaPermessoCostruzione extends CartaConBonus {
    private ArrayList<Città> città = new ArrayList<>(1);

    public CartaPermessoCostruzione (Bonus bonus, Città... città) throws IllegalArgumentException{
        super(bonus);
        if (città == null) throw new IllegalArgumentException("non è possibile creare carte permit senza città");
        for (Città elemento : città) {
            this.città.add(elemento);
        }
    }

    public ArrayList<Città> getCittà(){
        return città;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CartaPermessoCostruzione) {
            CartaPermessoCostruzione c = (CartaPermessoCostruzione) o;
            if (c.getCittà().containsAll(this.getCittà()) && this.getCittà().containsAll(c.getCittà()))
                return true;
        }
        return false;
    }
}
