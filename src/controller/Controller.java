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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class Controller implements Runnable, InterfacciaController{
    private Partita partita;
    private ArrayList<InterfacciaView> views;
    private Giocatore giocatoreCorrente;
    private int azioniPrincipaliDisponibili;
    private boolean azioneVeloceEseguita;
    private HashMap<IdBalcone, BalconeDelConsiglio> mappaBalconi;
    private GrafoCittà grafoCittà;


    public Controller(Partita partita, ArrayList<InterfacciaView> views) {
        this.partita = partita;
        this.views = views;
        this.giocatoreCorrente = partita.getGiocatori().get(0);
        azioniPrincipaliDisponibili = 0;
        azioneVeloceEseguita= false;
        //creo una mappaBalconi come struttura di supporto
        mappaBalconi = new HashMap<>();
        mappaBalconi.put(IdBalcone.COSTA, partita.getRegione(NomeRegione.valueOf(IdBalcone.COSTA.toString())).getBalconeDelConsiglio());
        mappaBalconi.put(IdBalcone.COLLINA, partita.getRegione(NomeRegione.valueOf(IdBalcone.COLLINA.toString())).getBalconeDelConsiglio());
        mappaBalconi.put(IdBalcone.MONTAGNA, partita.getRegione(NomeRegione.valueOf(IdBalcone.MONTAGNA.toString())).getBalconeDelConsiglio());
        mappaBalconi.put(IdBalcone.RE, partita.getBalconeDelConsiglioRe());
        //creazione del grafo delle città che vuole in input la lista di tutte le città
        ArrayList<Città> cittàPartita = new ArrayList<>();
        for (Regione regione : partita.getRegioni())
                cittàPartita.addAll(regione.getCittà());
        grafoCittà = new GrafoCittà(cittàPartita);

    }

    @Override
    public void run() {
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
                exc.printStackTrace();
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
    public boolean eleggereConsigliere(String nomeBalcone, String coloreConsigliereDaRiserva) {
        if(!azionePrincipaleDisponibile())
            return false;
        Consigliere consigliereDaInserireInBalcone, consigliereDaInserireInRiserva;
        BalconeDelConsiglio balcone;
        try{
            consigliereDaInserireInBalcone = partita.ottieniConsigliereDaRiserva(ColoreConsigliere.valueOf(coloreConsigliereDaRiserva));
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
    public boolean acquistareTesseraPermessoCostruzione(String nomeBalcone, List<String> nomiColoriCartePolitica, int numeroCarta) {
        Supplier<Boolean> supplier = () -> {
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
            giocatoreCorrente.addCarta(cartaPermessoCostruzione);
            assegnaBonus(cartaPermessoCostruzione.getBonus());
            return true;
        };
        return acquistareTesseraPermesso(nomeBalcone, nomiColoriCartePolitica, supplier);
    }



    private boolean acquistareTesseraPermesso(String nomeBalcone, List<String> nomiColoriCartePolitica, Supplier<Boolean> supplier){
        if(!azionePrincipaleDisponibile())
            return false;
        BalconeDelConsiglio balconeDelConsiglio = mappaBalconi.get(IdBalcone.valueOf(nomeBalcone));
        //creo una mano di colori carte  politica come struttura di supporto
        List<ColoreCartaPolitica> coloriCartePolitica = nomiColoriCartePolitica.stream().map(ColoreCartaPolitica::valueOf).collect(Collectors.toList());
        if(balconeDelConsiglio.soddisfaConsiglio(coloriCartePolitica)){
            if (prendiCartePoliticaGiocatore(giocatoreCorrente, coloriCartePolitica)) {
                try {
                    giocatoreCorrente.pagaMonete(moneteDaPagareSoddisfaConsiglio(coloriCartePolitica));
                } catch (MoneteNonSufficientiException exc) {
                    return false;
                }
                if (!supplier.get()) {
                    return false;
                }
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
        if (!giocatoreCorrente.decrementaEmporiDisponibili()) {
            return false;
        } else {
            if (costruisciEmporio(nomeCittà)) {
                giocatoreCorrente.getManoCartePermessoCostruzione().remove(cartaPermessoCostruzione);
                cartaPermessoCostruzione.setVisibile(false);
                giocatoreCorrente.addCarta(cartaPermessoCostruzione);
                return true;
            } else return false;
        }
    }

    @Override
    public boolean costruireEmporioConAiutoRe(List<String> nomiColoriCartePolitica, String nomeCittàCostruzione) {
        if (!acquistareTesseraPermesso("RE", nomiColoriCartePolitica, () -> true)){
            return false;
        }
        grafoCittà.bfs(getCittàDaNome(partita.getCittàRe()), (p1, p2) -> {});
        Città cittàCostruzione = getCittàDaNome(NomeCittà.valueOf(nomeCittàCostruzione));
        Integer distanza = cittàCostruzione.getDistanza();
        if (distanza.equals(Integer.MAX_VALUE)){
            return false; //la città scelta non è collegata a quella dove risiede attualmente il Re
        }
        try {
            giocatoreCorrente.pagaMonete(distanza * Costanti.MONETE_PER_STRADA);
        } catch (MoneteNonSufficientiException exc){
            return false;
        }
        if (!costruisciEmporio(cittàCostruzione.getNome())) {
            return false;
        }
        return true;
    }


    private int moneteDaPagareSoddisfaConsiglio(List<ColoreCartaPolitica> coloriCartePolitica){
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
       return azioniPrincipaliDisponibili > 0;
    }

    private boolean prendiCartePoliticaGiocatore(Giocatore giocatore, List<ColoreCartaPolitica> coloriCartePolitica){
        List<Colore> arrayListManoColoriCartePolitica = coloriCartePolitica.stream().map(ColoreCartaPolitica::toColore).collect(Collectors.toList());
        HashMap<Colore, Integer> mappaColoriManoCartePoliticaGiocatore = Utility.listToHashMap(arrayListManoColoriCartePolitica);
        HashMap<Colore, Integer> mappaColoriCartePolitica = Utility.listToHashMap(ColoreCartaPolitica.toColore(coloriCartePolitica));
        if(Utility.hashMapContainsAllWithDuplicates(mappaColoriManoCartePoliticaGiocatore, mappaColoriCartePolitica)){
            ArrayList<CartaPolitica> cartePoliticaScartate = giocatore.scartaCartePolitica(coloriCartePolitica);
            cartePoliticaScartate.forEach((cartaPolitica) -> cartaPolitica.setVisibile(false));
            partita.addCartePoliticaScartate(cartePoliticaScartate);
            return true;
        }
        return false;
    }

    private boolean costruisciEmporio(NomeCittà nomeCittàCostruzione){
        Città cittàCostruzione = getCittàDaNome(nomeCittàCostruzione);
        Regione regione = getRegioneDaNomeCittà(nomeCittàCostruzione);
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
                //verifico se il giocatore ha costruito empori in tutte le città dello stesso colore
                if (grafoCittà.dfs((Città cittàAdiacente, Boolean valoreDaRitornare) -> { //codice metodo apply di BiFunction
                    if (cittàAdiacente.getColore().equals(cittàCostruzione.getColore()))
                        if (!cittàAdiacente.giàCostruito(giocatoreCorrente))
                            return false;
                    return valoreDaRitornare;
                })) { //corpo dell'if
                    assegnaBonus(partita.ottieniCartaBonusColoreCittà(cittàCostruzione.getColore()).getBonus());
                }
                //verifico se il giocatore ha costruito empori in tutte le città della stessa regione
                if (grafoCittà.dfs((Città cittàAdiacente, Boolean valoreDaRitornare) -> { //codice metodo apply di BiFunction
                    if (cittàAdiacente.getNomeRegione().equals(cittàCostruzione.getNomeRegione())) {
                        if (!cittàAdiacente.giàCostruito(giocatoreCorrente)) {
                            return false;
                        }
                    }
                    return valoreDaRitornare;
                })) { //corpo dell'if
                    assegnaBonus(regione.ottieniCartaBonusRegione().getBonus());
                }
                return true;
            } catch (AiutantiNonSufficientiException exc){
                return false;
            }
        } catch (EmporioGiàEsistenteException exc) {
            return false;
        }

    }

    private Regione getRegioneDaNomeCittà(NomeCittà nomeCittà) throws IllegalArgumentException{
        for (Regione regione : partita.getRegioni()) {
            if (regione.getNomiCittà().contains(nomeCittà)) {
                return regione;
            }
        }
        throw new IllegalArgumentException("Non esiste una città con questo nome");
    }

    private Città getCittàDaNome(NomeCittà nomeCittà) throws IllegalArgumentException{
        Regione regione = getRegioneDaNomeCittà(nomeCittà);
            for(Città cittàSingola : regione.getCittà())
                if (cittàSingola.getNome().equals(nomeCittà)) {
                    return cittàSingola;
                }
            throw new IllegalArgumentException("Non esiste una città con questo nome!");
    }
}



