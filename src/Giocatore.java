import eccezioni.AiutantiNonSufficientiException;
import eccezioni.ImpossibileDecrementareMosseException;
import eccezioni.MoneteNonSufficientiException;

import java.util.Set;

public class Giocatore {
    private static int numeroGiocatori = 0;
    private int idGiocatore;
    private int puntiVittoria;
    private int monete;
    private int aiutanti;
    private int posizionePercorsoNobiltà;
    private int emporiDisponibili;
    private Set<Carta> manoCartePolitica;
    private Set<Carta> manoCartePermessoCostruzione;
    private Set<Carta> manoCartePremioDelRe;
    private Set<Carta> manoCarteBonusRegione;
    private int azioniPrincipaliDisponibili;
    private int azioniVelociDisponibili;

    public Giocatore (int monete, int aiutanti, int posizionePercorsoNobiltà){
        idGiocatore = numeroGiocatori;
        numeroGiocatori ++;
        this.monete = monete;
        this.aiutanti = aiutanti;
        this.posizionePercorsoNobiltà = posizionePercorsoNobiltà;
        emporiDisponibili = 10;
        //TODO carte in mano, pesca 6 carte politica.
    }

    //gestione delle monete
    public int getMonete(){return monete;}

    public void setMonete(int m) throws IllegalArgumentException{
        if (m < 0) throw new IllegalArgumentException("Impossibile assegnare un valore negativo alle monete di un giocatore");
        this.monete = m;
    }

    public void guadagnaMonete(int m) throws IllegalArgumentException{
        if(m <= 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo o nullo a guadagnaMonete(int m)");
        monete = monete + m;
    }

    public void pagaMonete(int m) throws IllegalArgumentException, MoneteNonSufficientiException {
        if(m <= 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo o nullo a pagaMonete(int m)");
        if(monete - m < 0) throw new MoneteNonSufficientiException();
    }

    //gestione numero aiutanti
    public int getAiutanti(){return aiutanti;}

    public void setAiutanti(int a) throws IllegalArgumentException{
        if (a<0) throw new IllegalArgumentException("impossibile assegnare un valore negativo al numero di aiutanti di un giocatore");
        this.aiutanti = a;
    }

    public void guadagnaAiutanti(int a) throws IllegalArgumentException{
        if (a <= 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo o nullo a guadagnaAiutanti(int a)");
        aiutanti = aiutanti + a;
    }

    public void pagaAiutanti(int a) throws IllegalArgumentException, AiutantiNonSufficientiException {
        if (a <= 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo o nullo a pagaAiutanti(int a)");
        if (aiutanti - a < 0) throw new AiutantiNonSufficientiException();
        aiutanti = aiutanti - a;
    }

    //gestione percorso nobiltà
    public int getPosizionePercorsoNobiltà(){return posizionePercorsoNobiltà;}

    public void avanzaPercorsoNobiltà(int n) throws IllegalArgumentException{
        if (n <= 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo o nullo a avanzaPercorsoNobiltà(int n)");
        posizionePercorsoNobiltà = posizionePercorsoNobiltà + n;
    }

    //gestione punti vittoria
    public int getPuntiVittoria(){return puntiVittoria;}

    public void guadagnaPuntiVittoria(int p) throws IllegalArgumentException{
        if (p <= 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo o nullo a guadagnaPuntiVittoria(int p)");
        puntiVittoria = puntiVittoria + p;
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