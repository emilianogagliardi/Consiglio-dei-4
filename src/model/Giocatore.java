package model;
import model.bonus.Bonus;
import model.carte.*;
import model.eccezioni.AiutantiNonSufficientiException;
import model.eccezioni.ImpossibileDecrementareMosseException;
import model.eccezioni.MoneteNonSufficientiException;
import model.Costanti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Giocatore {
    private int idGiocatore;
    private int puntiVittoria;
    private int monete;
    private int aiutanti;
    private int posizionePercorsoNobiltà;
    private int emporiDisponibili;
    private List<CartaPolitica> manoCartePolitica;
    private List<CartaPremioDelRe> manoCartePremioDelRe;
    private List<CartaBonusRegione> manoCarteBonusRegione;
    private List<CartaBonusColoreCittà> manoCarteBonusColoreCittà;
    private List<CartaPermessoCostruzione> manoCartePermessoCostruzione;
    private List<CartaPermessoCostruzione> cartePermessoCostruzioneCoperte;
    private int azioniPrincipaliDisponibili;
    private int azioniVelociDisponibili;

    public Giocatore (int id, int monete, int aiutanti){
        idGiocatore = id;
        guadagnaMonete(monete);
        guadagnaAiutanti(aiutanti);
        emporiDisponibili = Costanti.NUM_EMPORI_GIOCATORE;
        manoCartePolitica = new ArrayList<CartaPolitica>();
        manoCarteBonusRegione = new ArrayList<CartaBonusRegione>();
        manoCartePremioDelRe = new ArrayList<CartaPremioDelRe>();
        manoCartePermessoCostruzione = new ArrayList<CartaPermessoCostruzione>();
        manoCarteBonusColoreCittà = new ArrayList<CartaBonusColoreCittà>();
        cartePermessoCostruzioneCoperte = new ArrayList<CartaPermessoCostruzione>();
    }

    //gestione delle monete
    public int getMonete(){return monete;}

    public void guadagnaMonete(int m) throws IllegalArgumentException{
        if(m < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a guadagnaMonete(int m)");
        monete = (monete + m);
        if (monete > Costanti.MAX_MONETE) {
            monete = Costanti.MAX_MONETE;
        }
    }

    public void pagaMonete(int m) throws IllegalArgumentException, MoneteNonSufficientiException {
        if(m < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a pagaMonete(int m)");
        if(monete - m < 0) throw new MoneteNonSufficientiException();
        monete = monete - m;
    }

    //gestione numero aiutanti
    public int getAiutanti(){return aiutanti;}

    public void guadagnaAiutanti(int a) throws IllegalArgumentException{
        if (a < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a guadagnaAiutanti(int a)");
        aiutanti = aiutanti + a;
    }

    public void pagaAiutanti(int a) throws IllegalArgumentException, AiutantiNonSufficientiException {
        if (a < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a pagaAiutanti(int a)");
        if (aiutanti - a < 0) throw new AiutantiNonSufficientiException();
        aiutanti = aiutanti - a;
    }

    //gestione percorso nobiltà
    public int getPosizionePercorsoNobiltà(){return posizionePercorsoNobiltà;}

    public void avanzaPercorsoNobiltà(int n) throws IllegalArgumentException{
        if (n < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a avanzaPercorsoNobiltà(int n)");
        posizionePercorsoNobiltà = posizionePercorsoNobiltà + n;
        if (posizionePercorsoNobiltà > Costanti.MAX_POS_NOBILTA) {
            posizionePercorsoNobiltà = Costanti.MAX_POS_NOBILTA;
        }
    }

    //gestione punti vittoria
    public int getPuntiVittoria(){return puntiVittoria;}

    public void guadagnaPuntiVittoria(int p) throws IllegalArgumentException{
        if (p < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a guadagnaPuntiVittoria(int p)");
        puntiVittoria = puntiVittoria + p;
        if (puntiVittoria > Costanti.MAX_PUNTI_VITTORIA) {
            puntiVittoria = Costanti.MAX_PUNTI_VITTORIA;
        }
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


    public void ottieniBonus(Bonus b) {
        b.ottieni(this);
    }

    public void addCarta(Carta c) throws IllegalArgumentException{
        if (c instanceof CartaPolitica){
            manoCartePolitica.add((CartaPolitica) c);
        }
        else if(c instanceof CartaBonusColoreCittà) {
            manoCarteBonusColoreCittà.add((CartaBonusColoreCittà) c);
        }
        else if (c instanceof CartaPermessoCostruzione) {
            manoCartePermessoCostruzione.add((CartaPermessoCostruzione) c);
        }
        else if (c instanceof CartaBonusRegione) {
            manoCarteBonusRegione.add((CartaBonusRegione) c);
        }
        else if (c instanceof CartaPremioDelRe) {
            manoCartePremioDelRe.add((CartaPremioDelRe) c);
        }
        else throw new IllegalArgumentException();
    }

    public void scartaCartePolitica(CartaPolitica... carte) throws IllegalArgumentException{
        List<CartaPolitica> carteDaScartare = Arrays.asList(carte);
        if (!manoCartePolitica.containsAll(carteDaScartare)) throw new IllegalArgumentException();
        for (CartaPolitica c_scarta : carteDaScartare) {
            for (CartaPolitica c_mano : manoCartePolitica) {
                if (c_scarta.equals(c_mano)) {
                    manoCartePolitica.remove(c_mano);
                    break;
                }
            }
        }
    }

    public void copriCartaPermessoCostruzione(CartaPermessoCostruzione c)throws IllegalArgumentException{
        if (!manoCartePermessoCostruzione.contains(c)) throw new IllegalArgumentException("carta permesso non presente nella mano del giocatore, impossibile coprirla");
        for (CartaPermessoCostruzione cp : manoCartePermessoCostruzione) {
            if (cp.equals(c)) {
                manoCartePermessoCostruzione.remove(cp);
                cartePermessoCostruzioneCoperte.add(cp);
                break;
            }
        }
    }

    public int getId() {
        return idGiocatore;
    }

    public int getEmporiDisponibili() {
        return emporiDisponibili;
    }

    public List<CartaPolitica> getManoCartePolitica() {
        return manoCartePolitica;
    }

    public List<CartaPermessoCostruzione> getManoCartePermessoCostruzione() {
        return manoCartePermessoCostruzione;
    }

    public List<CartaPremioDelRe> getManoCartePremioDelRe() {
        return manoCartePremioDelRe;
    }

    public List<CartaBonusRegione> getManoCarteBonusRegione() {
        return manoCarteBonusRegione;
    }

    public List<CartaBonusColoreCittà> getManoCarteBonusColoreCittà() {
        return manoCarteBonusColoreCittà;
    }

    public List<CartaPermessoCostruzione> getCartePermessoCostruzioneCoperte() {
        return cartePermessoCostruzioneCoperte;
    }
}