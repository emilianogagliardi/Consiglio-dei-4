package model;


import model.carte.CartaBonusRegione;
import model.carte.CartaPermessoCostruzione;

import java.util.Collection;
import java.util.HashSet;

import static model.Costanti.NUM_CITTA_PER_REGIONE;


public class Regione {
    private BalconeDelConsiglio balconeDelConsiglio;
    private CartaBonusRegione cartaBonusRegione;
    private CartaPermessoCostruzione cartaPermessoCostruzione1;
    private CartaPermessoCostruzione cartaPermessoCostruzione2;
    private Collection<Città> città = new HashSet<Città>(NUM_CITTA_PER_REGIONE); //HashSet è un'implementazione dell'interfaccia Set, una Collection che non permette duplicati. 5 è la capacità iniziale
    private Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzione;

    public Regione(Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzione, BalconeDelConsiglio balcone, CartaBonusRegione cartaBonusRegione){
        this.balconeDelConsiglio = balcone;
        this.cartaBonusRegione = cartaBonusRegione;
        this.mazzoCartePermessoCostruzione = mazzoCartePermessoCostruzione;
        this.mazzoCartePermessoCostruzione.mischia();
        this.cartaPermessoCostruzione1 = mazzoCartePermessoCostruzione.getCarta();
        this.cartaPermessoCostruzione1.setVisibile(true);
        this.cartaPermessoCostruzione2 = mazzoCartePermessoCostruzione.getCarta();
        this.cartaPermessoCostruzione2.setVisibile(true);
    }

    public BalconeDelConsiglio getBalconeDelConsiglio(){
        return balconeDelConsiglio;
    }

    public CartaBonusRegione getCartaBonusRegione(){
        cartaBonusRegione = null;
        return cartaBonusRegione;
    }

    public CartaPermessoCostruzione getCartaPermessoCostruzione1(){
        CartaPermessoCostruzione cartaDaRitornare = cartaPermessoCostruzione1;
        cartaPermessoCostruzione1 = mazzoCartePermessoCostruzione.getCarta();
        cartaPermessoCostruzione1.setVisibile(true);
        return cartaDaRitornare;
    }

    public CartaPermessoCostruzione getCartaPermessoCostruzione2(){
        CartaPermessoCostruzione cartaDaRitornare = cartaPermessoCostruzione2;
        cartaPermessoCostruzione2 = mazzoCartePermessoCostruzione.getCarta();
        cartaPermessoCostruzione2.setVisibile(true);
        return cartaDaRitornare;
}

    public void addCittà (Città città) {
        this.città.add(città);
    }

    public Collection<Città> getCittà(){
        return città;
    }


    public void cambiaCartePermessoCostruzione(){
        cartaPermessoCostruzione1.setVisibile(false);
        mazzoCartePermessoCostruzione.addCarta(cartaPermessoCostruzione1);
        cartaPermessoCostruzione2.setVisibile(false);
        mazzoCartePermessoCostruzione.addCarta(cartaPermessoCostruzione2);
        cartaPermessoCostruzione1 = mazzoCartePermessoCostruzione.getCarta();
        cartaPermessoCostruzione1.setVisibile(true);
        cartaPermessoCostruzione2 = mazzoCartePermessoCostruzione.getCarta();
        cartaPermessoCostruzione2.setVisibile(true);
    }
}
