package model.carte;

import java.util.ArrayList;
import java.util.Objects;


import model.Città;
import model.bonus.Bonus;

public class CartaPermessoCostruzione extends CartaConBonus {
    private ArrayList<Città> città = new ArrayList<>(1);


    public CartaPermessoCostruzione (Bonus bonus, Città... città) throws IllegalArgumentException{ //le città devono appartenere alla regione cui appartiene il mazzo di carte permesso
        super(bonus);
        if (città == null) throw new IllegalArgumentException("Non è possibile creare carte permesso senza città");
        for (Città elemento : città) {
            this.città.add(elemento);
        }
    }

    public ArrayList<Città> getCittà(){
        return città;
    }
}
