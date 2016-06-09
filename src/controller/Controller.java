package controller;

import model.*;
import model.bonus.*;
import model.carte.CartaPermessoCostruzione;
import model.carte.CartaPolitica;
import model.carte.ColoreCartaPolitica;
import model.eccezioni.AiutantiNonSufficientiException;
import model.eccezioni.EmporioGiàEsistenteException;
import model.eccezioni.MoneteNonSufficientiException;
import proxyView.InterfacciaView;
import server.CostantiSistema;
import server.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Controller implements Runnable, InterfacciaController{
    private Partita partita;
    private ArrayList<InterfacciaView> views;
    private int idGiocatoreCorrente = 0;
    private Giocatore giocatoreCorrente = partita.getGiocatori().get(idGiocatoreCorrente);
    private int azioniPrincipaliDisponibili = 0;
    private boolean azioneVeloceEseguita = false;
    private HashMap<IdBalcone, BalconeDelConsiglio> mappaBalconi;
    GrafoCittà grafoCittà;


    public Controller(Partita partita, ArrayList<InterfacciaView> views) {
        this.partita = partita;
        this.views = views;
        ArrayList<Città> cittàPartita = new ArrayList<>();
        for (Regione regione : partita.getRegioni())
            for (Città città : regione.getCittà())
                cittàPartita.add(città);
        grafoCittà = new GrafoCittà(cittàPartita);
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
                wait(CostantiSistema.TIMEOUT_TURNO);
                //TODO: view.fineTuno();

                //si passa al giocatore successivo
                giocatoreCorrente = prossimoGiocatore(giocatoreCorrente);
            } catch (InterruptedException exc) {
                //do nothing
            }
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
    public void passaTurno(){ //verifica che il giocatore possa finire il turno
        //TODO: verifica azioni
        notify();
    }

    private void assegnaBonus(Bonus bonus) throws IllegalArgumentException {
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
        if(!azionePrincipaleDisponibile())
            return false;
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
        giocatoreCorrente.guadagnaMonete(Costanti.MONETE_GUADAGNATE_ELEGGERE_CONSIGLIERE);
        decrementaAzioniPrincipaliDisponibili();
        return true;
    }


    @Override
    public boolean acquistareTesseraPermessoCostruzione(String nomeBalcone, ArrayList<String> nomiColoriCartePolitica, int numeroCarta) {
        if(!azionePrincipaleDisponibile())
            return false;
        BalconeDelConsiglio balconeDelConsiglio = mappaBalconi.get(IdBalcone.valueOf(nomeBalcone));
        //creo una manoCartePolitica come struttura di supporto
        ArrayList<ColoreCartaPolitica> coloriCartePolitica = new ArrayList<>();
        for(String nomeColoreCartaPolitica : nomiColoriCartePolitica)
            coloriCartePolitica.add(ColoreCartaPolitica.valueOf(nomeColoreCartaPolitica));
        if(balconeDelConsiglio.soddisfaConsiglio(coloriCartePolitica)){
            if (prendiCartePoliticaGiocatore(giocatoreCorrente, coloriCartePolitica)) {
                try {
                    giocatoreCorrente.pagaMonete(moneteDaPagareSoddisfaConsiglio(coloriCartePolitica));
                } catch (MoneteNonSufficientiException exc) {
                    return false;
                }
                CartaPermessoCostruzione cartaPermessoCostruzione;
                switch (numeroCarta) {
                    case 1:
                        cartaPermessoCostruzione = partita.getRegione(NomeRegione.valueOf(nomeBalcone)).ottieniCartaPermessoCostruzione1();
                        break;
                    case 2:
                        cartaPermessoCostruzione = partita.getRegione(NomeRegione.valueOf(nomeBalcone)).ottieniCartaPermessoCostruzione2();
                        break;
                    default:
                        return false;
                }
                assegnaBonus(cartaPermessoCostruzione.getBonus());
                decrementaAzioniPrincipaliDisponibili();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean costruireEmporioConTesseraPermessoCostruzione(CartaPermessoCostruzione cartaPermessoCostruzione, String stringaNomeCittà) {
        NomeCittà nomeCittà = NomeCittà.valueOf(stringaNomeCittà);
        //il giocatore deve avere azioni principali disponibili; la carta permesso costruzione non deve essere coperta; la città passat in input deve essere presenta sulla carta
        //permesso; la carta permesso passata in input deve effettivamente appartenere alla mano carte permesso del giocatore
        if(!(azionePrincipaleDisponibile() && cartaPermessoCostruzione.isVisibile() && cartaPermessoCostruzione.getCittà().contains(nomeCittà)
                && giocatoreCorrente.getManoCartePermessoCostruzione().contains(cartaPermessoCostruzione)))
            return false;
        for(Regione regione : partita.getRegioni()){
            if (regione.getNomiCittà().contains(nomeCittà)) {
                if (!giocatoreCorrente.decrementaEmporiDisponibili()) {
                    return false;
                } else {
                    if (costruisciEmporio(nomeCittà, regione)) {
                        giocatoreCorrente.getManoCartePermessoCostruzione().remove(cartaPermessoCostruzione);
                        cartaPermessoCostruzione.setVisibile(false);
                        giocatoreCorrente.addCarta(cartaPermessoCostruzione);
                        return true;
                    } else return false;
                }
            }
        }
        return false;
    }

    private int moneteDaPagareSoddisfaConsiglio(ArrayList<ColoreCartaPolitica> coloriCartePolitica){
        int numeroCarteJolly = 0;
        int monete;
        for (ColoreCartaPolitica coloreCartaPolitica : coloriCartePolitica)
            if(coloreCartaPolitica.equals(ColoreCartaPolitica.JOLLY))
                numeroCarteJolly++;
        switch (coloriCartePolitica.size()) {
            case 1:
                monete = Costanti.MONETE_1_CARTA_POLITICA;
                break;
            case 2:
                monete = Costanti.MONETE_2_CARTE_POLITICA;
                break;
            case 3:
                monete = Costanti.MONETE_3_CARTE_POLITICA;
                break;
            default:
                monete = 0;
                break;
        }
        return monete + numeroCarteJolly * Costanti.MONETE_PER_CARTA_JOLLY;

    }

    private boolean decrementaAzioniPrincipaliDisponibili() {
        if ((azioniPrincipaliDisponibili - 1) >= 0) {
            azioniPrincipaliDisponibili--;
            return true;
        } else {
            return false;
        }
    }

    private boolean azionePrincipaleDisponibile(){
        if (azioniPrincipaliDisponibili > 0)
            return true;
        else return false;
    }

    private boolean prendiCartePoliticaGiocatore(Giocatore giocatore, ArrayList<ColoreCartaPolitica> coloriCartePolitica){
        ArrayList<Colore> arrayListManoColoriCartePolitica = new ArrayList<>();
        for(CartaPolitica cartaPolitica : giocatore.getManoCartePolitica())
            arrayListManoColoriCartePolitica.add(cartaPolitica.getColore().toColore());
        HashMap<Colore, Integer> mappaColoriManoCartePoliticaGiocatore = Utility.arrayListToHashMap(arrayListManoColoriCartePolitica);
        HashMap<Colore, Integer> mappaColoriCartePolitica = Utility.arrayListToHashMap(ColoreCartaPolitica.toColore(coloriCartePolitica));
        if(Utility.hashMapContainsAllWithDuplicates(mappaColoriManoCartePoliticaGiocatore, mappaColoriCartePolitica)){
            ArrayList<CartaPolitica> cartePoliticaScartate = giocatore.scartaCartePolitica(coloriCartePolitica);
            cartePoliticaScartate.forEach((cartaPolitica) -> cartaPolitica.setVisibile(false));
            partita.addCartePoliticaScartate(cartePoliticaScartate);
            return true;
        }
        return false;
    }

    private boolean costruisciEmporio(NomeCittà nomeCittàCostruzione, Regione regione){
        Città cittàCostruzione = regione.getCittàSingola(nomeCittàCostruzione);
        ArrayList<Città> cittàCollegateRitornate;
        try {
            cittàCostruzione.costruisciEmporio(new Emporio(giocatoreCorrente.getId()));
            if(cittàCostruzione.giàCostruito(giocatoreCorrente))
                return false;
            try {
                giocatoreCorrente.pagaAiutanti(Costanti.NUMERO_AIUTANTI_PAGARE_EMPORIO * cittàCostruzione.getNumeroEmporiCostruiti());
                assegnaBonus(cittàCostruzione.getBonus());
                //ora utilizzo un algortimo di esplorazione dei grafi per ricevere i bonus delle città adiacenti dove è presente un emporio del giocatore corrente
                cittàCollegateRitornate = grafoCittà.bfs(cittàCostruzione, (cittàAdiacente, cittàCollegate) -> {
                    if (cittàAdiacente.giàCostruito(giocatoreCorrente)) {
                        cittàCollegate.add(cittàAdiacente);
                    } else {
                        cittàAdiacente.setFlag(true); //stoppo l'esplorazione attraverso questa città
                    }
                });
                for (Città città : cittàCollegateRitornate)
                        assegnaBonus(città.getBonus());
                //TODO: verificare se al giocatore spetta una carta bonus colore
                return true;
            } catch (AiutantiNonSufficientiException exc){
                return false;
            }
        } catch (EmporioGiàEsistenteException exc) {
            return false;
        }

    }
}



