package server.sistema;

import classicondivise.bonus.*;
import classicondivise.carte.CartaPermessoCostruzione;
import classicondivise.IdBalcone;
import classicondivise.NomeCittà;
import interfaccecondivise.InterfacciaView;
import server.controller.Controller;
import server.model.*;
import server.model.carte.*;
import server.proxyView.SocketProxyView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
questa classe implementa la creazione della partita, ed il popolamento dell'intero model con la configurazione iniziale
della partita definita dalle regole del gioco. Il flusso è eseguito in un thread separato, perchè esegue operazioni
bloccanti, in particolare chiede ad un giocatore di scegliere la mappa.
 */
public class AvviatorePartita implements Runnable {
    private int numeroBindRegistry;
    private ArrayList<InterfacciaView> proxyViews;
    private static ExecutorService executors = Executors.newCachedThreadPool();
    private int idMappa;

    public AvviatorePartita(ArrayList<InterfacciaView> proxyViews, int numeroBindRegistry) {
        this.proxyViews = proxyViews;
        this.numeroBindRegistry = numeroBindRegistry;
    }

    public void setMappa(int idMappa){this.idMappa = idMappa;}

    @Override
    public void run() {
        Properties pro = sceltaMappa();
        Partita nuovaPartita = creaPartita(pro); //crea l'ambiente della partita senza aggiungere i giocatori
        ArrayList<SocketPollingController> socketPollingControllers = new ArrayList<>();
        if (!Thread.currentThread().isInterrupted()) {
            try {
                Controller controller = new Controller(nuovaPartita, proxyViews);
                //crea i socketPolling per il controller
                proxyViews.stream().filter((InterfacciaView view) -> view instanceof SocketProxyView).forEach((InterfacciaView view) -> socketPollingControllers.add(new SocketPollingController(controller, ((SocketProxyView)view).getOis())));
                socketPollingControllers.forEach((SocketPollingController runnable) -> {new Thread(runnable).start();});
                Registry registry = LocateRegistry.getRegistry(CostantiSistema.RMI_PORT);
                String chiaveController = PrefissiChiaviRMI.PREFISSO_CHIAVE_CONTROLLER + numeroBindRegistry ;
                registry.bind(chiaveController, controller);
                for(InterfacciaView view : proxyViews) { //non uso espressione lambda perchè dovrei innestare un try catch che è già fatto
                    view.iniziaAGiocare(idMappa);
                }
                //vengono aggiunti i giocatori alla partita, in modo che le update vadano a buon fine, perchè
                //le view conoscono già il proprio id
                ArrayList<Giocatore> giocatori = creaGiocatori(nuovaPartita);
                giocatori.forEach(nuovaPartita::addGiocatore);
                executors.submit(controller);
            }catch (RemoteException | AlreadyBoundException e){
                System.out.println("impossibile bindare controller");
                e.printStackTrace();
                proxyViews.forEach((InterfacciaView view) -> {
                    try {
                        view.erroreDiConnessione();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                });
            }
        }
    }

    //crea l'ambiente della partita senza aggiungere i giocatori
    private Partita creaPartita(Properties pro){
        Partita partita = new Partita(proxyViews);
        partita.aggiungiAiutanti(CostantiModel.NUM_AIUTANTI);
        ArrayList<Consigliere> riserva = creaRiservaConsiglieri();
        HashSet<Città> tutteLeCittà = creaCittàDaFile(pro);
        creaSentieriCittàDaFile(pro, tutteLeCittà);
        partita.setRegioni(creaRegioni(pro, tutteLeCittà, riserva));
        partita.setBalconeDelConsiglioRe(creaBalcone(IdBalcone.RE, riserva));
        partita.setRe(creaRe(tutteLeCittà));
        partita.setPercorsoDellaNobiltà(creaPercorsoNobiltà());
        partita.setMazzoCartePolitica(creaMazzoCartePolitica());
        partita.setMazzoCartePremioRe(creaMazzoCartePremioRe());
        partita.setCarteBonusColoreCittà(creaMazzoBonusColoreCittà());
        partita.setRiservaConsiglieri(riserva);
        return partita;
    }

    private ArrayList<Consigliere> creaRiservaConsiglieri() {
        ArrayList<Consigliere> consiglieri = new ArrayList<>();
        Arrays.stream(ColoreConsigliere.values()).forEach((ColoreConsigliere colore) -> {
            for (int i = 0; i < CostantiModel.NUM_CONSIGLIERI_PER_COLORE; i++)
                consiglieri.add(new Consigliere(colore));
        });
        Collections.shuffle(consiglieri);
        return consiglieri;
    }

    //crea un insieme contenente tutte le città, leggendo da file i colori e le regioni
    private HashSet<Città> creaCittàDaFile(Properties pro){
        HashSet<Città> tutteLeCittà = new HashSet<>();
        try {
            /*
            ottiene una hashmap contenente nome città come chiave, nome regione come valore
             */
            HashMap<NomeCittà, NomeRegione> nomeCittàNomeRegioneHashMap = new HashMap<>();
            Arrays.stream(NomeRegione.values()).forEach(nomeRegione -> {
                String stringNomiCittàInRegione = pro.getProperty(nomeRegione.toString());
                Arrays.stream(stringNomiCittàInRegione.split(",")).forEach((String nomeCittà) -> nomeCittàNomeRegioneHashMap.put(NomeCittà.valueOf(nomeCittà), nomeRegione));
            });
            /*
            ottiene una hashMap contenente nome città come chiave, colore come valore
             */
            HashMap<NomeCittà, ColoreCittà> nomeCittàColoreCittàHashMap = new HashMap<>();
            Arrays.stream(NomeCittà.values()).forEach((NomeCittà nomeCittà) -> {
                String stringColore = pro.getProperty(nomeCittà.toString()+ ".colore");
                nomeCittàColoreCittàHashMap.put(nomeCittà, ColoreCittà.valueOf(stringColore));
            });
            /*
            riempie l'hashset di nuove città, sfruttando le informazioni contenute nelle hashmap
             */
            Arrays.stream(NomeCittà.values()).forEach((NomeCittà nomeCittà) -> tutteLeCittà.add(new Città(nomeCittàNomeRegioneHashMap.get(nomeCittà),nomeCittà, nomeCittàColoreCittàHashMap.get(nomeCittà),
                    creaBonus(BonusPescaCartaPolitica.class, BonusMonete.class, BonusAiutanti.class), proxyViews)));
            return tutteLeCittà;
        }catch(NullPointerException e) {
            System.out.println("impossibile creare le città da file");
            proxyViews.forEach((InterfacciaView view) -> {
                try {
                    view.erroreDiConnessione();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            });
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /*
    leggendo dal file di configurazione i link tra le città, aggiunge all'insieme
    di città che prende in input le città ada esse adiacentiadiacendi
     */
    private void creaSentieriCittàDaFile(Properties pro, HashSet<Città> tutteLeCittà){
        HashMap<String, Città> mapCittà = new HashMap<>(); //mappa conetente (nomecittà, città) utile per l'algoritmo
        tutteLeCittà.forEach((Città città) -> mapCittà.put(città.getNome().toString(), città));
        try {
            tutteLeCittà.stream().forEach((Città città) -> {
                String chiaveProprietà = città.getNome().toString() + ".cittaAdiacenti";
                String stringaCittàAdiacenti = pro.getProperty(chiaveProprietà); //ottiene una stringa del tipo "NOMECITTà1,NOMECITTà2"
                String[] nomiCittàAdiacente = stringaCittàAdiacenti.split(","); //ottiene un array di stringhe contenente i nomi delle città
                Arrays.stream(nomiCittàAdiacente).forEach((String nomeCittà) -> città.addCittàAdiacenti(mapCittà.get(nomeCittà)));
            });
        }catch (NullPointerException e) {
            System.out.println("impossibile assegnare città adiacenti");
            proxyViews.forEach((InterfacciaView view) -> {
                try {
                    view.erroreDiConnessione();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            });
            Thread.currentThread().interrupt();
        }
    }

    /*
    crea le regioni leggendo dal file di configurazione della mappa quali sono le città che appartengono alla
    regione in creazione. Le regioni contengono ognuna un mazzo carte permesso. Questi vengono creati chaimando un altro
    metodo, ed aggiunti alla regione corrispindente
     */
    private HashSet<Regione> creaRegioni(Properties pro, HashSet<Città> tutteLeCittà, ArrayList<Consigliere> riservaConsiglieri) {
        HashSet<Regione> regioni = new HashSet<>();
        Arrays.stream(NomeRegione.values()).forEach((NomeRegione nomeRegione) -> {
            ArrayList<Città> cittàRegione = new ArrayList<>(CostantiModel.NUM_CITTA_PER_REGIONE);
            cittàRegione.addAll(cittàInRegioniDaFile(pro, nomeRegione, tutteLeCittà)); //cittàRegione contiene solo le città apparteneti alla regione corrente
            Mazzo<CartaPermessoCostruzione> mazzoCartePermesso = creaMazzoCartePermesso(cittàRegione);
            mazzoCartePermesso.mischia();
            CartaBonusRegione cartaBonusRegione = new CartaBonusRegione(nomeRegione, CostantiModel.PUNTI_VITTORIA_BONUS_REGIONE);
            BalconeDelConsiglio balcone = creaBalcone(IdBalcone.valueOf(nomeRegione.toString()), riservaConsiglieri);
            Regione regione = new Regione(nomeRegione, mazzoCartePermesso, balcone, cartaBonusRegione, proxyViews);
            cittàRegione.forEach(regione::addCittà);
            regioni.add(regione);
        });
        return regioni;
    }

    /*
    crea un mazzo carte permesso aventi città possibili in cui costruire quelle contenute in città
    prende in input un arrayList di città che appartengono ad una determinata regione
     */
    private Mazzo<CartaPermessoCostruzione> creaMazzoCartePermesso(ArrayList<Città> cittàs) {
        Mazzo<CartaPermessoCostruzione> mazzo = new Mazzo<>();
        //crea 5 carte con una sola città
        cittàs.forEach((Città città) -> {
            HashSet<NomeCittà> cittàCarta = new HashSet<>(1);
            cittàCarta.add(città.getNome());
            mazzo.addCarta(new CartaPermessoCostruzione(creaBonus(
                    BonusAiutanti.class, BonusMonete.class, BonusPescaCartaPolitica.class, BonusAvanzaPercorsoNobiltà.class, BonusPuntiVittoria.class
            ), cittàCarta));
        });
        //crea le restanti NUM_CARTE_PERMESSO_REGIONE -5 carte permesso, assegnando città randomiche
        Random rand = new Random();
        for (int i = 0; i < CostantiModel.NUM_CARTE_PERMESSO_REGIONE - 5; i++) {
            HashSet<NomeCittà> cittàCarta = new HashSet<>(2);
            int numCittàCarta = 2 + rand.nextInt(2); //numCittàCarta può valere 2 oppure 3
            ArrayList<Città> cittàPossibili = new ArrayList<>();
            cittàs.forEach(cittàPossibili::add); //città possibili contiene le città che possono essere assegnate alla carta corrente
            for (int j = 0; j < numCittàCarta; j++) {
                int posizioneCittàDaAggiungere = rand.nextInt(cittàPossibili.size());
                cittàCarta.add(cittàPossibili.get(posizioneCittàDaAggiungere).getNome());
                cittàPossibili.remove(posizioneCittàDaAggiungere); //permette di evitare di aggiungere due volte la stessa città
            }
            mazzo.addCarta(new CartaPermessoCostruzione(creaBonus(
                    BonusAiutanti.class, BonusMonete.class, BonusPescaCartaPolitica.class, BonusAvanzaPercorsoNobiltà.class, BonusPuntiVittoria.class
            ), cittàCarta));
        }
        return mazzo;
    }

    /*
    rimuovendo i consiglieri dalla riserva già creata, li aggiunge al balcone
     */
    private BalconeDelConsiglio creaBalcone(IdBalcone id, ArrayList<Consigliere> riservaConsiglieri){
        ArrayList<Consigliere> consiglieri = new ArrayList<>();
        for (int i = 0; i < CostantiModel.NUM_CONSIGLIERI_BALCONE; i++) {
            consiglieri.add(riservaConsiglieri.remove(riservaConsiglieri.size()-1));
        }
        return new BalconeDelConsiglio(id, proxyViews, consiglieri);
    }

    //prende in input tutte le citta assegna alla regione richiesta quelle giuste leggendolo da file
    private HashSet<Città> cittàInRegioniDaFile(Properties pro, NomeRegione nomeRegione, HashSet<Città> tutteLeCittà){
        HashMap<String, Città> mapCittà = new HashMap<>();
        HashSet<Città> appartenentiRegione = new HashSet<>();
        //crea una mappa contenente (NomeCittà, città) utile per l'algoritmo
        tutteLeCittà.forEach((Città città) -> mapCittà.put(città.getNome().toString(), città));
        String cittàStringa = pro.getProperty(nomeRegione.toString()); //ottiene una stringa del tipo "NOMECITTà1,NOMECITTà2"
        String[] nomiCittà = cittàStringa.split(","); //ottiene un array di stringhe contenente il nome delle città
        Arrays.stream(nomiCittà).forEach((String nomeCittà) -> appartenentiRegione.add(mapCittà.get(nomeCittà)));
        return appartenentiRegione;
    }

    private Re creaRe(HashSet<Città> tutteLeCittà) {
        NomeCittà nomeCittàRe = NomeCittà.valueOf(CostantiModel.CITTÀ_RE);
        for (Città città : tutteLeCittà) {
            if (città.getNome() == nomeCittàRe) {
                return new Re (città, proxyViews);
            }
        }
        //se la città re non è stata trovata
        proxyViews.forEach((InterfacciaView view) -> {
            try {
                view.erroreDiConnessione();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        });
        Thread.currentThread().interrupt();
        return null;
    }

    private List<Bonus> creaPercorsoNobiltà () {
        List<Bonus> percorso = new ArrayList<>(CostantiModel.MAX_POS_NOBILTA);
        Random random = new Random();
        for(int i = 0; i < CostantiModel.MAX_POS_NOBILTA; i++){
            if(random.nextDouble() < CostantiModel.PERCENTUALE_BONUS_PERCORSO_NOBILTA){
                percorso.add(creaBonus(
                        BonusAiutanti.class, BonusMonete.class, BonusPescaCartaPolitica.class, BonusRipetiAzionePrincipale.class, BonusPuntiVittoria.class
                ));
            }
            else percorso.add(NullBonus.getInstance());
        }
        percorso.add(new BonusPuntiVittoria(CostantiModel.MAX_POS_NOBILTA, NullBonus.getInstance()));
        return percorso;
    }

    private Mazzo<CartaPolitica> creaMazzoCartePolitica () {
        Mazzo<CartaPolitica> mazzo = new Mazzo<>();
        Arrays.stream(ColoreCartaPolitica.values()).forEach((ColoreCartaPolitica colore) ->{
            for (int i = 0; i < CostantiModel.NUM_CARTE_POLITICA_PER_COLORE; i++) {
                mazzo.addCarta(new CartaPolitica(colore));
            }
        });

        mazzo.mischia();
        return mazzo;
    }

    private Mazzo<CartaPremioDelRe> creaMazzoCartePremioRe() {
        Mazzo<CartaPremioDelRe> mazzo = new Mazzo<>();
        for (int i = 0; i < CostantiModel.NUM_CARTE_PREMIO_RE; i++) {
            CartaPremioDelRe carta = new CartaPremioDelRe(CostantiModel.PUNTI_CARTA_PREMIO_RE[i]);
            carta.setVisibile(true);
            mazzo.addCarta(carta);
        }
        return mazzo;
    }

    private HashSet<CartaBonusColoreCittà> creaMazzoBonusColoreCittà () {
        HashMap<ColoreCittà, Integer> puntiColoreMap = new HashMap<>();
        puntiColoreMap.put(ColoreCittà.ORO, CostantiModel.PUNTI_BONUS_COLORE_CITTA_ORO);
        puntiColoreMap.put(ColoreCittà.ARGENTO, CostantiModel.PUNTI_BONUS_COLORE_CITTA_ARGENTO);
        puntiColoreMap.put(ColoreCittà.BRONZO, CostantiModel.PUNTI_BONUS_COLORE_CITTA_BRONZO);
        puntiColoreMap.put(ColoreCittà.FERRO, CostantiModel.PUNTI_BONUS_COLORE_CITTA_FERRO);
        HashSet<CartaBonusColoreCittà> carte = new HashSet<>();
        Arrays.stream(ColoreCittà.values()).filter(coloreCittà -> coloreCittà!=ColoreCittà.CITTA_RE).forEach((ColoreCittà colore) -> {
            CartaBonusColoreCittà carta = new CartaBonusColoreCittà(puntiColoreMap.get(colore), colore);
            carte.add(carta);
        });
        return carte;
    }

    /*
    crea i giocatori nel loro stato iniziale, che differisce per l'ordine di turno
     */
    private ArrayList<Giocatore> creaGiocatori(Partita partita) {
        ArrayList<Giocatore> giocatori = new ArrayList<>();
        for (int i = 0; i < proxyViews.size(); i++) {
            InterfacciaView viewCorrente = proxyViews.get(i);
            int idGiocatore = 0;
            try {
                idGiocatore = viewCorrente.getIdGiocatore();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            partita.decrementaAiutanti(CostantiModel.AIUTANTI_INIZIALI_GIIOCATORI[i]);
            Giocatore giocatore = new Giocatore(idGiocatore, CostantiModel.MONETE_INIZIALI_GIOCATORI[i], CostantiModel.AIUTANTI_INIZIALI_GIIOCATORI[i], proxyViews);
            for (int j = 0; j < CostantiModel.NUM_CARTE_POLITICA_INIZIALI_GIOCATORE; j++) {
                giocatore.addCarta(partita.ottieniCartaPolitica());
            }
            giocatori.add (giocatore);
        }
        return giocatori;
    }
    /*
    effetta la comunicazione con il primo client loggato per la scelta della mappa, ed ottenuto l'id della mappa,
    carica il file di properties che contiene la configurazione della mappa
     */
    private Properties sceltaMappa(){
        if(proxyViews.get(0) instanceof SocketProxyView) { //il primo giocatore loggato sceglie la mappa
            SocketProxyView proxyView = (SocketProxyView) proxyViews.get(0);
            proxyView.setAvviatore(this);
            try {
                proxyViews.get(0).scegliMappa();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            sceltaMappaRMI(proxyViews.get(0));
        }
        String nomeFile = "mappa"+idMappa;
        System.out.println("nomeFile = " + nomeFile);
        try {
            FileInputStream is = new FileInputStream("./serverResources/fileconfigmappe/"+nomeFile);
            Properties pro = new Properties();
            pro.load(is);
            return pro;
        }catch(FileNotFoundException e) {
            System.out.println("impossibile trovare il file di configurazione della mappa");
            proxyViews.forEach((InterfacciaView view) -> {
                try {
                    view.erroreDiConnessione();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            });
            Thread.currentThread().interrupt();
            return null;
        } catch (IOException e) {
            System.out.println("impossibile trovare il file di configurazione della mappa");
            proxyViews.forEach((InterfacciaView view) -> {
                try {
                    view.erroreDiConnessione();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            });
            Thread.currentThread().interrupt();
            return null;
        }
    }

    private void sceltaMappaRMI(InterfacciaView view) {
        try {
            synchronized (this){
                Registry registry = LocateRegistry.getRegistry(CostantiSistema.RMI_PORT);
                String chiaveSceltaMappa = PrefissiChiaviRMI.PREFISSO_CHIAVE_SCELTA_MAPPA + numeroBindRegistry;
                registry.bind(chiaveSceltaMappa, new SceltaMappaRMI(this));
                view.scegliMappa();
                wait();
            }
        }catch(RemoteException | AlreadyBoundException | InterruptedException e){
            e.printStackTrace();
        }
    }

    //friendly
    void mappaSettata(){
        synchronized (this) {
            notify();
        }
    }

    /*
    la creazione dei bonus (che sono composti da sottobonus) prende in input le classi
    che corrispondono ai sottobonus componente il bonus che si vuole creare. In questo modo
    è possibile differenziare i bonus in base a che siano bonus su carte permesso, città o
    percorso della nobiltà
     */
    private Bonus creaBonus(Class... tipiBonus) {
        Random rand = new Random();
        Bonus bonus = NullBonus.getInstance();
        ArrayList<Class> bonusNonUtilizzati = new ArrayList<>();
        for(Class c : tipiBonus){
            bonusNonUtilizzati.add(c);
        }
        int numeroSottobonus = CostantiModel.MIN_NUM_SOTTOBONUS_PER_BONUS + rand.nextInt(CostantiModel.MAX_NUM_SOTTOBONUS_PER_BONUS - CostantiModel.MIN_NUM_SOTTOBONUS_PER_BONUS) + 1;
        for (int i = 0; i < numeroSottobonus; i++) {
            int valoreBonus = CostantiModel.MIN_VALORE_SOTTOBONUS + rand.nextInt(CostantiModel.MAX_VALORE_SOTTOBONUS - CostantiModel.MIN_VALORE_SOTTOBONUS) + 1;
            int sceltaBonus = rand.nextInt(bonusNonUtilizzati.size());
            switch (bonusNonUtilizzati.get(sceltaBonus).toString()){
                case "class classicondivise.bonus.BonusAiutanti":
                    bonus = new BonusAiutanti(valoreBonus, bonus);
                    break;
                case "class classicondivise.bonus.BonusMonete":
                    bonus = new BonusMonete(valoreBonus, bonus);
                    break;
                case "class classicondivise.bonus.BonusPuntiVittoria":
                    bonus = new BonusPuntiVittoria(valoreBonus, bonus);
                    break;
                case "class classicondivise.bonus.BonusAvanzaPercorsoNobiltà":
                    bonus = new BonusAvanzaPercorsoNobiltà(valoreBonus, bonus);
                    break;
                case "class classicondivise.bonus.BonusPescaCartaPolitica":
                    bonus = new BonusPescaCartaPolitica(valoreBonus, bonus);
                    break;
                case "class classicondivise.bonus.BonusRipetiAzionePrincipale":
                    bonus = new BonusRipetiAzionePrincipale(bonus);
                default:
                    break;
            }
            bonusNonUtilizzati.remove(sceltaBonus);
        }
        return bonus;
    }
}


