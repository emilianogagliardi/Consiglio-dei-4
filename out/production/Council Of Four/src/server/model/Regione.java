package server.model;


import classicondivise.NomeCittà;
import classicondivise.bonus.BonusPuntiVittoria;
import server.model.carte.CartaBonusRegione;
import classicondivise.carte.CartaPermessoCostruzione;
import interfaccecondivise.InterfacciaView;

import java.rmi.RemoteException;
import java.util.*;

import static server.model.CostantiModel.NUM_CITTA_PER_REGIONE;


public class Regione extends Observable{
    private BalconeDelConsiglio balconeDelConsiglio;
    private CartaBonusRegione cartaBonusRegione;
    private CartaPermessoCostruzione cartaPermessoCostruzione1;
    private CartaPermessoCostruzione cartaPermessoCostruzione2;
    private HashSet<Città> città = new HashSet<Città>(NUM_CITTA_PER_REGIONE); //HashSet è un'implementazione dell'interfaccia Set, una Collection che non permette duplicati. NUM_CITTA_PER_REGIONE è la capacità iniziale
    private Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzione;
    private NomeRegione nomeRegione;

    public Regione(NomeRegione nomeRegione, Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzione, BalconeDelConsiglio balcone, CartaBonusRegione cartaBonusRegione, ArrayList<InterfacciaView> views) throws IllegalArgumentException {
        super(views);
        this.nomeRegione = Objects.requireNonNull(nomeRegione);
        if (!nomeRegione.equals(balcone.getIdBalcone().toNomeRegione()))
            throw new IllegalArgumentException("Il balcone non appartiene a questa regione");
        this.balconeDelConsiglio = Objects.requireNonNull(balcone);
        if (!nomeRegione.equals(cartaBonusRegione.getNomeRegione()))
            throw new IllegalArgumentException("La carta non appartiene a questa regione");
        this.cartaBonusRegione = Objects.requireNonNull(cartaBonusRegione);
        this.mazzoCartePermessoCostruzione = Objects.requireNonNull(mazzoCartePermessoCostruzione);
        this.mazzoCartePermessoCostruzione.mischia();
        cartaPermessoCostruzione1 = mazzoCartePermessoCostruzione.ottieniCarta();
        cartaPermessoCostruzione1.setVisibile(true);
        cartaPermessoCostruzione2 = mazzoCartePermessoCostruzione.ottieniCarta();
        cartaPermessoCostruzione2.setVisibile(true);
        updateViewCartePermessoCostruzione();
        //updateViewCarteBonusRegione();
    }

    public BalconeDelConsiglio getBalconeDelConsiglio(){
        return balconeDelConsiglio;
    }

    public NomeRegione getNome() { return nomeRegione; }

    public CartaBonusRegione ottieniCartaBonusRegione() throws NoSuchElementException {
        CartaBonusRegione cartaBonusRegioneDaRitornare = this.cartaBonusRegione;
        this.cartaBonusRegione = null;
        if (cartaBonusRegioneDaRitornare == null) {
            throw new NoSuchElementException();
        }
        return cartaBonusRegioneDaRitornare;
    }

    public CartaPermessoCostruzione ottieniCartaPermessoCostruzione1() throws NoSuchElementException {
        CartaPermessoCostruzione cartaDaRitornare = cartaPermessoCostruzione1;
        cartaPermessoCostruzione1 = mazzoCartePermessoCostruzione.ottieniCarta();
        cartaPermessoCostruzione1.setVisibile(true);
        updateViewCartePermessoCostruzione();
        try {
            cartaPermessoCostruzione1 = mazzoCartePermessoCostruzione.ottieniCarta();
            cartaPermessoCostruzione1.setVisibile(true);
        } catch (NoSuchElementException exc){
            cartaPermessoCostruzione1 = null;
        }
        updateViewCartePermessoCostruzione();
        if (cartaDaRitornare == null){
            throw new NoSuchElementException();
        }
        return cartaDaRitornare;
    }

    public boolean cartaPermessoCostruzione1IsNull(){
        return cartaPermessoCostruzione1 == null;
    }

    public CartaPermessoCostruzione ottieniCartaPermessoCostruzione2() throws NoSuchElementException{
        CartaPermessoCostruzione cartaDaRitornare = cartaPermessoCostruzione2;
        cartaPermessoCostruzione2 = mazzoCartePermessoCostruzione.ottieniCarta();
        cartaPermessoCostruzione2.setVisibile(true);
        updateViewCartePermessoCostruzione();
        try {
            cartaPermessoCostruzione2 = mazzoCartePermessoCostruzione.ottieniCarta();
            cartaPermessoCostruzione2.setVisibile(true);
        } catch (NoSuchElementException exc) {
            cartaPermessoCostruzione2 = null;
        }
        updateViewCartePermessoCostruzione();
        if (cartaDaRitornare == null){
            throw new NoSuchElementException();
        }
        return cartaDaRitornare;
    }

    public boolean cartaPermessoCostruzione2IsNull(){
        return cartaPermessoCostruzione2 == null;
    }

    public void addCittà (Città città) throws IllegalArgumentException{
        if (!città.getNomeRegione().equals(this.getNome()))
            throw new IllegalArgumentException("La città non appartiene a questa regione");
        this.città.add(città);
    }

    public void addCittà(HashSet<Città> città) throws IllegalArgumentException {
        for (Città cittàSingola : città)
            if (!cittàSingola.getNomeRegione().equals(this.getNome()))
                throw new IllegalArgumentException("La città non appartiene a questa regione");
            else this.città.add(cittàSingola);
    }

    public HashSet<Città> getCittà(){
        return città;
    }

    public ArrayList<NomeCittà> getNomiCittà(){
        ArrayList<NomeCittà> nomiCittà = new ArrayList<>();
        for(Città cittàSingola : this.città)
            nomiCittà.add(cittàSingola.getNome());
        return nomiCittà;
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

    private void updateViewCartePermessoCostruzione() {
        super.notifyViews((InterfacciaView view) -> {
            try {
                view.updateCartePermessoRegione(getNome().toString(), cartaPermessoCostruzione1, cartaPermessoCostruzione2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
