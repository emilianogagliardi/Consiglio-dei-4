package model.carte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;


import model.Città;
import model.bonus.Bonus;

public class CartaPermessoCostruzione extends CartaConBonus implements Serializable{
    private ArrayList<Città> città = new ArrayList<>(1);

    public CartaPermessoCostruzione (Bonus bonus, ArrayList<Città> città){ //le città devono appartenere alla regione cui appartiene il mazzo di carte permesso
        super(bonus);
        this.città = città;
    }

    public ArrayList<Città> getCittà(){
        return città;
    }
}
