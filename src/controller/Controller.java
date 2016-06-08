package controller;

import model.*;
import model.bonus.*;
import model.carte.CartaPolitica;
import model.carte.ColoreCartaPolitica;
import proxyView.InterfacciaView;
import server.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Controller implements Runnable, InterfacciaController{
    private Partita partita;
    private ArrayList<InterfacciaView> views;
    private boolean threadSospeso = true;
    private int idGiocatoreCorrente = 0;
    private Giocatore giocatoreCorrente = partita.getGiocatori().get(idGiocatoreCorrente);
    private int azioniPrincipaliDisponibili = 0;
    private boolean azioneVeloceEseguita = false;
    private HashMap<IdBalcone, BalconeDelConsiglio> mappaBalconi;


    public Controller(Partita partita, ArrayList<InterfacciaView> views) {
        this.partita = partita;
        this.views = views;
    }

    @Override
    public void run() {
        //creo una mappaBalconi come struttura di supporto
        mappaBalconi.put(IdBalcone.COSTA, partita.getRegione(NomeRegione.valueOf(IdBalcone.COSTA.toString())).getBalconeDelConsiglio());
        mappaBalconi.put(IdBalcone.COLLINA, partita.getRegione(NomeRegione.valueOf(IdBalcone.COLLINA.toString())).getBalconeDelConsiglio());
        mappaBalconi.put(IdBalcone.MONTAGNA, partita.getRegione(NomeRegione.valueOf(IdBalcone.MONTAGNA.toString())).getBalconeDelConsiglio());
        mappaBalconi.put(IdBalcone.RE, partita.getBalconeDelConsiglioRe());

        //inizio il ciclo dei turni
        while(!partitaTerminata()){
            azioniPrincipaliDisponibili = 1;
            azioneVeloceEseguita = false;

            //il giocatore pesca una carta politica
            giocatoreCorrente.addCarta(partita.ottieniCartaPolitica());

            //il controller da il consenso al giocatore di iniziare il turno
            views.get(giocatoreCorrente.getId()).eseguiTurno();

            //il controller aspetta che il giocatore abbia finito il turno
            try {
                while(threadSospeso)
                    wait();
            } catch (InterruptedException exc) {
                //do nothing
            }

            //si passa al giocatore successivo
            giocatoreCorrente = prossimoGiocatore(giocatoreCorrente);
        }


    }

    private boolean emporiDisponibili(Giocatore giocatore){
        return giocatore.getEmporiDisponibili() > 0;
    }

    private Giocatore prossimoGiocatore(Giocatore giocatoreCorrente){
        int idGiocatoreCorrente = giocatoreCorrente.getId();
        if(idGiocatoreCorrente == partita.getGiocatori().size())
            return partita.getGiocatori().get(0);
        return partita.getGiocatori().get(++idGiocatoreCorrente);
    }

    private boolean partitaTerminata(){ //cicla sui giocatori per capire se qualcuno ha terminato i propri empori disponibili
        for(Giocatore giocatore : partita.getGiocatori())
            if (!emporiDisponibili(giocatore))
                return true;
        return false;
    }

    @Override
    public boolean fineTurno(){ //verifica che il giocatore possa finire il turno
        //verifica azioni
        return true;
    }

    @Override
    public void ottieniBonus(Bonus bonus) throws IllegalArgumentException {
        while (!(bonus instanceof NullBonus)){
            if(bonus instanceof BonusAiutanti) {
                giocatoreCorrente.guadagnaAiutanti(((BonusAiutanti) bonus).getNumeroAiutanti());
            }
            else if (bonus instanceof BonusAvanzaPercorsoNobiltà){
                giocatoreCorrente.avanzaPercorsoNobiltà(((BonusAvanzaPercorsoNobiltà) bonus).getNumeroPosti());
            }
            else if(bonus instanceof BonusMonete){
                giocatoreCorrente.guadagnaMonete(((BonusMonete) bonus).getNumeroMonete());
            }
            else if(bonus instanceof BonusPescaCartaPolitica){
                for (int i = 0; i < ((BonusPescaCartaPolitica) bonus).getNumeroCarte(); i++)
                    giocatoreCorrente.addCarta(partita.ottieniCartaPolitica());
            }
            else if(bonus instanceof BonusPuntiVittoria){
                giocatoreCorrente.guadagnaPuntiVittoria(((BonusPuntiVittoria) bonus).getPuntiVittoria());
            }
            else if(bonus instanceof BonusRipetiAzionePrincipale){
                azioniPrincipaliDisponibili++;
            }
            else throw new IllegalArgumentException("Bonus non previsto"); //non si dovrebbe mai arrivare in questo branch else, se succede significa che è stato passato in ingresso un Bonus non previsto
            bonus = ((RealBonus) bonus).getDecoratedBonus();
        }
    }

    @Override
    public boolean eleggereConsigliere(String nomeBalcone, String coloreConsigliere) {
        Consigliere consigliereDaInserireInBalcone, consigliereDaInserireInRiserva;
        BalconeDelConsiglio balcone;
        try{
            consigliereDaInserireInBalcone = partita.ottieniConsigliereDaRiserva(ColoreConsigliere.valueOf(coloreConsigliere));
        } catch (NoSuchElementException exc){
            return false;
        }
        balcone = mappaBalconi.get(IdBalcone.valueOf(nomeBalcone));
        if(balcone == null)
            return false;
        consigliereDaInserireInRiserva = balcone.addConsigliere(consigliereDaInserireInBalcone);
        partita.addConsigliereARiserva(consigliereDaInserireInRiserva);
        return true;
    }


    @Override
    public boolean acquistareTesseraPermessoCostruzione(String nomeBalcone, ArrayList<String> nomiColoriCartePolitica, String nomeRegione, int carta) {
        BalconeDelConsiglio balconeDelConsiglio = mappaBalconi.get(IdBalcone.valueOf(nomeBalcone));
        //creo una manoCartePolitica come struttura di supporto
        ArrayList<ColoreCartaPolitica> coloriCartePolitica = new ArrayList<>();
        for(String nomeColoreCartaPolitica : nomiColoriCartePolitica)
            coloriCartePolitica.add(ColoreCartaPolitica.valueOf(nomeColoreCartaPolitica));
        if(balconeDelConsiglio.soddisfaConsiglio(coloriCartePolitica)){
            return prendiCartePoliticaGiocatore(giocatoreCorrente, coloriCartePolitica);
        }
        return false;
    }

    private boolean prendiCartePoliticaGiocatore(Giocatore giocatore, ArrayList<ColoreCartaPolitica> coloriCartePolitica){
        ArrayList<Colore> arrayListManoColoriCartePolitica = new ArrayList<>();
        for(CartaPolitica cartaPolitica : giocatore.getManoCartePolitica())
            arrayListManoColoriCartePolitica.add(cartaPolitica.getColore().toColore());
        HashMap<Colore, Integer> mappaColoriManoCartePoliticaGiocatore = Utility.arrayListToHashMap(arrayListManoColoriCartePolitica);
        HashMap<Colore, Integer> mappaColoriCartePolitica = Utility.arrayListToHashMap(ColoreCartaPolitica.toColore(coloriCartePolitica));
        if(Utility.hashMapContainsAllWithDuplicates(mappaColoriManoCartePoliticaGiocatore, mappaColoriCartePolitica)){
            partita.addCartePoliticaScartate(giocatore.scartaCartePolitica(coloriCartePolitica));
            return true;
        }
        return false;
    }




}



