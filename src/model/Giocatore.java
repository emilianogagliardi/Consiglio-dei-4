package model;
import model.carte.*;
import model.eccezioni.AiutantiNonSufficientiException;
import model.eccezioni.ImpossibileDecrementareMosseException;
import model.eccezioni.MoneteNonSufficientiException;

import java.util.ArrayList;

public class Giocatore {
    private int idGiocatore;
    private int puntiVittoria;
    private int monete;
    private int aiutanti;
    private int posizionePercorsoNobiltà;
    private int emporiDisponibili;
    private ArrayList<CartaPolitica> manoCartePolitica;
    private ArrayList<CartaPermessoCostruzione> manoCartePermessoCostruzione;
    private ArrayList<CartaPremioDelRe> manoCartePremioDelRe;
    private ArrayList<CartaBonusRegione> manoCarteBonusRegione;
    private int azioniPrincipaliDisponibili;
    private int azioniVelociDisponibili;

    public Giocatore (int id, int monete, int aiutanti, int posizionePercorsoNobiltà){
        idGiocatore = id;
        guadagnaMonete(monete);
        guadagnaAiutanti(aiutanti);
        avanzaPercorsoNobiltà(posizionePercorsoNobiltà);
        emporiDisponibili = 10;
        //TODO carte in mano, pesca 6 carte politica.
    }

    //gestione delle monete
    public int getMonete(){return monete;}

    public void guadagnaMonete(int m) throws IllegalArgumentException{
        if(m < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a guadagnaMonete(int m)");
        monete = monete + m;
        //TODO gestione overflow
    }

    public void pagaMonete(int m) throws IllegalArgumentException, MoneteNonSufficientiException {
        if(m < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a pagaMonete(int m)");
        if(monete - m < 0) throw new MoneteNonSufficientiException();
    }

    //gestione numero aiutanti
    public int getAiutanti(){return aiutanti;}

    public void guadagnaAiutanti(int a) throws IllegalArgumentException{
        if (a < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a guadagnaAiutanti(int a)");
        aiutanti = aiutanti + a;
        //TODO decremementare gli aiutanti nel pool dei disponibili
    }

    public void pagaAiutanti(int a) throws IllegalArgumentException, AiutantiNonSufficientiException {
        if (a < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a pagaAiutanti(int a)");
        if (aiutanti - a < 0) throw new AiutantiNonSufficientiException();
        aiutanti = aiutanti - a;
        //TODO incrementare gli aiutanti nel pool di aiutanti disponibili
    }

    //gestione percorso nobiltà
    public int getPosizionePercorsoNobiltà(){return posizionePercorsoNobiltà;}

    public void avanzaPercorsoNobiltà(int n) throws IllegalArgumentException{
        if (n < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a avanzaPercorsoNobiltà(int n)");
        posizionePercorsoNobiltà = posizionePercorsoNobiltà + n;
        //TODO gestione overflow
    }

    //gestione punti vittoria
    public int getPuntiVittoria(){return puntiVittoria;}

    public void guadagnaPuntiVittoria(int p) throws IllegalArgumentException{
        if (p < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a guadagnaPuntiVittoria(int p)");
        puntiVittoria = puntiVittoria + p;
        //TODO gestione overflow
    }

    //gestione numero di azioni principali
    public int getAzioniPrincipaliDisponibili(){return azioniPrincipaliDisponibili;}

    public void incrementaAzioniPrincipaliDisponibili(){
        azioniPrincipaliDisponibili++;
    }

    public void decrementaAzioniPrincipaliDisponibili() throws ImpossibileDecrementareMosseException {
        if (azioniPrincipaliDisponibili == 0) throw new ImpossibileDecrementareMosseException();
        azioniPrincipaliDisponibili--;
    }

    //gestione numero di azioni veloci
    public int getAzioniVelociDisponibili(){return azioniVelociDisponibili;}

    public void decrementaAzioniVelociDisponibili() throws ImpossibileDecrementareMosseException{
        if (azioniVelociDisponibili == 0) throw new ImpossibileDecrementareMosseException();
        azioniVelociDisponibili--;
    }

    //TODO gestione delle carte del player
    public void pescaCartePolitica(int numeroCarte){};
}