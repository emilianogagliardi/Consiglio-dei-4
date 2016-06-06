package controller;

import model.Giocatore;
import model.Partita;
import model.bonus.*;
import proxyview.InterfacciaView;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller implements Runnable{
    private Partita partita;
    private ArrayList<InterfacciaView> views;
    private boolean threadSospeso = true;
    int idGiocatoreCorrente = 0;
    Giocatore giocatoreCorrente = partita.getGiocatori().get(idGiocatoreCorrente);


    public Controller(Partita partita, ArrayList<InterfacciaView> views) {
        this.partita = partita;
        this.views = views;
    }

    @Override
    public void run() {


        while(!partitaTerminata()){
            //inizializzo il numero di azioni veloci e principali disponibili
            //TODO: gestione azioni principali e veloci

            //il controller da il consenso al giocatore di iniziare il turno
            views.get(giocatoreCorrente.getId()).eseguiTurno();

            //il giocatore pesca una carta politica
            giocatoreCorrente.addCarta(partita.ottieniCartaPolitica());

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

    public boolean fineTurno(){ //verifica che il giocatore possa finire il turno
        //verifica azioni
        return true;
    }

    public void ottieniBonus(Bonus bonus){
        while (!(bonus instanceof NullBonus)){
            if(bonus instanceof BonusAiutanti) {
                giocatoreCorrente.guadagnaAiutanti(((BonusAiutanti) bonus).getNumeroAiutanti());
            }
            else if (bonus instanceof BonusAvanzaPercorsoNobiltà){
                giocatoreCorrente.avanzaPercorsoNobiltà(((BonusAvanzaPercorsoNobiltà) bonus).getNumeroPosti());
            }
            if (bonus instanceof RealBonus){
                bonus = ((RealBonus) bonus).getDecoratedBonus();
            }
        }
    }
}
