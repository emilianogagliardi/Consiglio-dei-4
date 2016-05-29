package model.carte;

import java.util.ArrayList;


import model.Città;
import model.bonus.Bonus;

public class CartaPermessoCostruzione extends CartaConBonus {
    private ArrayList<Città> città = new ArrayList<>(1);


    public CartaPermessoCostruzione (Bonus bonus, Città... città) throws IllegalArgumentException{ //le città devono appartenere alla regione cui appartiene il mazzo di carte permesso
        super(bonus);
        if (città == null) throw new IllegalArgumentException("non è possibile creare carte permesso senza città");
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
            if (c.getCittà().containsAll(this.getCittà()) && this.getCittà().containsAll(c.getCittà())) //TODO: due carte permesso sono uguali solo se hanno le stesse città? Non si tiene conto dei bonus...
                return true;
        }
        return false;
    }
}
