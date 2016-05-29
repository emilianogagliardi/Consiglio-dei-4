package model;

import model.bonus.Bonus;
import model.carte.Carta;
import model.carte.CartaBonusColoreCittà;
import model.carte.CartaPolitica;
import model.carte.CartaPremioDelRe;
import model.eccezioni.*;

import java.util.*;

import static model.Costanti.NUM_CARTE_BONUS_COLORE_CITTA;
import static model.Costanti.NUM_CONSIGLIERI_RISERVA;
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
    private Collection<Consigliere> riservaConsiglieri;
    private List<Giocatore> giocatori;

    public void setRe(Città cittàRe) throws ReNonInizializzatoException{
        Re.init(cittàRe);
        re = Re.getInstance();
    }

    public void setBalconeDelConsiglioRe(BalconeDelConsiglio balconeDelConsiglioRe){
        if (this.balconeDelConsiglioRe == null)
            this.balconeDelConsiglioRe = balconeDelConsiglioRe;
    }

    public void setRegioni(Collection<Regione> regioni) throws IllegalArgumentException {
        if (regioni.size() != NUM_REGIONI){
            throw new IllegalArgumentException("Il numero di regioni deve essere " + NUM_REGIONI);
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

    public void setCarteBonusColoreCittà(Collection<CartaBonusColoreCittà> carteBonusColoreCittà) throws IllegalArgumentException  {
        if(carteBonusColoreCittà.size() != NUM_CARTE_BONUS_COLORE_CITTA){
            throw new IllegalArgumentException("Il numero di carte bonus colore città deve essere " + NUM_CARTE_BONUS_COLORE_CITTA);
        }
        else if(this.carteBonusColoreCittà == null)
            this.carteBonusColoreCittà = carteBonusColoreCittà;
    }

    public void setRiservaConsiglieri(Collection<Consigliere> riservaConsiglieri) throws IllegalArgumentException {
        if(riservaConsiglieri.size() != NUM_CONSIGLIERI_RISERVA) {
            throw new IllegalArgumentException("Il numero di consiglieri deve essere " + NUM_CONSIGLIERI_RISERVA);
        }
        else if (this.riservaConsiglieri == null){
            this.riservaConsiglieri = riservaConsiglieri;
        }
    }

    public void addGiocatore(Giocatore giocatore) throws IllegalArgumentException {
        if(giocatore == null)
            throw new IllegalArgumentException("giocatore is null");
        this.giocatori.add(giocatore);
    }

    public List<Giocatore> getGiocatori(){
        return giocatori;
    }
}
