package model;

import model.bonus.Bonus;
import model.carte.Carta;
import model.carte.CartaBonusColoreCittà;
import model.carte.CartaPolitica;
import model.carte.CartaPremioDelRe;
import model.eccezioni.*;

import java.util.*;

import static model.Costanti.NUM_REGIONI;

public class Partita {
    private Re re;
    private BalconeDelConsiglio balconeDelConsiglioRe;
    private Collection<Regione> regioni;
    private List<Bonus> percorsoDellaNobiltà;
    private Mazzo<CartaPolitica> cartePoliticaScartate;
    private Mazzo<CartaPolitica> mazzoCartePolitica;
    private Mazzo<CartaPremioDelRe> mazzoCartePremioRe;
    private Collection<CartaBonusColoreCittà> carteBonusColoreCittà;
    private Collection<Consiglere> riservaConsiglieri;

    public void setRe(Città cittàRe) throws ReNonInizializzatoException{
        Re.init(cittàRe);
        re = Re.getInstance();
    }

    public void setBalconeDelConsiglioRe(BalconeDelConsiglio balconeDelConsiglioRe){
        if (this.balconeDelConsiglioRe == null)
            this.balconeDelConsiglioRe = balconeDelConsiglioRe;
    }

    public void setRegioni(Collection<Regione> regioni) throws NumeroRegioniNonValidoException{
        if (regioni.size() != Costanti.NUM_REGIONI){
            throw new NumeroRegioniNonValidoException();
        }
        else if(this.regioni == null) {
            this.regioni = regioni;
        }

}

    public void setPercorsoDellaNobiltà(List<Bonus> percorsoDellaNobiltà){
        if(this.percorsoDellaNobiltà == null) {
            this.percorsoDellaNobiltà = percorsoDellaNobiltà;
        }
    }

    public void setCartePoliticaScartate(Mazzo<CartaPolitica> cartePoliticaScartate){
        if(this.cartePoliticaScartate == null)
            this.cartePoliticaScartate = cartePoliticaScartate;
    }

    public void setMazzoCartePolitica(Mazzo<CartaPolitica> mazzoCartePolitica){
        if(this.mazzoCartePolitica == null)
            this.mazzoCartePolitica = mazzoCartePolitica;
    }

    public void setMazzoCartePremioRe(Mazzo<CartaPremioDelRe> mazzoCartePremioRe){
        if(this.mazzoCartePremioRe == null)
            this.mazzoCartePremioRe = mazzoCartePremioRe;
    }

    public void setCarteBonusColoreCittà(Collection<CartaBonusColoreCittà> carteBonusColoreCittà) throws NumeroCarteBonusColoreCittàNonValidoException  {
        if(carteBonusColoreCittà.size() != Costanti.NUM_CARTE_BONUS_COLORE_CITTA){
            throw new NumeroCarteBonusColoreCittàNonValidoException();
        }
        else if(this.carteBonusColoreCittà == null)
            this.carteBonusColoreCittà = carteBonusColoreCittà;
    }

    public void setRiservaConsiglieri(Collection<Consiglere> riservaConsiglieri) throws NumeroConsiglieriRiservaNonValidoException{
        if(riservaConsiglieri.size() != 8) {
            throw new NumeroConsiglieriRiservaNonValidoException();
        }
        else if (this.riservaConsiglieri == null){
            this.riservaConsiglieri = riservaConsiglieri;
        }
    }



}
