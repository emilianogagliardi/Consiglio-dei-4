package server.model.carte;

import java.io.Serializable;
import java.util.ArrayList;

import server.model.NomeCittà;
import server.model.bonus.Bonus;

public class CartaPermessoCostruzione extends CartaConBonus implements Serializable{
    private ArrayList<NomeCittà> città = new ArrayList<>(1);

    public CartaPermessoCostruzione (Bonus bonus, ArrayList<NomeCittà> città){ //le città devono appartenere alla regione cui appartiene il mazzo di carte permesso
        super(bonus);
        this.città = città;
    }

    public ArrayList<NomeCittà> getCittà(){
        return città;
    }
}
