package server.controller;

import interfaccecondivise.InterfacciaController;
import interfaccecondivise.InterfacciaView;
import server.model.*;
import server.model.bonus.*;
import server.model.carte.CartaBonusRegione;
import server.model.carte.CartaPermessoCostruzione;
import server.model.carte.CartaPolitica;
import server.model.carte.ColoreCartaPolitica;
import server.model.eccezioni.AiutantiNonSufficientiException;
import server.model.eccezioni.MoneteNonSufficientiException;
import server.sistema.CostantiSistema;
import server.sistema.Utility;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class Controller implements Runnable, InterfacciaController {
    private Partita partita;
    private ArrayList<InterfacciaView> views;
    private Giocatore giocatoreCorrente;
    private int azioniPrincipaliDisponibili;
    private boolean azioneVeloceEseguita;
    private HashMap<IdBalcone, BalconeDelConsiglio> mappaBalconi;
    private GrafoCittà grafoCittà;


    public Controller(Partita partita, ArrayList<InterfacciaView> views) throws RemoteException {
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
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void run() {
        //inizio il ciclo dei turni
        while(!partitaTerminata()){
            azioniPrincipaliDisponibili = 1;
            azioneVeloceEseguita = false;

            //il giocatore pesca una carta politica
            giocatoreCorrente.addCarta(partita.ottieniCartaPolitica());

            //il server.controller da il consenso al giocatore di iniziare il turno
            try {
                views.get(giocatoreCorrente.getId()).eseguiTurno();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            //il server.controller aspetta che il giocatore abbia finito il turno
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
    public boolean passaTurno()  throws RemoteException{ //verifica che il giocatore possa finire il turno
        //TODO: verifica azioni
        notify();
        return true;
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
    public boolean eleggereConsigliere(String idBalcone, String coloreConsigliereDaRiserva)  throws RemoteException{
        if(!azionePrincipaleDisponibile())
            return false;
        if (!inserisciConsigliereRiservaInBalcone(idBalcone, coloreConsigliereDaRiserva))
            return false;
        giocatoreCorrente.guadagnaMonete(CostantiModel.MONETE_GUADAGNATE_ELEGGERE_CONSIGLIERE);
        decrementaAzioniPrincipaliDisponibili();
        return true;
    }

    private boolean inserisciConsigliereRiservaInBalcone(String idBalcone, String coloreConsigliereDaRiserva){
        Consigliere consigliereDaInserireInBalcone, consigliereDaInserireInRiserva;
        try{
            consigliereDaInserireInBalcone = partita.ottieniConsigliereDaRiserva(ColoreConsigliere.valueOf(coloreConsigliereDaRiserva));
        } catch (NoSuchElementException exc){
            return false;
        }
        BalconeDelConsiglio balcone = mappaBalconi.get(IdBalcone.valueOf(idBalcone));
        if(balcone == null)
            return false;
        consigliereDaInserireInRiserva = balcone.addConsigliere(consigliereDaInserireInBalcone);
        partita.addConsigliereARiserva(consigliereDaInserireInRiserva);
        return true;
    }

    @Override
    public boolean acquistareTesseraPermessoCostruzione(String idBalconeRegione, List<String> nomiColoriCartePolitica, int numeroCarta)  throws RemoteException{
        if(!azionePrincipaleDisponibile())
            return false;
        Supplier<Boolean> supplier = () -> {
            CartaPermessoCostruzione cartaPermessoCostruzione;
            switch (numeroCarta) {
                case 1:
                    cartaPermessoCostruzione = partita.getRegione(NomeRegione.valueOf(idBalconeRegione)).ottieniCartaPermessoCostruzione1();
                    break;
                case 2:
                    cartaPermessoCostruzione = partita.getRegione(NomeRegione.valueOf(idBalconeRegione)).ottieniCartaPermessoCostruzione2();
                    break;
                default:
                    return false;
            }
            giocatoreCorrente.addCarta(cartaPermessoCostruzione);
            assegnaBonus(cartaPermessoCostruzione.getBonus());
            decrementaAzioniPrincipaliDisponibili();
            return true;
        };
        return acquistareTesseraPermesso(idBalconeRegione, nomiColoriCartePolitica, supplier);
    }



    private boolean acquistareTesseraPermesso(String idBalcone, List<String> nomiColoriCartePolitica, Supplier<Boolean> supplier){
        BalconeDelConsiglio balconeDelConsiglio = mappaBalconi.get(IdBalcone.valueOf(idBalcone));
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
                return true;
            } else {
                return false;
            }
        }
        return false;

    }
    @Override
    public boolean costruireEmporioConTesseraPermessoCostruzione(CartaPermessoCostruzione cartaPermessoCostruzione, String stringaNomeCittà)  throws RemoteException{
        if(!azionePrincipaleDisponibile())
            return false;
        NomeCittà nomeCittà = NomeCittà.valueOf(stringaNomeCittà);
        //il giocatore deve avere azioni principali disponibili; la carta permesso costruzione non deve essere coperta; la città passat in input deve essere presenta sulla carta
        //permesso; la carta permesso passata in input deve effettivamente appartenere alla mano carte permesso del giocatore
        if(!(cartaPermessoCostruzione.isVisibile() && cartaPermessoCostruzione.getCittà().contains(nomeCittà) && giocatoreCorrente.getManoCartePermessoCostruzione().contains(cartaPermessoCostruzione)))
            return false;
        if (!giocatoreCorrente.decrementaEmporiDisponibili()) {
            return false;
        } else {
            if (costruisciEmporio(nomeCittà)) {
                giocatoreCorrente.getManoCartePermessoCostruzione().remove(cartaPermessoCostruzione);
                cartaPermessoCostruzione.setVisibile(false);
                giocatoreCorrente.addCarta(cartaPermessoCostruzione);  //riassegno al giocatore la stessa carta coperta
                decrementaAzioniPrincipaliDisponibili();
                return true;
            } else return false;
        }
    }

    @Override
    public boolean costruireEmporioConAiutoRe(List<String> nomiColoriCartePolitica, String nomeCittàCostruzione)  throws RemoteException{
        if(!azionePrincipaleDisponibile())
            return false;
        if (!acquistareTesseraPermesso("RE", nomiColoriCartePolitica, () -> true)){
            return false;
        }
        grafoCittà.bfs(getCittàDaNome(partita.getCittàRe()), (p1, p2) -> {});
        Città cittàCostruzione = getCittàDaNome(NomeCittà.valueOf(nomeCittàCostruzione));
        Integer distanza = cittàCostruzione.getDistanza();
        if (distanza.equals(Integer.MAX_VALUE)){
            return false; //la città scelta non è collegata a quella dove risiede attualmente il Re
        }
        if (!costruisciEmporio(cittàCostruzione.getNome())) {
            return false;
        }
        try {
            giocatoreCorrente.pagaMonete(distanza * CostantiModel.MONETE_PER_STRADA);
        } catch (MoneteNonSufficientiException exc){
            return false;
        }
        partita.getRe().setPosizione(cittàCostruzione);
        decrementaAzioniPrincipaliDisponibili();
        return true;
    }

    @Override
    public boolean ingaggiareAiutante()  throws RemoteException{
        if(azioneVeloceEseguita)
            return false;
        try {
            giocatoreCorrente.pagaMonete(CostantiModel.MONETE_INGAGGIARE_AIUTANTE);
        } catch (MoneteNonSufficientiException exc){
            return false;
        }
        int aiutanti = CostantiModel.AIUTANTI_GUADAGNATI_INGAGG_AIUTANTE;
        try {
            partita.decrementaAiutanti(aiutanti);
        } catch (IllegalArgumentException exc){
            return false;
        }
        giocatoreCorrente.guadagnaAiutanti(aiutanti);
        azioneVeloceEseguita = true;
        return true;
    }

    @Override
    public boolean cambiareTesserePermessoCostruzione(String regione)  throws RemoteException{
        if(azioneVeloceEseguita)
            return false;
        if (!giocatoreRestituisciAiutantiARiserva(CostantiModel.AIUTANTI_PAGARE_CAMBIO_TESSERE_PERMESSO)){
            return false;
        }
        partita.getRegione(NomeRegione.valueOf(regione)).cambiaCartePermessoCostruzione();
        azioneVeloceEseguita = true;
        return true;
    }

    @Override
    public boolean mandareAiutanteEleggereConsigliere(String idBalcone, String coloreConsigliere)  throws RemoteException{
        if(azioneVeloceEseguita)
            return false;
        if (!giocatoreRestituisciAiutantiARiserva(CostantiModel.AIUTANTI_PAGARE_MANDA_AIUTANTE_ELEGG_CONS)) {
            return false;
        }
        if (!inserisciConsigliereRiservaInBalcone(idBalcone, coloreConsigliere))
            return false;
        azioneVeloceEseguita = true;
        return true;
    }

    @Override
    public boolean compiereAzionePrincipaleAggiuntiva()  throws RemoteException{
        if(azioneVeloceEseguita)
            return false;
        if (!giocatoreRestituisciAiutantiARiserva(CostantiModel.AIUTANTI_PAGARE_AZIONE_PRINCIPALE_AGGIUNTIVA))
            return false;
        azioniPrincipaliDisponibili++;
        azioneVeloceEseguita = true;
        return true;
    }

    private boolean giocatoreRestituisciAiutantiARiserva(int aiutanti){
        try {
            giocatoreCorrente.pagaAiutanti(aiutanti);
        } catch (AiutantiNonSufficientiException exc){
            return  false;
        }
        partita.aggiungiAiutanti(aiutanti);
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
                monete = CostantiModel.MONETE_1_CARTA_POLITICA;
                break;
            case 2:
                monete = CostantiModel.MONETE_2_CARTE_POLITICA;
                break;
            case 3:
                monete = CostantiModel.MONETE_3_CARTE_POLITICA;
                break;
            default:
                monete = 0;
                break;
        }
        return monete + numeroCarteJolly * CostantiModel.MONETE_PER_CARTA_JOLLY;

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
        if(cittàCostruzione.giàCostruito(giocatoreCorrente))
            return false;

        try {
            giocatoreCorrente.pagaAiutanti(CostantiModel.NUMERO_AIUTANTI_PAGARE_EMPORIO * cittàCostruzione.getNumeroEmporiCostruiti());
        } catch (AiutantiNonSufficientiException exc){return false;}


        cittàCostruzione.costruisciEmporio(new Emporio(giocatoreCorrente.getId()));

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
            CartaBonusRegione carta = regione.ottieniCartaBonusRegione();
            Bonus bonus = carta.getBonus();
            assegnaBonus(bonus);
        }
        return true;
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



