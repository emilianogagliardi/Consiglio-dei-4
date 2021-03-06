package server.model;

import classicondivise.NomeCittà;
import classicondivise.bonus.Bonus;
import interfaccecondivise.InterfacciaView;
import server.model.carte.CartaBonusColoreCittà;
import server.model.carte.CartaPolitica;
import server.model.carte.CartaPremioDelRe;
import server.model.eccezioni.NumeroMassimoGiocatoriRaggiuntoException;
import server.sistema.CostantiSistema;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import static server.model.CostantiModel.*;

public class Partita extends Observable {
    private Re re;
    private BalconeDelConsiglio balconeDelConsiglioRe;
    private HashSet<Regione> regioni;
    private List<Bonus> percorsoDellaNobiltà;
    private Mazzo<CartaPolitica> cartePoliticaScartate;
    private Mazzo<CartaPolitica> mazzoCartePolitica;
    private Mazzo<CartaPremioDelRe> mazzoCartePremioRe;
    private HashSet<CartaBonusColoreCittà> carteBonusColoreCittà;
    private ArrayList<Consigliere> riservaConsiglieri;
    private ArrayList<Giocatore> giocatori;
    private int riservaAiutanti;

    public Partita(ArrayList<InterfacciaView> views){
        super(views);
        cartePoliticaScartate = new Mazzo<>();
        giocatori = new ArrayList<>(CostantiSistema.NUM_GOCATORI_MAX);
    }

    public List<Bonus> getPercorsoDellaNobiltà(){
        return this.percorsoDellaNobiltà;
    }

    public NomeCittà getCittàRe(){
        return re.getCittà().getNome();
    }

    public Regione getRegione(NomeRegione nomeRegione) throws NoSuchElementException{
        for(Regione regione : regioni){
            if(regione.getNome().equals(nomeRegione))
                return regione;
        }
        throw new NoSuchElementException("Non esiste una regione con il nome indicato");
    }

    public HashSet<Regione> getRegioni() {
        return regioni;
    }

    public void decrementaAiutanti(int numAiutanti) throws IllegalArgumentException { //prende dalla riserva un numero di aiutanti pari a numAiutanti. Lancia un'eccezione se non ci sono abbastanza aiutanti
        if ((this.riservaAiutanti - numAiutanti) < 0)
           throw new IllegalArgumentException("Non ci sono abbastanza aiutanti in riserva");
        this.riservaAiutanti -= numAiutanti;
        updateViewRiservaAiutanti();
    }

    public void aggiungiAiutanti(int numAiutanti) throws IllegalArgumentException { //aggiunge aiutanti alla riserva. Lancia un'eccezione se si cerca di superare il numero
        //massimo consentito
        if((this.riservaAiutanti + numAiutanti) > NUM_AIUTANTI)
            throw new IllegalArgumentException("Numero massimo di aiutanti in riserva superato");
        this.riservaAiutanti += numAiutanti;
        updateViewRiservaAiutanti();
    }

    public int getRiservaAiutanti(){
        return this.riservaAiutanti;
    }

    public void setRe(Re re) {
        if(this.re == null)
            this.re = re;
    }

    public Re getRe(){
        return this.re;
    }

    public void setBalconeDelConsiglioRe(BalconeDelConsiglio balconeDelConsiglioRe){
        if (this.balconeDelConsiglioRe == null)
            this.balconeDelConsiglioRe = balconeDelConsiglioRe;
    }

    public BalconeDelConsiglio getBalconeDelConsiglioRe(){
        return balconeDelConsiglioRe;
    }

    public void setRegioni(HashSet<Regione> regioni) throws IllegalArgumentException {
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
            updateViewBonusPercorsoNobiltà();
        }
    }

    public void addCartePoliticaScartate(ArrayList<CartaPolitica> nuoveCarteScartate){
        nuoveCarteScartate.forEach((CartaPolitica carta) -> cartePoliticaScartate.addCarta(carta));
    }

    public void setMazzoCartePolitica(Mazzo<CartaPolitica> mazzoCartePolitica){
        if(this.mazzoCartePolitica == null)
            this.mazzoCartePolitica = mazzoCartePolitica;
    }

    //pesca una carta politica dal mazzo
    public CartaPolitica ottieniCartaPolitica() {
        if (mazzoCartePolitica.isEmpty()) {
            mazzoCartePolitica = cartePoliticaScartate;
            cartePoliticaScartate = new Mazzo<>();
        }
        return mazzoCartePolitica.ottieniCarta();
    }

    public void setMazzoCartePremioRe(Mazzo<CartaPremioDelRe> mazzoCartePremioRe){
        if(this.mazzoCartePremioRe == null)
            this.mazzoCartePremioRe = mazzoCartePremioRe;
    }

    public CartaPremioDelRe ottieniCartaPremioRe(){
        CartaPremioDelRe cartaPremioDelRe;
        try {
            cartaPremioDelRe = mazzoCartePremioRe.ottieniCarta();
        } catch (NoSuchElementException exc){
            cartaPremioDelRe = null;
        }
        return cartaPremioDelRe;
    }

    public void setCarteBonusColoreCittà(HashSet<CartaBonusColoreCittà> carteBonusColoreCittà) throws IllegalArgumentException  {
        if(carteBonusColoreCittà.size() != NUM_CARTE_BONUS_COLORE_CITTA){
            throw new IllegalArgumentException("Il numero di carte bonus colore città deve essere " + NUM_CARTE_BONUS_COLORE_CITTA);
        }
        else if(this.carteBonusColoreCittà == null)
            this.carteBonusColoreCittà = carteBonusColoreCittà;
    }


    public CartaBonusColoreCittà ottieniCartaBonusColoreCittà(ColoreCittà coloreCittà) throws NoSuchElementException{
        for(CartaBonusColoreCittà carta : carteBonusColoreCittà)
            if (carta.getColore().equals(coloreCittà)) {
                carteBonusColoreCittà.remove(carta);
                return carta;
            }
        throw new NoSuchElementException("Non è disponibile una carta bonus colore città del colore voluto");
    }

    public void setRiservaConsiglieri(ArrayList<Consigliere> riservaConsiglieri) throws IllegalArgumentException {
       if (this.riservaConsiglieri == null){
            this.riservaConsiglieri = riservaConsiglieri;
        }
        updateViewRiservaConsiglieri();
    }

    //restituisce un consigiere del colore scelto preso dalla riserva se disponibile. Altrimenti lancia l'eccezione NoSuchElementException
    public Consigliere ottieniConsigliereDaRiserva(ColoreConsigliere coloreConsigliereDaRestituire) throws NoSuchElementException{
        Consigliere consigliereDaRestituire;
        for(Consigliere consigliere : riservaConsiglieri)
            if(consigliere.getColore().equals(coloreConsigliereDaRestituire)){
                consigliereDaRestituire = consigliere;
                riservaConsiglieri.remove(consigliere);
                updateViewRiservaConsiglieri();
                return consigliereDaRestituire;
            }
         throw new NoSuchElementException("Non esiste in riserva un consigliere del colore indicato");
    }

    public void addConsigliereARiserva(Consigliere consigliere){
        riservaConsiglieri.add(consigliere);
        updateViewRiservaConsiglieri();
    }

    public void addGiocatore(Giocatore giocatore) throws IllegalArgumentException, NumeroMassimoGiocatoriRaggiuntoException {
        if(giocatore == null)
            throw new IllegalArgumentException("giocatore is null");
        if(giocatori.size() == CostantiSistema.NUM_GOCATORI_MAX) {
            throw new NumeroMassimoGiocatoriRaggiuntoException();
        }
        this.giocatori.add(giocatore);
    }

    public ArrayList<Giocatore> getGiocatori(){
        return giocatori;
    }


    private void updateViewRiservaAiutanti(){
        super.notifyViews((InterfacciaView view) -> {
            try {
                view.updateRiservaAiutanti(riservaAiutanti);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateViewRiservaConsiglieri(){
        ArrayList<String> coloriConsiglieri = new ArrayList<>();
        riservaConsiglieri.forEach((Consigliere consigliere) -> coloriConsiglieri.add(consigliere.getColore().toString()));
        super.notifyViews((InterfacciaView view) -> {
            try {
                view.updateRiservaConsiglieri(coloriConsiglieri);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }


    private void updateViewBonusPercorsoNobiltà(){
        super.notifyViews((InterfacciaView view) -> {
            try {
                view.updateBonusPercorsoNobiltà(this.percorsoDellaNobiltà);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
