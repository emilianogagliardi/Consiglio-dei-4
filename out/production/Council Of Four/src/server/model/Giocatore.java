package server.model;
import classicondivise.bonus.Bonus;
import classicondivise.bonus.BonusPuntiVittoria;
import classicondivise.carte.Carta;
import classicondivise.carte.CartaPermessoCostruzione;
import server.model.carte.*;
import server.model.eccezioni.AiutantiNonSufficientiException;
import server.model.eccezioni.MoneteNonSufficientiException;
import interfaccecondivise.InterfacciaView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Giocatore extends Observable {
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

    public Giocatore (int id, int monete, int aiutanti, ArrayList<InterfacciaView> views){
        super (views);
        idGiocatore = id;
        guadagnaMonete(monete);
        guadagnaAiutanti(aiutanti);
        emporiDisponibili = CostantiModel.NUM_EMPORI_GIOCATORE;
        manoCartePolitica = new ArrayList<CartaPolitica>();
        manoCarteBonusRegione = new ArrayList<CartaBonusRegione>();
        manoCartePremioDelRe = new ArrayList<CartaPremioDelRe>();
        manoCartePermessoCostruzione = new ArrayList<CartaPermessoCostruzione>(); //è utilizzabile se isVisible
        manoCarteBonusColoreCittà = new ArrayList<CartaBonusColoreCittà>();
        updateViewMonete();
        updateViewAiutanti();
        updateViewPuntiVittoria();
        updateViewPercorsoNobiltà();
        updateViewEmporiDisponibili();
        updateViewCartePolitica();
        //updateViewCarteBonusRegione();
        //updateViewCarteBonusColoreCittà();
        //updateViewCartePremioRe();
        updateViewCartePermessoCostruzione();
    }

    //gestione delle monete
    public int getMonete(){return monete;}

    public void guadagnaMonete(int m) throws IllegalArgumentException{
        if(m < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a guadagnaMonete(int m)");
        monete = (monete + m);
        if (monete > CostantiModel.MAX_MONETE) {
            monete = CostantiModel.MAX_MONETE;
        }
        updateViewMonete();
    }

    public void pagaMonete(int m) throws IllegalArgumentException, MoneteNonSufficientiException {
        if(m < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a pagaMonete(int m)");
        if(monete - m < 0) throw new MoneteNonSufficientiException();
        monete = monete - m;
        updateViewMonete();
    }

    //gestione numero aiutanti
    public int getAiutanti(){return aiutanti;}

    public void guadagnaAiutanti(int a) throws IllegalArgumentException{
        if (a < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a guadagnaAiutanti(int a)");
        aiutanti = aiutanti + a;
        updateViewAiutanti();
    }

    public void pagaAiutanti(int a) throws IllegalArgumentException, AiutantiNonSufficientiException {
        if (a < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a pagaAiutanti(int a)");
        if (aiutanti - a < 0) throw new AiutantiNonSufficientiException();
        aiutanti = aiutanti - a;
        updateViewAiutanti();
    }

    //gestione percorso nobiltà
    public int getPosizionePercorsoNobiltà(){return posizionePercorsoNobiltà;}

    public void avanzaPercorsoNobiltà(int n) throws IllegalArgumentException{
        if (n < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a avanzaPercorsoNobiltà(int n)");
        posizionePercorsoNobiltà = posizionePercorsoNobiltà + n;
        if (posizionePercorsoNobiltà > CostantiModel.MAX_POS_NOBILTA) {
            posizionePercorsoNobiltà = CostantiModel.MAX_POS_NOBILTA;
        }
        updateViewPercorsoNobiltà();
    }

    //gestione punti vittoria
    public int getPuntiVittoria(){return puntiVittoria;}

    public void guadagnaPuntiVittoria(int p) throws IllegalArgumentException{
        if (p < 0) throw new IllegalArgumentException("Non è possibile assegnare un valore negativo a guadagnaPuntiVittoria(int p)");
        puntiVittoria = puntiVittoria + p;
        if (puntiVittoria > CostantiModel.MAX_PUNTI_VITTORIA) {
            puntiVittoria = CostantiModel.MAX_PUNTI_VITTORIA;
        }
        updateViewPuntiVittoria();
    }


    public void addCarta(Carta c) throws IllegalArgumentException{
        if (c instanceof CartaPolitica){
            manoCartePolitica.add((CartaPolitica) c);
            updateViewCartePolitica();
        }
        else if(c instanceof CartaBonusColoreCittà) {
            manoCarteBonusColoreCittà.add((CartaBonusColoreCittà) c);
            //updateViewCarteBonusColoreCittà();
        }
        else if (c instanceof CartaPermessoCostruzione) {
            manoCartePermessoCostruzione.add((CartaPermessoCostruzione) c);
            updateViewCartePermessoCostruzione();
        }
        else if (c instanceof CartaBonusRegione) {
            manoCarteBonusRegione.add((CartaBonusRegione) c);
            //updateViewCarteBonusRegione();
        }
        else if (c instanceof CartaPremioDelRe) {
            manoCartePremioDelRe.add((CartaPremioDelRe) c);
            //updateViewCartePremioRe();
        }
        else throw new IllegalArgumentException();
    }

    public ArrayList<CartaPolitica> scartaCartePolitica(List<ColoreCartaPolitica> colori){
        ArrayList<CartaPolitica> carteScartate = new ArrayList<>();
        for (ColoreCartaPolitica colore : colori) {
            for (CartaPolitica carta : manoCartePolitica) {
                if (colore.equals(carta.getColore())) {
                    carteScartate.add(carta);
                    manoCartePolitica.remove(carta);
                    break;
                }
            }
        }
        updateViewCartePolitica();
        return carteScartate;
    }


    public int getId() {
        return idGiocatore;
    }

    public int getEmporiDisponibili() {
        return emporiDisponibili;
    }

    public boolean decrementaEmporiDisponibili(){
        if (emporiDisponibili - 1 < 0) {
            return false;
        } else {
            --emporiDisponibili;
            return true;
        }
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


    //update view
    private void updateViewMonete(){
        super.notifyViews((InterfacciaView v) -> {
            try {
                v.updateMonete(getId(), getMonete());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateViewAiutanti(){
        super.notifyViews((InterfacciaView v) -> {
            try {
                v.updateAiutanti(getId(), getAiutanti());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateViewCartePolitica(){
        super.notifyViews((InterfacciaView v) -> {
            ArrayList<String> colori = new ArrayList<>();
            manoCartePolitica.stream().forEach((CartaPolitica c) -> colori.add(c.getColore().toString()));
            try {
                if (v.getIdGiocatore() == getId())  v.updateCartePoliticaProprie(colori);
                else v.updateCartePoliticaAvversari(getId(), getManoCartePolitica().size());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateViewEmporiDisponibili(){
        super.notifyViews((InterfacciaView v) -> {
            try {
                v.updateEmporiDisponibiliGiocatore(getId(), getEmporiDisponibili());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateViewPuntiVittoria(){
        super.notifyViews((InterfacciaView v) -> {
            try {
                v.updatePuntiVittoriaGiocatore(getId(), getPuntiVittoria());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateViewPercorsoNobiltà(){
        super.notifyViews((InterfacciaView v) -> {
            try {
                v.updatePercorsoNobiltà(getId(), getPosizionePercorsoNobiltà());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    /*private void updateViewCarteBonusRegione() {
        HashMap<String, Integer> mapCarte = new HashMap<>();
        manoCarteBonusRegione.forEach((CartaBonusRegione carta) -> {
            Bonus bonus = carta.getBonus();
            int punti;
            try{
                BonusPuntiVittoria bonusPuntiVittoria = (BonusPuntiVittoria) bonus;
                punti = bonusPuntiVittoria.getPuntiVittoria();
            }catch (ClassCastException e) {
                System.out.println("la carta bonus regione non ha bonus punti vittoria, impossibile eseguire cast");
                punti = 0;
            }
            mapCarte.put(carta.getNomeRegione().toString(), punti);
        });
        super.notifyViews((InterfacciaView v) -> {
            try {
                v.updateCarteBonusRegioneGiocatore(getId(), mapCarte);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }*/

    /*private void updateViewCarteBonusColoreCittà (){
        HashMap<String, Integer> mapCarte = new HashMap<>();
        manoCarteBonusColoreCittà.forEach((CartaBonusColoreCittà carta) -> {
            Bonus bonus = carta.getBonus();
            int punti;
            try{
                BonusPuntiVittoria bonusPuntiVittoria = (BonusPuntiVittoria) bonus;
                punti = bonusPuntiVittoria.getPuntiVittoria();
            }catch (ClassCastException e) {
                System.out.println("la carta bonus colore città non ha bonus punti vittoria, impossibile eseguire cast");
                punti = 0;
            }
            mapCarte.put(carta.getColore().toString(), punti);
        });
        super.notifyViews((InterfacciaView v) -> {
            try {
                v.updateCarteBonusColoreCittàGiocatore(getId(), mapCarte);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }*/

    /*private void updateViewCartePremioRe(){
        HashMap<String, Integer> mapCarte = new HashMap<>();
        manoCartePremioDelRe.forEach((CartaPremioDelRe carta) -> {
            Bonus bonus = carta.getBonus();
            int punti;
            try{
                BonusPuntiVittoria bonusPuntiVittoria = (BonusPuntiVittoria) bonus;
                punti = bonusPuntiVittoria.getPuntiVittoria();
            }catch (ClassCastException e) {
                System.out.println("la carta premio del re non ha bonus punti vittoria, impossibile eseguire cast");
                punti = 0;
            }
            mapCarte.put(((Integer)carta.getNumeroOrdine()).toString(), punti);
        });
       super.notifyViews((InterfacciaView v) -> {
           try {
               v.updateCarteBonusReGiocatore(getId(), mapCarte);
           } catch (RemoteException e) {
               e.printStackTrace();
           }
       });
    }*/

    private void updateViewCartePermessoCostruzione() {
        super.notifyViews((InterfacciaView v) -> {
            try {
                v.updateCartePermessoGiocatore(getId(), manoCartePermessoCostruzione);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}