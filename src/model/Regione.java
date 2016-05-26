package model;


import model.carte.CartaBonusRegione;
import model.carte.CartaPermessoCostruzione;

import java.util.Collection;
import java.util.HashSet;

import static model.Costanti.NUM_CITTA_REGIONE;

public class Regione {
    private BalconeDelConsiglio balconeDelConsiglio;
    private CartaBonusRegione cartaBonusRegione;
    private CartaPermessoCostruzione cartaPermessoCostruzione1;
    private CartaPermessoCostruzione cartaPermessoCostruzione2;
    private Collection<Città> città = new HashSet<Città>(NUM_CITTA_REGIONE); //HashSet è un'implementazione dell'interfaccia Set, una Collection che non permette duplicati. 5 è la capacità iniziale
    private Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzione = new Mazzo<>();

    public Regione(BalconeDelConsiglio balcone, CartaBonusRegione cartaBonusRegione, CartaPermessoCostruzione cartaPermessoCostruzione1, CartaPermessoCostruzione cartaPermessoCostruzione2){
        this.balconeDelConsiglio = balcone;
        this.cartaBonusRegione = cartaBonusRegione;
        this.cartaPermessoCostruzione1 = cartaPermessoCostruzione1;
        this.cartaPermessoCostruzione2 = cartaPermessoCostruzione2;
    }

    public BalconeDelConsiglio getBalconeDelConsiglio(){
        return balconeDelConsiglio;
    }

    public CartaBonusRegione getCartaBonusRegione(){
        return cartaBonusRegione;
    }

    public CartaPermessoCostruzione getCartaPermessoCostruzione1(){
        return cartaPermessoCostruzione1;
    }

    public CartaPermessoCostruzione getCartaPermessoCostruzione2(){
        return cartaPermessoCostruzione2;
}

    public void addCittà (Città città) {
        this.città.add(città);
    }

    public Collection<Città> getCittà(){
        return città;
    }

    public void addCartaPermessoCostruzione(CartaPermessoCostruzione cartaPermessoCostruzione){
        mazzoCartePermessoCostruzione.addCarta(cartaPermessoCostruzione);
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
