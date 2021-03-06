package server.controller;

import classicondivise.*;
import classicondivise.bonus.*;
import classicondivise.carte.CartaPermessoCostruzione;
import interfaccecondivise.InterfacciaController;
import interfaccecondivise.InterfacciaView;
import server.model.*;
import server.model.carte.CartaPolitica;
import server.model.carte.ColoreCartaPolitica;
import server.model.eccezioni.AiutantiNonSufficientiException;
import server.model.eccezioni.MoneteNonSufficientiException;
import server.sistema.CostantiSistema;
import server.sistema.SocketPollingController;
import server.sistema.Utility;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
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
    private GiocatoriOnline giocatoriOnline;
    private VetrinaMarket vetrinaMarket;
    private boolean faseTurno;
    private boolean faseVenditaMarket;
    private boolean faseAcquistoMarket;
    private boolean venduteCartePermesso;
    private boolean venduteCartePolitica;
    private boolean vendutiAiutanti;
    private boolean viewCorrenteRimossa = false;


    public Controller(Partita partita, ArrayList<InterfacciaView> views) throws RemoteException {
        this.partita = partita;
        this.views = views;
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
        giocatoriOnline = new GiocatoriOnline();
        partita.getGiocatori().forEach((Giocatore giocatore) -> {
            giocatoriOnline.aggiungiGiocatore(giocatore);
        });
        InterfacciaView viewCorrente;
        try{
            //inizio il ciclo dei turni
            while(!partitaTerminata()){

                //INIZIO TURNO
                faseTurno = true;
                do {
                    //si passa al giocatore successivo
                    giocatoreCorrente = giocatoriOnline.prossimo();
                    viewCorrente = getViewGiocatoreCorrente();
                    azioniPrincipaliDisponibili = 1;
                    azioneVeloceEseguita = false;

                    //il giocatore pesca una carta politica
                    giocatoreCorrente.addCarta(partita.ottieniCartaPolitica());

                    //il controller da il consenso al giocatore di iniziare il turno
                    viewCorrente.eseguiTurno();

                    comunicaAGiocatoreCorrente("E' il tuo turno");
                    comunicaAdAltriGiocatori("E' il turno di giocatore " + giocatoreCorrente.getId());

                    //il controller aspetta che il giocatore abbia finito il turno
                    try {
                        synchronized (this){
                            wait(CostantiSistema.TIMEOUT_TURNO);
                        }
                        if (views.size() == 0) {
                            System.out.println("Partita terminata: tutti i giocatori sono andati offline");
                            return;
                        }
                        if (!viewCorrenteRimossa) { //se view corrente fosse offline la chiamata di fine turno causerebbe una NullPointerException
                            viewCorrente.fineTurno();
                        } else viewCorrenteRimossa = false;
                    } catch (InterruptedException exc) {
                        exc.printStackTrace();
                    }
                } while (giocatoriOnline.haProssimo());
                faseTurno = false;


                vetrinaMarket = new VetrinaMarket();
                faseVenditaMarket = true;
                //INIZIO FASE VENDITA MARKET
                comunicaATutti("Inizia la fase di vendita del market");
                do {
                    //si passa al giocatore successivo
                    giocatoreCorrente = giocatoriOnline.prossimo();
                    viewCorrente = getViewGiocatoreCorrente();
                    //questi flag servono per evitare che il giocatore venda più volte oggetti dello stesso tipo
                    venduteCartePermesso = false;
                    venduteCartePolitica = false;
                    vendutiAiutanti = false;
                    viewCorrente.vendi();

                    comunicaAGiocatoreCorrente("E' il tuo turno di vendita");
                    comunicaAdAltriGiocatori("E' il turno di vendita di giocatore " + giocatoreCorrente.getId());

                    try {
                        synchronized (this){
                            wait(CostantiSistema.TIMEOUT_TURNO);
                        }
                        if (views.size() == 0) {
                            System.out.println("Partita terminata: tutti i giocatori sono andati offline");
                            return;
                        }
                        if (!viewCorrenteRimossa) {
                            viewCorrente.fineVendi();
                        } else viewCorrenteRimossa = false;
                    } catch (InterruptedException exc) {
                        exc.printStackTrace();
                    }
                } while (giocatoriOnline.haProssimo());
                faseVenditaMarket = false;


                faseAcquistoMarket = true;
                //INIZIO FASE ACQUISTO MARKET
                comunicaATutti("Inizia la fase di acquisto del market");
                ScatolaIdGiocatori scatolaIdGiocatori = new ScatolaIdGiocatori();
                do {
                    giocatoreCorrente = giocatoreDaPartita(scatolaIdGiocatori.pescaNumero()); //scelgo un giocatore a caso per la fase di acquisto
                    viewCorrente = getViewGiocatoreCorrente();
                    viewCorrente.compra(vetrinaMarket.getVendibili());

                    comunicaAGiocatoreCorrente("E' il tuo turno di acquisto");
                    comunicaAdAltriGiocatori("E' il turno di acquisto di giocatore " + giocatoreCorrente.getId());

                    try {
                        synchronized (this){
                            wait(CostantiSistema.TIMEOUT_TURNO);
                        }
                        if (views.size() == 0) {
                            System.out.println("Partita terminata: tutti i giocatori sono andati offline");
                            return;
                        }
                        if (!viewCorrenteRimossa) {
                            viewCorrente.fineCompra();
                        } else viewCorrenteRimossa = false;
                    } catch (InterruptedException exc) {
                        exc.printStackTrace();
                    }

                } while (!scatolaIdGiocatori.èVuota());
                faseAcquistoMarket = false;
            }
            //dopo che la partita è terminata procedo al conteggio dei punti vittoria
            ArrayList<Integer> puntiVittoriaGiocatori = new ArrayList<>();
            ArrayList<Giocatore> giocatori = partita.getGiocatori();
            for (Giocatore giocatore : giocatori){
                puntiVittoriaGiocatori.add(giocatore.getPuntiVittoria());
            }
            int primo = Collections.max(puntiVittoriaGiocatori); //calcolo il massimo dei punti vittoria guadagnati da tutti i giocatori
            int contatorePrimi = 0;    //conto quanti giocatori hanno guadagnato punti vittoria pari al massimo
            for (Giocatore giocatore : giocatori) {
                if (giocatore.getPuntiVittoria() == primo) {
                    contatorePrimi++;
                }
            }
            for (int i = 0; i < contatorePrimi; i++) {
                puntiVittoriaGiocatori.remove(primo); //tolgo tutte le occorrenze di punteggio massimo
            }
            if (puntiVittoriaGiocatori.size() == 0) { //controllo se ci sono oppure no altri punteggi
                assegnaPuntiVittoria(contatorePrimi, primo, 0, 0);
            } else{
                int secondo = Collections.max(puntiVittoriaGiocatori);  //se ci sono altri punteggi allora calcolo il massimo del nuovo ArrayList puntiVittoria privato dei punteggi massimi
                int contatoreSecondi = 0; //conto quanti punteggi secondi ci sono
                for (Giocatore giocatore : giocatori) {
                    if (giocatore.getPuntiVittoria() == secondo) {
                        contatoreSecondi++;
                    }
                }
                assegnaPuntiVittoria(contatorePrimi, primo, contatoreSecondi, secondo);
            }
            //assegno punti vittoria al giocatore con più carte permesso
            ArrayList<Integer> numeroCartePermesso = new ArrayList<>();
            for (Giocatore giocatore : giocatori){
                numeroCartePermesso.add(giocatore.getManoCartePermessoCostruzione().size());
            }
            int maxNumCartePermeesso = Collections.max(numeroCartePermesso);
            for (Giocatore giocatore : giocatori) {
                if (giocatore.getManoCartePermessoCostruzione().size() == maxNumCartePermeesso) {
                    giocatore.guadagnaPuntiVittoria(CostantiModel.PUNTI_VITTORIA_GUADAGNATI_MAGGIOR_NUMERO_TESSERE_PERMESSO);
                }
            }

            ArrayList<Integer> puntiVittoriaGiocatoriFinali = new ArrayList<>();
            for (Giocatore giocatore : giocatori) {
                puntiVittoriaGiocatoriFinali.add(giocatore.getPuntiVittoria());
            }
            int puntiVittoriaMax = Collections.max(puntiVittoriaGiocatoriFinali); //calcolo il massimo di tutti i punti vittoria aggiornati
            for (Giocatore giocatore : giocatori){
                if (giocatore.getPuntiVittoria() == puntiVittoriaMax) {
                    comunicaATutti("!!!!!!!!!!!!!!!Ha vinto giocatore " + giocatore.getId() + "!!!!!!!!!!!!!!!");
                }
            }
        } catch (RemoteException exc){
            exc.printStackTrace();
        }
        System.out.println("Partita terminata");
    }

    private void assegnaPuntiVittoria(int numPrimi, int puntiPrimo, int numSecondi, int puntiSecondo) { //assegno i punti vittoria in base a quanti sono arrivati primi e quanti sono arrivati secondi
        ArrayList<Giocatore> giocatori = partita.getGiocatori();
        if (numPrimi > 1) { //se più giocatore hanno punti vittoria pari al massimo allora assegno 5 punti vittoria a questi giovatori e 0 a tutti gli altri
            for (Giocatore giocatore : giocatori) {
                if (giocatore.getPuntiVittoria() == puntiPrimo) {
                    giocatore.guadagnaPuntiVittoria(CostantiModel.PUNTI_VITTORIA_GUADAGNATI_PRIMO_PERCORSO_NOBILTA);
                }
            }
        } else { //altrimenti controllo quanti giocatori sono arrivati pari merito al secondo posto
            if (numSecondi != 0) {
                for (Giocatore giocatore : giocatori) {
                    if (giocatore.getPuntiVittoria() == puntiSecondo) {
                        giocatore.guadagnaPuntiVittoria(CostantiModel.PUNTI_VITTORIA_GUADAGNATI_SECONDO_PERCORSO_NOBILTA);
                    } else if (giocatore.getPuntiVittoria() == puntiPrimo) {
                        giocatore.guadagnaPuntiVittoria(CostantiModel.PUNTI_VITTORIA_GUADAGNATI_PRIMO_PERCORSO_NOBILTA);
                    }
                }
            } else { //se c'è solo un giocatore arrivato primo e nessuno arrivato secondo assegno 5 punti al primo giocatore
                for (Giocatore giocatore : giocatori) {
                   if (giocatore.getPuntiVittoria() == puntiPrimo) {
                        giocatore.guadagnaPuntiVittoria(CostantiModel.PUNTI_VITTORIA_GUADAGNATI_PRIMO_PERCORSO_NOBILTA);
                    }
                }
            }
        }
    }

    private Giocatore giocatoreDaPartita(int idGiocatore) {
        for (Giocatore giocatore : partita.getGiocatori()) {
            if (giocatore.getId() == idGiocatore) {
                return giocatore;
            }
        }
        throw new IllegalArgumentException("Non esiste un giocatore con questo Id!");
    }

    private InterfacciaView getViewGiocatoreCorrente(){
        for (InterfacciaView view : views) {
            try {
                if (view.getIdGiocatore() == giocatoreCorrente.getId()) {
                    return view;
                }
            } catch (RemoteException exc) {
                exc.printStackTrace();
            }

        }
        throw new IllegalArgumentException("Non esiste una view con Id uguale all'Id del giocatore corrente!");
    }

    private boolean emporiDisponibili(Giocatore giocatore){
        return giocatore.getEmporiDisponibili() > 0;
    }


    private boolean partitaTerminata(){ //cicla sui giocatori per capire se qualcuno ha terminato i propri empori disponibili
        for(Giocatore giocatore : partita.getGiocatori())
            if (!emporiDisponibili(giocatore))
                return true;
        return false;
    }

    @Override
    public boolean passaTurno() throws RemoteException {
        synchronized (this) {
            notify();
        }
        return true;
    }


    private void assegnaBonus(Bonus bonus) throws IllegalArgumentException {
        int num;
        while (!(bonus instanceof NullBonus)){
            if(bonus instanceof BonusAiutanti) {
                try {
                    num = ((BonusAiutanti) bonus).getNumeroAiutanti();
                    partita.decrementaAiutanti(num);
                    giocatoreCorrente.guadagnaAiutanti(num);
                    comunicaBonus(num + " aiutanti!");
                } catch (IllegalArgumentException exc) {
                    comunicaAGiocatoreCorrente(exc.getMessage());
                }
            }
            else if (bonus instanceof BonusAvanzaPercorsoNobiltà){
                num = ((BonusAvanzaPercorsoNobiltà) bonus).getNumeroPosti();
                giocatoreCorrente.avanzaPercorsoNobiltà(num);
                comunicaBonus(num + " passi in avanti sul percorso della nobiltà!");
                assegnaBonus(partita.getPercorsoDellaNobiltà().get(num));
            }
            else if(bonus instanceof BonusMonete){
                num = ((BonusMonete) bonus).getNumeroMonete();
                giocatoreCorrente.guadagnaMonete(num);
                comunicaBonus(num + " monete!");
            }
            else if(bonus instanceof BonusPescaCartaPolitica){
                try{
                    num = ((BonusPescaCartaPolitica) bonus).getNumeroCarte();
                    for (int i = 0; i < num; i++)
                        giocatoreCorrente.addCarta(partita.ottieniCartaPolitica());
                    comunicaBonus(num + " pescate di carte politica");
                } catch (NoSuchElementException exc){
                    comunicaAGiocatoreCorrente("Non sono disponibili carte politica!");
                }
            }
            else if(bonus instanceof BonusPuntiVittoria){
                num = ((BonusPuntiVittoria) bonus).getPuntiVittoria();
                giocatoreCorrente.guadagnaPuntiVittoria(num);
                comunicaBonus(num + " punti vittoria!");
            }
            else if(bonus instanceof BonusRipetiAzionePrincipale){
                azioniPrincipaliDisponibili++;
                comunicaBonus("un'azione principale aggiuntiva!");
            }
            else throw new IllegalArgumentException("Bonus non previsto"); //non si dovrebbe mai arrivare in questo branch else, se succede significa che è stato passato in ingresso un Bonus non previsto
            bonus = ((RealBonus) bonus).getDecoratedBonus();
        }
    }

    //comunica al giocatore corrente il bonus guadagnato
    private void comunicaBonus(String messaggio){
        views.forEach((InterfacciaView view) -> {
            try {
                if (view.getIdGiocatore() == giocatoreCorrente.getId()){
                    view.mostraMessaggio("Hai guadagnato " + messaggio);
                } else {
                    view.mostraMessaggio("Giocatore " + giocatoreCorrente.getId() + " ha guadagnato " + messaggio);
                }
            } catch (RemoteException exc){
                exc.printStackTrace();
            }

        });
    }

    private void comunicaAGiocatoreCorrente(String messaggio) {
        views.forEach((InterfacciaView view) -> {
            try {
                if (view.getIdGiocatore() == giocatoreCorrente.getId())
                    view.mostraMessaggio(messaggio);
            } catch (RemoteException exc) {
                exc.printStackTrace();
            }
        });
    }

    private void comunicaAdAltriGiocatori(String messaggio){
        views.forEach((InterfacciaView view) -> {
            try {
                if (view.getIdGiocatore() != giocatoreCorrente.getId())
                    view.mostraMessaggio(messaggio);
            } catch (RemoteException exc) {
                exc.printStackTrace();
            }
        });
    }

    private void comunicaATutti(String messaggio){
        views.forEach((InterfacciaView view) -> {
            try {
                    view.mostraMessaggio(messaggio);
            } catch (RemoteException exc) {
                exc.printStackTrace();
            }
        });
    }

    //controllo che il giocatore abbia azioni principali disponibili e che sia la fase di turno gioco
    private boolean controlliGeneraliTurnoAzionePrincipale(){
        if (!faseTurno){
            comunicaAGiocatoreCorrente("Non puoi eseguire mosse in questo momento!");
            return false;
        }
        if (!azionePrincipaleDisponibile()) {
            comunicaAGiocatoreCorrente("Non hai più azioni principali disponibili!");
            return false;
        }
        return true;
    }


    //la mossa eleggere un consigliere inserisce in un balcone un consigliere preso dalla riserva
    @Override
    public boolean eleggereConsigliere(String idBalcone, String coloreConsigliereDaRiserva)  throws RemoteException{
        if (controlliEleggereConsigliere(idBalcone, coloreConsigliereDaRiserva)) {
            if (!inserisciConsigliereRiservaInBalcone(idBalcone, coloreConsigliereDaRiserva)) {
                comunicaAGiocatoreCorrente("Non è stato possibile inserire il consigliere nel balcone!");
                return false;
            }
            giocatoreCorrente.guadagnaMonete(CostantiModel.MONETE_GUADAGNATE_ELEGGERE_CONSIGLIERE);
            decrementaAzioniPrincipaliDisponibili();
            return true;
        }
        return false;
    }

    private boolean controlliEleggereConsigliere(String idBalcone, String coloreConsigliereDaRiserva){
        if (!controlliGeneraliTurnoAzionePrincipale()) {
            return false;
        }
        try {
            IdBalcone.valueOf(idBalcone);
        } catch (IllegalArgumentException exc){
            comunicaAGiocatoreCorrente("Il nome del balcone inserito non esiste!");
            return false;
        }
        try {
            ColoreConsigliere.valueOf(coloreConsigliereDaRiserva);
        } catch (IllegalArgumentException exc) {
            comunicaAGiocatoreCorrente("Il colore inserito non esiste!");
            return false;
        }
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

    //la mossa acquistare permesso di costruzione richiede la scelta di un balcone e della carte politica con cui soddisfare quel balcone. Se è possibile soddisfare il balcone
    //allora si ottiene una carta permesso a scelta tra le due visibili
    @Override
    public boolean acquistareTesseraPermessoCostruzione(String idBalconeRegione, List<String> nomiColoriCartePolitica, int numeroCarta)  throws RemoteException{
        if (controlliAcquistareTesseraPermessoCostruzione(idBalconeRegione, nomiColoriCartePolitica, numeroCarta)) {
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
        return false;
    }

    private boolean controlliAcquistareTesseraPermessoCostruzione(String idBalconeRegione, List<String> nomiColoriCartePolitica, int numeroCarta){
        if (!controlliGeneraliTurnoAzionePrincipale()) {
            return false;
        }
        NomeRegione nomeRegione;
        try {
            nomeRegione = NomeRegione.valueOf(idBalconeRegione);
        } catch (IllegalArgumentException exc) {
            comunicaAGiocatoreCorrente("Il nome del balcone inserito non è valido!");
            return false;
        }
        try {
            for (String coloreCartaPolitica : nomiColoriCartePolitica){
                ColoreCartaPolitica.valueOf(coloreCartaPolitica);
            }
        } catch (IllegalArgumentException exc){
            comunicaAGiocatoreCorrente("Non esiste il colore inserito!");
            return false;
        }
        if (!(numeroCarta == 1 || numeroCarta == 2)){
            comunicaAGiocatoreCorrente("Il numero di carta inserito non è valido!");
            return false;
        }
        Regione regione = partita.getRegione(nomeRegione);

        //controlle che ci sia effettivamente la carta voluta
        switch (numeroCarta){
            case 1:
                if (regione.cartaPermessoCostruzione1IsNull()) {
                    comunicaAGiocatoreCorrente("La carta permesso costruzione scelta non è disponibile");
                    return false;
                }
                break;
            case 2:
                if (regione.cartaPermessoCostruzione2IsNull()) {
                    comunicaAGiocatoreCorrente("La carta permesso costruzione scelta non è disponibile");
                    return false;
                }
                break;
        }

        //controllo che il giocatore abbia abbastanza monete per completare la mossa
        List<ColoreCartaPolitica> coloriCartePolitica = nomiColoriCartePolitica.stream().map(ColoreCartaPolitica::valueOf).collect(Collectors.toList());
        int moneteDaPagare = moneteDaPagareSoddisfaConsiglio(coloriCartePolitica);
        if (giocatoreCorrente.getMonete() - moneteDaPagare  < 0) {
            comunicaAGiocatoreCorrente("Non hai abbastanza monete per eseguire la mossa!");
            return false;
        }

        //controllo che sia possibile soddisfare il consiglio con le carte scelte
        BalconeDelConsiglio balconeDelConsiglio = mappaBalconi.get(IdBalcone.valueOf(idBalconeRegione));
        if(!(balconeDelConsiglio.soddisfaConsiglio(coloriCartePolitica))){
            comunicaAGiocatoreCorrente("Non puoi soddisfare il balcone del consiglio!");
            return false;
        }

        //controllo che il giocatore abbia le carte scelte
        List<ColoreCartaPolitica> manoColoriCartePoliticaGiocatore = giocatoreCorrente.getColoriCartePolitica();
        List<Colore> arrayListManoColoriGiocatore = manoColoriCartePoliticaGiocatore.stream().map(ColoreCartaPolitica::toColore).collect(Collectors.toList());
        HashMap<Colore, Integer> mappaColoriManoCartePoliticaGiocatore = Utility.listToHashMap(arrayListManoColoriGiocatore);
        HashMap<Colore, Integer> mappaColoriCartePolitica = Utility.listToHashMap(ColoreCartaPolitica.toColore(coloriCartePolitica));
        if(!(Utility.hashMapContainsAllWithDuplicates(mappaColoriManoCartePoliticaGiocatore, mappaColoriCartePolitica))){
            comunicaAGiocatoreCorrente("Le carte selezionate non sono valide!");
            return false;
        }
        return true;
    }


    //questo è un metodo utilizzato sia dalla mossa acquistare tessera permesso di costruzione che dalla mossa costruire emporio con l'aiuto del re
    //prende in input un supplier che determina a runtime quale caratteristica assumere in base al metodo chiamante
    //nel caso di acquistare tessera permesso costruzione il supplier assegna al giocatore la carta voluta
    //nel caso di costruire emporio con l'aiuto del re il supplier non fa nulla e ritorna true
    private boolean acquistareTesseraPermesso(String idBalcone, List<String> nomiColoriCartePolitica, Supplier<Boolean> supplier){
        BalconeDelConsiglio balconeDelConsiglio = mappaBalconi.get(IdBalcone.valueOf(idBalcone));
        //creo una mano di colori carte  politica come struttura di supporto
        List<ColoreCartaPolitica> coloriCartePolitica = nomiColoriCartePolitica.stream().map(ColoreCartaPolitica::valueOf).collect(Collectors.toList());
        int moneteDaPagare = moneteDaPagareSoddisfaConsiglio(coloriCartePolitica);
        prendiCartePoliticaGiocatore(giocatoreCorrente, coloriCartePolitica);
        try {
            giocatoreCorrente.pagaMonete(moneteDaPagare);
        } catch (MoneteNonSufficientiException exc) {
            comunicaAGiocatoreCorrente("Non hai abbastanza monete per eseguire la mossa!");
            return false;
        }
        return supplier.get();
    }

    //la mossa costruire emporio con tessera permesso costruzione mi permette di costruire un emporio nella città scelta a patto che sia indicata sulla tessera permesso scelta
    @Override
    public boolean costruireEmporioConTesseraPermessoCostruzione(CartaPermessoCostruzione cartaPermessoCostruzione, String stringaNomeCittà)  throws RemoteException{
        if (controlliCostruireEmporioConTesseraPermessoCostruzione(cartaPermessoCostruzione, stringaNomeCittà)) {
            NomeCittà nomeCittà = NomeCittà.valueOf(stringaNomeCittà);
            if (costruisciEmporio(nomeCittà)) {
                List<CartaPermessoCostruzione> lista = new ArrayList<>();
                lista.add(cartaPermessoCostruzione);
                giocatoreCorrente.scartaCartePermessoCostruzione(lista);
                cartaPermessoCostruzione.setVisibile(false);
                giocatoreCorrente.addCarta(cartaPermessoCostruzione);  //riassegno al giocatore la stessa carta coperta
                decrementaAzioniPrincipaliDisponibili();
                comunicaAdAltriGiocatori("Giocatore " + giocatoreCorrente.getId() + " ha costruito un emporio nella città di " + nomeCittà);
                comunicaAGiocatoreCorrente("Hai costruito nella città di " + nomeCittà);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean controlliCostruireEmporioConTesseraPermessoCostruzione(CartaPermessoCostruzione cartaPermessoCostruzione, String stringaNomeCittà){
        if (!controlliGeneraliTurnoAzionePrincipale()) {
            return false;
        }
        NomeCittà nomeCittà;
        try {
            nomeCittà = NomeCittà.valueOf(stringaNomeCittà);
        } catch (IllegalArgumentException exc) {
            comunicaAGiocatoreCorrente("Il nome della città non è valido!");
            return false;
        }

        //la carta permesso costruzione non deve essere coperta; la città passata in input deve essere presenta sulla carta
        //permesso; la carta permesso passata in input deve effettivamente appartenere alla mano carte permesso del giocatore
        if (!(cartaPermessoCostruzione.isVisibile() && cartaPermessoCostruzione.getCittà().contains(nomeCittà) && giocatoreCorrente.getManoCartePermessoCostruzione().contains(cartaPermessoCostruzione))) {
            comunicaAGiocatoreCorrente("La scelta della carta permesso non è valida!");
            return false;
        }
        if (giocatoreCorrente.getEmporiDisponibili() < 1) {
            comunicaAGiocatoreCorrente("Non hai più empori disponibili!");
            return false;
        }

        Città città = getCittàDaNome(nomeCittà);
        if (città.giàCostruito(giocatoreCorrente)) {
            comunicaAGiocatoreCorrente("Hai già costruito in questa città!");
            return false;
        }

        int numeroAiutanti = CostantiModel.NUMERO_AIUTANTI_PAGARE_EMPORIO * città.getNumeroEmporiCostruiti();
        if (giocatoreCorrente.getAiutanti() - numeroAiutanti < 0) {
            comunicaAGiocatoreCorrente("Non hai abbastanza aiutanti per eseguire la mossa!");
            return false;
        }
        return true;
    }

    //costruire emporio con l'aiuto del re utilizza il metodo acquistare tessera permesso per soddisfare il balcone del consiglio del re
    //poi viene costruito un emporio nella città indicata e spostato il re in quella città
    @Override
    public boolean costruireEmporioConAiutoRe(List<String> nomiColoriCartePolitica, String nomeCittàCostruzione)  throws RemoteException{
        if (controlliCostruireEmporioConAiutoRe(nomiColoriCartePolitica, nomeCittàCostruzione)) {
            grafoCittà.bfs(getCittàDaNome(partita.getCittàRe()), (p1, p2) -> {});
            Città cittàCostruzione = getCittàDaNome(NomeCittà.valueOf(nomeCittàCostruzione));
            Integer distanza = cittàCostruzione.getDistanza();
            int moneteDaPagare = distanza * CostantiModel.MONETE_PER_STRADA;

            acquistareTesseraPermesso("RE", nomiColoriCartePolitica, () -> true);

            //ora sono sicuro che posso costruire un emporio
            if (!costruisciEmporio(cittàCostruzione.getNome())) {
                comunicaAGiocatoreCorrente("Non puoi costruire un emporio!");
                return false;
            }
            try {
                giocatoreCorrente.pagaMonete(moneteDaPagare);
            } catch (MoneteNonSufficientiException exc){
                comunicaAGiocatoreCorrente("Non hai abbastanza monete per eseguire la mossa!");
                return false;
            }
            partita.getRe().setPosizione(cittàCostruzione);
            decrementaAzioniPrincipaliDisponibili();
            comunicaAdAltriGiocatori("Giocatore " + giocatoreCorrente.getId() + " ha costruito nella città  di " + cittàCostruzione);
            comunicaAGiocatoreCorrente("Hai costruito nella città  di " + cittàCostruzione);
            return true;
        }
        return false;
    }

    private boolean controlliCostruireEmporioConAiutoRe(List<String> nomiColoriCartePolitica, String nomeCittàCostruzione){
        if (!controlliGeneraliTurnoAzionePrincipale()) {
            return false;
        }
        Città cittàCostruzione;
        try {
             cittàCostruzione = getCittàDaNome(NomeCittà.valueOf(nomeCittàCostruzione));
        } catch (IllegalArgumentException exc) {
            comunicaAGiocatoreCorrente("Il nome della città non è valido!");
            return false;
        }
        try {
            for (String coloreCartaPolitica : nomiColoriCartePolitica){
                ColoreCartaPolitica.valueOf(coloreCartaPolitica);
            }
        } catch (IllegalArgumentException exc){
            comunicaAGiocatoreCorrente("Non esiste il colore inserito!");
            return false;
        }

        grafoCittà.bfs(getCittàDaNome(partita.getCittàRe()), (p1, p2) -> {});
        Integer distanza = cittàCostruzione.getDistanza();
        if (distanza.equals(Integer.MAX_VALUE)){
            comunicaAGiocatoreCorrente("La città scelta non è collegata a quella dove risiede attualmente il Re!");
            return false;
        }

        //controllo che sia possibile soddisfare il consiglio con le carte scelte
        List<ColoreCartaPolitica> coloriCartePolitica = nomiColoriCartePolitica.stream().map(ColoreCartaPolitica::valueOf).collect(Collectors.toList());
        BalconeDelConsiglio balconeDelConsiglio = mappaBalconi.get(IdBalcone.RE);
        if(!(balconeDelConsiglio.soddisfaConsiglio(coloriCartePolitica))){
            comunicaAGiocatoreCorrente("Non puoi soddisfare il balcone del consiglio!");
            return false;
        }

        //controllo che il giocatore abbia le carte scelte
        List<ColoreCartaPolitica> manoColoriCartePoliticaGiocatore = giocatoreCorrente.getColoriCartePolitica();
        List<Colore> arrayListManoColoriGiocatore = manoColoriCartePoliticaGiocatore.stream().map(ColoreCartaPolitica::toColore).collect(Collectors.toList());
        HashMap<Colore, Integer> mappaColoriManoCartePoliticaGiocatore = Utility.listToHashMap(arrayListManoColoriGiocatore);
        HashMap<Colore, Integer> mappaColoriCartePolitica = Utility.listToHashMap(ColoreCartaPolitica.toColore(coloriCartePolitica));
        if(!(Utility.hashMapContainsAllWithDuplicates(mappaColoriManoCartePoliticaGiocatore, mappaColoriCartePolitica))){
            comunicaAGiocatoreCorrente("Le carte selezionate non sono valide!");
            return false;
        }

        //controllo che il giocatore abbia abbastanza monete per eseguire la mossa
        int moneteDaPagare = distanza * CostantiModel.MONETE_PER_STRADA;
        moneteDaPagare += moneteDaPagareSoddisfaConsiglio(coloriCartePolitica);
        if (giocatoreCorrente.getMonete() - moneteDaPagare < 0 ) {
            comunicaAGiocatoreCorrente("Non hai abbastanza monete per costruire un emporio in questa città!");
            return false;
        }

        //verifico se si può costruire un emporio
        if (cittàCostruzione.giàCostruito(giocatoreCorrente)) {
            comunicaAGiocatoreCorrente("Hai già costruito in questa città!");
            return false;
        }

        int numeroAiutanti = CostantiModel.NUMERO_AIUTANTI_PAGARE_EMPORIO * cittàCostruzione.getNumeroEmporiCostruiti();
        if (giocatoreCorrente.getAiutanti() - numeroAiutanti < 0) {
            comunicaAGiocatoreCorrente("Ti servono " + numeroAiutanti + " per cotruire in questa città!");
            return false;
        }
      return true;
    }


    //la mossa veloce ingaggiare un aiutante permette di guadagnare un aiutante pagando delle monete
    @Override
    public boolean ingaggiareAiutante()  throws RemoteException{
        if (!faseTurno){
            comunicaAGiocatoreCorrente("Non puoi eseguire mosse in questo momento!");
            return false;
        }
        if (azioneVeloceEseguita) {
            comunicaAGiocatoreCorrente("Hai già eseguito un'azione veloce!");
            return false;
        }
        int moneteDaPagare = CostantiModel.MONETE_INGAGGIARE_AIUTANTE;
        if (giocatoreCorrente.getMonete() - moneteDaPagare < 0) {
            comunicaAGiocatoreCorrente("Non hai abbastanza monete per eseguire la mossa!");
            return false;
        }
        int aiutanti = CostantiModel.AIUTANTI_GUADAGNATI_INGAGG_AIUTANTE;
        if (partita.getRiservaAiutanti() - aiutanti < 0) {
            comunicaAGiocatoreCorrente("Non ci sono abbastanza aiutanti in riserva!");
            return false;
        }
        try {
            giocatoreCorrente.pagaMonete(moneteDaPagare);
        } catch (MoneteNonSufficientiException exc){
            return false;
        }
        try {
            partita.decrementaAiutanti(aiutanti);
        } catch (IllegalArgumentException exc){
            return false;
        }
        giocatoreCorrente.guadagnaAiutanti(aiutanti);
        comunicaAGiocatoreCorrente("Hai guadagnato " + aiutanti + ((aiutanti == 1) ? " aiutante" : " aiutanti"));
        comunicaAdAltriGiocatori("Giocatore " + giocatoreCorrente.getId() + " ha guadagnato " + aiutanti + ((aiutanti == 1) ? " aiutante" : " aiutanti"));
        azioneVeloceEseguita = true;
        return true;
    }

    //la mossa cambiare tessere permesso costruzione permette di sostituire le due carte permesso visibili con due carte nuove pescate dal mazzo pagando degli aiutanti
    @Override
    public boolean cambiareTesserePermessoCostruzione(String regione)  throws RemoteException{
        if (!faseTurno){
            comunicaAGiocatoreCorrente("Non puoi eseguire mosse in questo momento!");
            return false;
        }
        if (azioneVeloceEseguita) {
            comunicaAGiocatoreCorrente("Hai già eseguito un'azione veloce!");
            return false;
        }
        int aiutanti = CostantiModel.AIUTANTI_PAGARE_CAMBIO_TESSERE_PERMESSO;
        if (!giocatoreRestituisciAiutantiARiserva(aiutanti)){
            comunicaAGiocatoreCorrente("Ti servono " +  aiutanti + " aiutanti per eseguire questa azione veloce!");
            return false;
        }
        partita.getRegione(NomeRegione.valueOf(regione)).cambiaCartePermessoCostruzione();
        azioneVeloceEseguita = true;
        return true;
    }

    //con questa mossa veloce si va ad inserire un consigliere del colore voluto preso da riserva pagando degli aiutanti
    @Override
    public boolean mandareAiutanteEleggereConsigliere(String idBalcone, String coloreConsigliere)  throws RemoteException{
        if (!faseTurno){
            comunicaAGiocatoreCorrente("Non puoi eseguire mosse in questo momento!");
            return false;
        }
        if (azioneVeloceEseguita) {
            comunicaAGiocatoreCorrente("Hai già eseguito un'azione veloce!");
            return false;
        }
        int aiutanti = CostantiModel.AIUTANTI_PAGARE_MANDA_AIUTANTE_ELEGG_CONS;
        if (giocatoreCorrente.getAiutanti() - aiutanti < 0) {
            comunicaAGiocatoreCorrente("Ti servono " +  aiutanti + " aiutanti per eseguire questa azione veloce!");
            return false;
        }
        if (!inserisciConsigliereRiservaInBalcone(idBalcone, coloreConsigliere)){
            comunicaAGiocatoreCorrente("Non è possibile inserire nel balcone il consigliere scelto!");
            return false;
        }
        if (!giocatoreRestituisciAiutantiARiserva(aiutanti)) {
            return false;
        }
        azioneVeloceEseguita = true;
        return true;
    }

    @Override
    public boolean compiereAzionePrincipaleAggiuntiva()  throws RemoteException{
        if (!faseTurno){
            comunicaAGiocatoreCorrente("Non puoi eseguire mosse in questo momento!");
            return false;
        }
        if (azioneVeloceEseguita) {
            comunicaAGiocatoreCorrente("Hai già eseguito un'azione veloce!");
            return false;
        }
        int aiutanti = CostantiModel.AIUTANTI_PAGARE_AZIONE_PRINCIPALE_AGGIUNTIVA;
        if (!giocatoreRestituisciAiutantiARiserva(aiutanti)) {
            comunicaAGiocatoreCorrente("Ti servono " +  aiutanti + " aiutanti per eseguire questa azione veloce!");
            return false;
        }
        azioniPrincipaliDisponibili++;
        comunicaAGiocatoreCorrente("Puoi eseguire un'azione principale aggiuntiva!");
        azioneVeloceEseguita = true;
        return true;
    }

    //questo metodo permette al giocatore di mettere in vendita degli oggetti, una sola volta per ogni tipo
    //gli oggetti possono essere venduti se il giocatore li possiede veramente ed effettuati gli oppportuni controlli vengono inseriti nella vetrna market
    @Override
    public boolean vendi(List<Vendibile> vendibili){
        if (!faseVenditaMarket){
            comunicaAGiocatoreCorrente("Non puoi vendere in questo momento!");
            return false;
        }
        for (Vendibile vendibile : vendibili) {
            switch (vendibile.getIdVendibile()){
                case CARTE_PERMESSO_COSTRUZIONE:
                    if (!venduteCartePermesso) {
                        List<CartaPermessoCostruzione> cartePermesso = (List<CartaPermessoCostruzione>) vendibile.getOggetto();
                        HashMap<CartaPermessoCostruzione, Integer> mappaCartePermessoVendibili = Utility.listToHashMap(cartePermesso);
                        HashMap<CartaPermessoCostruzione, Integer> mappaCartePermessoGiocatore = Utility.listToHashMap(giocatoreCorrente.getManoCartePermessoCostruzione());
                        if (Utility.hashMapContainsAllWithDuplicates(mappaCartePermessoGiocatore, mappaCartePermessoVendibili)) {
                            vetrinaMarket.aggiungiVendibile(vendibile);
                            venduteCartePermesso = true;
                        } else{
                            comunicaAGiocatoreCorrente("Non puoi vendere le carte scelte!");
                            return false;
                        }
                    } else {
                        comunicaAGiocatoreCorrente("Hai già venduto carte permesso di costruzione!");
                    }
                    break;
                case CARTE_POLITICA:
                    if (!venduteCartePolitica) {
                        List<String> cartePolitica = (List<String>) vendibile.getOggetto();
                        HashMap<String, Integer> mappaCartePoliticaVendibili = Utility.listToHashMap(cartePolitica);
                        List<ColoreCartaPolitica> manoColoriCartePolitica = giocatoreCorrente.getColoriCartePolitica();
                        List<String> manoStringheColoriCartePolitica = new ArrayList<>();
                        manoColoriCartePolitica.forEach((ColoreCartaPolitica colore) -> {
                            manoStringheColoriCartePolitica.add(colore.toString());
                        });
                        HashMap<String, Integer> mappaCartePoliticaGiocatore = Utility.listToHashMap(manoStringheColoriCartePolitica);
                        if (Utility.hashMapContainsAllWithDuplicates(mappaCartePoliticaGiocatore, mappaCartePoliticaVendibili)) {
                            vetrinaMarket.aggiungiVendibile(vendibile);
                            venduteCartePolitica = true;
                        } else {
                            comunicaAGiocatoreCorrente("Non puoi vendere le carte scelte!");
                            return false;
                        }
                    } else {
                        comunicaAGiocatoreCorrente("Hai già venduto carte politica");
                    }
                    break;
                case AIUTANTI:
                    if (!vendutiAiutanti) {
                        int numeroAiutanti = (Integer) vendibile.getOggetto();
                        if (giocatoreCorrente.getAiutanti() - numeroAiutanti < 0) {
                            comunicaAGiocatoreCorrente("Non hai abbastanza aiutanti!");
                            return false;
                        } else {
                            vetrinaMarket.aggiungiVendibile(vendibile);
                            vendutiAiutanti = true;
                        }
                    } else {
                        comunicaAGiocatoreCorrente("Hai già venduto aiutanti!");
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    //questo metodo permette di comprare oggetti esposti in vetrina market
    //viene controllato se il giocatore che desidera acquistare abbia un numero sufficiente di monete per procedere all'acquisto
    @Override
    public boolean compra(List<Vendibile> vendibili) throws RemoteException {
        if (!faseAcquistoMarket){
            comunicaAGiocatoreCorrente("Non puoi acquistare in questo momento!");
            return false;
        }
        int costoTotale = 0;
        for (Vendibile vendibile : vendibili) {
            costoTotale += vendibile.getPrezzo();
        }
        if (giocatoreCorrente.getMonete() - costoTotale < 0) {
            comunicaAGiocatoreCorrente("Non hai abbastanza monete!");
            return false;
        }
        int idGiocatore;

        HashMap<Vendibile, Integer> mappaVendibiliRichiesti = Utility.listToHashMap(vendibili);
        HashMap<Vendibile, Integer> mappaVendibiliVetrina = Utility.listToHashMap(vetrinaMarket.getVendibili());

        if (Utility.hashMapContainsAllWithDuplicates(mappaVendibiliVetrina, mappaVendibiliRichiesti)) {
            for (Vendibile vendibile :  vendibili) {
                idGiocatore = vendibile.getIdGiocatore();
                Giocatore giocatore = getGiocatoreDaPartitaConId(idGiocatore);
                //rimuovo i vendibili dal giocatore che li ha messi in vendita e dalla vetrina e li aggiungo al giocatore corrente
                switch (vendibile.getIdVendibile()) {
                    case CARTE_PERMESSO_COSTRUZIONE:
                        vetrinaMarket.rimuoviVendibile(vendibile);
                        giocatore.guadagnaMonete(vendibile.getPrezzo());
                        List<CartaPermessoCostruzione> cartePermesso = (List<CartaPermessoCostruzione>) vendibile.getOggetto();
                        ArrayList<CartaPermessoCostruzione> carteScartate = giocatore.scartaCartePermessoCostruzione(cartePermesso);
                        carteScartate.forEach((CartaPermessoCostruzione carta) -> {
                            giocatoreCorrente.addCarta(carta);
                        });
                        break;
                    case CARTE_POLITICA:
                        vetrinaMarket.rimuoviVendibile(vendibile);
                        giocatore.guadagnaMonete(vendibile.getPrezzo());
                        List<String> cartePolitica = (List<String>) vendibile.getOggetto();
                        List<ColoreCartaPolitica> listaColori = new ArrayList<>();
                        cartePolitica.forEach((String colore) -> {
                            listaColori.add(ColoreCartaPolitica.valueOf(colore));
                        });
                        List<CartaPolitica> cartePoliticaScartate = giocatore.scartaCartePolitica(listaColori);
                        cartePoliticaScartate.forEach((CartaPolitica cartaPolitica) -> {
                            giocatoreCorrente.addCarta(cartaPolitica);
                        });
                        break;
                    case AIUTANTI:
                        vetrinaMarket.rimuoviVendibile(vendibile);
                        giocatore.guadagnaMonete(vendibile.getPrezzo());
                        try {
                            int aiutanti = (Integer) vendibile.getOggetto();
                            giocatore.pagaAiutanti(aiutanti);
                            giocatoreCorrente.guadagnaAiutanti(aiutanti);
                        } catch (AiutantiNonSufficientiException exc){
                            exc.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
            try {
                giocatoreCorrente.pagaMonete(costoTotale);
            } catch (MoneteNonSufficientiException exc){
                exc.printStackTrace();
                return false;
            }
        } else {
            comunicaAGiocatoreCorrente("Uno dei vendibili scelti non è disponibile!");
            return false;
        }
        return false;
    }

    private Giocatore getGiocatoreDaPartitaConId(int idGiocatore) {
        for (Giocatore giocatore : partita.getGiocatori()) {
            if (giocatore.getId() == idGiocatore) {
                return giocatore;
            }
        }
        throw new IllegalArgumentException("Non esiste un giocatore con questo Id!");
    }

    //questo metodo effettua il logout del giocatore con l'id passato in input chiudendo tutte le connessioni socket e fermando i thread per la comunicazione socket, se presenti
    @Override
    public void logout(int idGiocatoreOffline) throws RemoteException {
        if (idGiocatoreOffline < giocatoreCorrente.getId()) {
            views.forEach((InterfacciaView view) -> {
                try {
                    if (view.getIdGiocatore() == idGiocatoreOffline) {
                        view.mostraMessaggio("Sei offline!");
                        view.logOut();
                    } else view.mostraMessaggio("Giocatore " + idGiocatoreOffline + " è offline!");
                } catch (RemoteException exc) {
                    exc.printStackTrace();
                }
            });
            giocatoriOnline.aggiungiGiocatoreDaEliminare(idGiocatoreOffline);
        } else if (idGiocatoreOffline > giocatoreCorrente.getId()) {
            views.forEach((InterfacciaView view) -> {
                try {
                    if (view.getIdGiocatore() == idGiocatoreOffline) {
                        view.mostraMessaggio("Sei offline!");
                        view.logOut();
                    } else view.mostraMessaggio("Giocatore " + idGiocatoreOffline + " è offline!");
                } catch (RemoteException exc) {
                    exc.printStackTrace();
                }
            });
            giocatoriOnline.eliminaGiocatore(idGiocatoreOffline);
        } else {
            comunicaAGiocatoreCorrente("Sei offline!");
            comunicaAdAltriGiocatori("Giocatore " + giocatoreCorrente.getId() + " è offline!");
            viewCorrenteRimossa = true;
            getViewGiocatoreCorrente().logOut();
            giocatoriOnline.aggiungiGiocatoreDaEliminare(idGiocatoreOffline);
        }
        for (Iterator<InterfacciaView> iterator = views.iterator(); iterator.hasNext(); ) {
            InterfacciaView view = iterator.next();
            if (view.getIdGiocatore() == idGiocatoreOffline) {
                iterator.remove();
            }
        }
        if (idGiocatoreOffline == giocatoreCorrente.getId()) {
            synchronized (this) {
                notify();
            }
        }
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

    //questo metodo calcola quante monete sono da pagare per soddisfare un consiglio con le carte indicate
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

    private void prendiCartePoliticaGiocatore(Giocatore giocatore, List<ColoreCartaPolitica> coloriCartePolitica){
            ArrayList<CartaPolitica> cartePoliticaScartate = giocatore.scartaCartePolitica(coloriCartePolitica);
            cartePoliticaScartate.forEach((cartaPolitica) -> cartaPolitica.setVisibile(false));
            partita.addCartePoliticaScartate(cartePoliticaScartate);
    }

    //il metodo costruisci emporio, effettuati alcuni controlli, aggiunge un emporio del giocatore corrente alla città richiesta
    //vengono assegnati i bonus della città dove si è costruito e tutti i bonus delle città collegate (se è presente un emporio dello stesso giocatore) tramite l'algoritmo bfs
    //a cui è passato un consumer che gli darà la caratteristica di fermare l'esplorazione del grafo quando incontra una città in cui non è presente un emporio del giocatore corrrente
    //poi viene controllato tramite l'algoritmo di dfs se il giocatore ha costruito oppure no in tutte le città di uno stesso colore o in tutte le città di una regione
    private boolean costruisciEmporio(NomeCittà nomeCittàCostruzione){
        Città cittàCostruzione = getCittàDaNome(nomeCittàCostruzione);
        Regione regione = getRegioneDaNomeCittà(nomeCittàCostruzione);
        ArrayList<Città> cittàCollegateRitornate;
        if (cittàCostruzione.giàCostruito(giocatoreCorrente)) {
            comunicaAGiocatoreCorrente("Hai già costruito in questa città!");
            return false;
        }
        int numeroAiutanti = CostantiModel.NUMERO_AIUTANTI_PAGARE_EMPORIO * cittàCostruzione.getNumeroEmporiCostruiti();
        try {
            giocatoreCorrente.pagaAiutanti(numeroAiutanti);
        } catch (AiutantiNonSufficientiException exc){
            comunicaAGiocatoreCorrente("Ti servono " + numeroAiutanti + " per cotruire in questa città!");
            return false;
        }

        cittàCostruzione.costruisciEmporio(new Emporio(giocatoreCorrente.getId()));
        giocatoreCorrente.decrementaEmporiDisponibili();
        if (giocatoreCorrente.getEmporiDisponibili() == 0){
            giocatoreCorrente.guadagnaPuntiVittoria(CostantiModel.PUNTI_VITTORIA_GUADAGNATI_COSTRUZIONE_ULTIMO_EMPORIO);
            comunicaAGiocatoreCorrente("Hai guadagnato 3 punti vittoria per essere stato il primo giocatore ad utilizzare tutti gli empori!");
        }

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
            try {
                assegnaBonus(partita.ottieniCartaBonusColoreCittà(cittàCostruzione.getColore()).getBonus());
                comunicaAGiocatoreCorrente("Complimenti! Hai ottenuto una tessera bonus colore città per aver costruito in tutte le città del colore " + cittàCostruzione.getColore());
                comunicaAGiocatoreCorrente("Giocatore " + giocatoreCorrente.getId() + " ha ottenuto una tessera bonus colore città per aver costruito in tutte le città del colore " + cittàCostruzione.getColore());
            } catch (NoSuchElementException exc){
                comunicaAGiocatoreCorrente("Hai costruito in tutte le città di uno stesso colore ma la carta bonus colore città non è disponibile!");
            }
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
            try{
                assegnaBonus(regione.ottieniCartaBonusRegione().getBonus());
                comunicaAGiocatoreCorrente("Complimenti! Hai ottenuto una tessera bonus regione per aver costruito in tutte le città della regione " + cittàCostruzione.getNomeRegione());
                comunicaAGiocatoreCorrente("Giocatore " + giocatoreCorrente.getId() + " ha ottenuto una tessera bonus regione per aver costruito in tutte le città della regione " + cittàCostruzione.getNomeRegione());
            } catch (NoSuchElementException exc){
                comunicaAGiocatoreCorrente("Hai costruito in tutte le città di una regione ma la carta bonus regione non è disponibile!");
            }
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

    //GiocatoriOnLine è una classe innestata che mi permette di gestire i turni e il logout
    private class GiocatoriOnline {
        private ArrayList<Giocatore> giocatoriOnline;
        private int posizione;
        private ArrayList<Giocatore> giocatoriDaEliminare;

        GiocatoriOnline(){
            this.giocatoriOnline = new ArrayList<>();
            this.giocatoriDaEliminare = new ArrayList<>();
            posizione = -1;
        }

        synchronized boolean haProssimo(){
            return !(posizione == (giocatoriOnline.size() - 1));
        }

        synchronized Giocatore prossimo(){
            if (this.haProssimo()) {
                return giocatoriOnline.get(++posizione);
            } else {
                eliminaGiocatoriOffline();
                if (giocatoriOnline.size() == 0) {
                    return null;
                }
                posizione = 0;
                return giocatoriOnline.get(posizione);
            }
        }

        synchronized void aggiungiGiocatoreDaEliminare(int idGiocatore){
            giocatoriOnline.forEach((Giocatore giocatore) -> {
                if (giocatore.getId() == idGiocatore) {
                    giocatoriDaEliminare.add(giocatore);
                }
            });
        }

        private synchronized void eliminaGiocatoriOffline(){
            giocatoriDaEliminare.forEach((Giocatore giocatore) -> {
                for (Iterator<Giocatore> iterator = giocatoriOnline.iterator(); iterator.hasNext(); ){
                    Giocatore giocatoreOnline = iterator.next();
                    if (giocatoreOnline.getId() == giocatore.getId()) {
                        iterator.remove();
                    }
                }
            });
            giocatoriDaEliminare = new ArrayList<>();
        }

        synchronized void eliminaGiocatore(int idGiocatore){
            giocatoriOnline.forEach((Giocatore giocatore) -> {
                for (Iterator<Giocatore> iterator = giocatoriOnline.iterator(); iterator.hasNext(); ){
                    Giocatore giocatoreOnline = iterator.next();
                    if (giocatoreOnline.getId() == idGiocatore) {
                        iterator.remove();
                    }
                }
            });
        }

        synchronized void aggiungiGiocatore(Giocatore giocatore){
            giocatoriOnline.add(giocatore);
        }

        ArrayList<Integer> getIdGiocatori(){
            ArrayList<Integer> ids = new ArrayList<>();
            giocatoriOnline.forEach((Giocatore giocatore) -> {ids.add(giocatore.getId());});
            return ids;
        }

    }

    //ScatolaIdGiocatori è una classe innestata che mi permette di pescare degli idGiocatore casuali
    private class ScatolaIdGiocatori{
        private ArrayList<Integer> numeri;
        private Random random = new Random();
        private int posizioneCasuale;

        ScatolaIdGiocatori(){
            numeri = new ArrayList<>();
            numeri.addAll(giocatoriOnline.getIdGiocatori());
        }

        int pescaNumero(){
            posizioneCasuale = random.nextInt(numeri.size());
            return numeri.remove(posizioneCasuale);
        }

        boolean èVuota(){
            return numeri.isEmpty();
        }

    }
}



