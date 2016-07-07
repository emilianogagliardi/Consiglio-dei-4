package classicondivise.carte;

import classicondivise.CostantiCondivise;
import classicondivise.bonus.Bonus;
import classicondivise.NomeCittà;

import java.io.Serializable;
import java.util.HashSet;

public class CartaPermessoCostruzione extends CartaConBonus implements Serializable{
    private HashSet<NomeCittà> città = new HashSet<>(1);

    public CartaPermessoCostruzione (Bonus bonus, HashSet<NomeCittà> città){ //le città devono appartenere alla regione cui appartiene il mazzo di carte permesso
        super(bonus);
        this.città = città;
    }

    @Override
    public boolean equals(Object other){ //due carte permesso sono uguali se hanno le stesse città
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof CartaPermessoCostruzione))return false;
        CartaPermessoCostruzione otherCartaPermessoCostruzione = (CartaPermessoCostruzione) other;
        return this.città.containsAll(otherCartaPermessoCostruzione.getCittà());
    }

    @Override
    public int hashCode(){
        return CostantiCondivise.HASH_CODE_CARTE_PERMESSO_COSTRUZIONE;
    }

    public HashSet<NomeCittà> getCittà(){
        return città;
    }

}
