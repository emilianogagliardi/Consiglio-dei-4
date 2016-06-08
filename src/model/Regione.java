package model;


import model.bonus.BonusPuntiVittoria;
import model.carte.CartaBonusRegione;
import model.carte.CartaPermessoCostruzione;
import proxyView.InterfacciaView;

import java.util.*;

import static model.Costanti.NUM_CITTA_PER_REGIONE;


public class Regione extends Observable{
    private BalconeDelConsiglio balconeDelConsiglio;
    private CartaBonusRegione cartaBonusRegione;
    private CartaPermessoCostruzione cartaPermessoCostruzione1;
    private CartaPermessoCostruzione cartaPermessoCostruzione2;
    private Collection<Città> città = new HashSet<Città>(NUM_CITTA_PER_REGIONE); //HashSet è un'implementazione dell'interfaccia Set, una Collection che non permette duplicati. NUM_CITTA_PER_REGIONE è la capacità iniziale
    private Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzione;
    private NomeRegione nomeRegione;

    public Regione(NomeRegione nomeRegione, Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzione, BalconeDelConsiglio balcone, CartaBonusRegione cartaBonusRegione, ArrayList<InterfacciaView> views) throws IllegalArgumentException {
        super(views);
        this.nomeRegione = Objects.requireNonNull(nomeRegione);
        if (!nomeRegione.equals(balconeDelConsiglio.getIdBalcone()))
            throw new IllegalArgumentException("Il balcone non appartiene a questa regione");
        this.balconeDelConsiglio = Objects.requireNonNull(balcone);
        this.cartaBonusRegione = Objects.requireNonNull(cartaBonusRegione);
        this.mazzoCartePermessoCostruzione = Objects.requireNonNull(mazzoCartePermessoCostruzione);
        this.mazzoCartePermessoCostruzione.mischia();
        cartaPermessoCostruzione1 = mazzoCartePermessoCostruzione.ottieniCarta();
        cartaPermessoCostruzione1.setVisibile(true);
        cartaPermessoCostruzione2 = mazzoCartePermessoCostruzione.ottieniCarta();
        cartaPermessoCostruzione2.setVisibile(true);
        updateViewCartePermessoCostruzione();
        updateViewCarteBonusRegione();
    }

    public BalconeDelConsiglio getBalconeDelConsiglio(){
        return balconeDelConsiglio;
    }

    public NomeRegione getNome() { return nomeRegione; }

    public CartaBonusRegione ottieniCartaBonusRegione(){
        CartaBonusRegione cartaBonusRegioneDaRitornare = this.cartaBonusRegione;
        this.cartaBonusRegione = null;
        updateViewCarteBonusRegione();
        return cartaBonusRegioneDaRitornare;
    }

    public CartaPermessoCostruzione getCartaPermessoCostruzione1(){
        CartaPermessoCostruzione cartaDaRitornare = cartaPermessoCostruzione1;
        cartaPermessoCostruzione1 = mazzoCartePermessoCostruzione.ottieniCarta();
        cartaPermessoCostruzione1.setVisibile(true);
        updateViewCartePermessoCostruzione();
        return cartaDaRitornare;
    }

    public CartaPermessoCostruzione getCartaPermessoCostruzione2(){
        CartaPermessoCostruzione cartaDaRitornare = cartaPermessoCostruzione2;
        cartaPermessoCostruzione2 = mazzoCartePermessoCostruzione.ottieniCarta();
        cartaPermessoCostruzione2.setVisibile(true);
        updateViewCartePermessoCostruzione();
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
        cartaPermessoCostruzione1 = mazzoCartePermessoCostruzione.ottieniCarta();
        cartaPermessoCostruzione1.setVisibile(true);
        cartaPermessoCostruzione2 = mazzoCartePermessoCostruzione.ottieniCarta();
        cartaPermessoCostruzione2.setVisibile(true);
        updateViewCartePermessoCostruzione();
    }

    //update view
    private void updateViewCarteBonusRegione() {
        int puntiCarta = 0;
        if (cartaBonusRegione != null) {
            try {
                BonusPuntiVittoria bonus = (BonusPuntiVittoria) cartaBonusRegione.ottieniBonus();
                puntiCarta = bonus.getPuntiVittoria();
            }catch(ClassCastException e){
                System.out.println("la carta bonus regione non ha un bonus punti vittoria");
                puntiCarta = 0; // errore
            }
        }
        else {
            puntiCarta = -1; //-1 indica l'assenza della carta
        }
        int finalPuntiCarta = puntiCarta; //per utilizzarla nella lambda expression
        super.notifyViews((InterfacciaView view) -> view.updateCarteBonusRegioneTabellone(getNome().toString(), finalPuntiCarta));
    }

    private void updateViewCartePermessoCostruzione() {
        super.notifyViews((InterfacciaView view) -> view.updateCartePermessoRegione(getNome().toString(), cartaPermessoCostruzione1, cartaPermessoCostruzione2));
    }
}
