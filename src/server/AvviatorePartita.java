package server;

import controller.Controller;
import model.*;
import model.bonus.*;
<<<<<<< HEAD:src/AvviatorePartita.java
import model.carte.*;
import proxyview.InterfacciaView;
=======
import model.carte.CartaBonusRegione;
import model.carte.CartaPermessoCostruzione;
import proxyView.InterfacciaView;
import proxyView.RMIProxyView;
import proxyView.SocketProxyView;
>>>>>>> 49ed980a4b646110199dc74b98dd91251d947e73:src/server/AvviatorePartita.java

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AvviatorePartita implements Runnable {
    private ArrayList<InterfacciaView> proxyViews;
    private static ExecutorService executors = Executors.newCachedThreadPool();

    public AvviatorePartita(ArrayList<InterfacciaView> proxyViews) {
        this.proxyViews = proxyViews;
    }

    @Override
    public void run() {
        Partita nuovaPartita = creaPartita(proxyViews);
        if (!Thread.currentThread().isInterrupted()) {
            executors.submit(new Controller(nuovaPartita, proxyViews));
        }
    }

    private Partita creaPartita(ArrayList<InterfacciaView> proxyViews){
        FileInputStream fileMappa = sceltaMappa(proxyViews);
        Partita partita = new Partita(proxyViews);
        partita.aggiungiAiutanti(Costanti.NUM_AIUTANTI);
        partita.setRiservaConsiglieri(creaRiservaConsiglieri());
        HashSet<Città> tutteLeCittà = creaCittàDaFile(fileMappa, proxyViews);
        creaSentieriCittàDaFile(fileMappa, tutteLeCittà, proxyViews);
        partita.setRegioni(creaRegioni(fileMappa, tutteLeCittà, partita.getRiservaConsiglieri(), proxyViews));
        partita.setBalconeDelConsiglioRe(creaBalcone(IdBalcone.RE, partita.getRiservaConsiglieri(), proxyViews));
        partita.setRe(creaRe(tutteLeCittà, proxyViews));
        partita.setPercorsoDellaNobiltà(creaPercorsoNobiltà());
        partita.setMazzoCartePolitica(creaMazzoCartePolitica());
        partita.setMazzoCartePremioRe(creaMazzoCartePremioRe());
        partita.setCarteBonusColoreCittà(creaMazzoBonusColoreCittà());
        ArrayList<Giocatore> giocatori = creaGiocatori(proxyViews);
        giocatori.forEach((Giocatore giocatore) -> partita.addGiocatore(giocatore));
        return partita;
    }

    private FileInputStream sceltaMappa(ArrayList<InterfacciaView> proxyViews){
        int idMappa = proxyViews.get(0).scegliMappa(); //il primo giocatore loggato sceglie la mappa
        String nomeFile = "mappa"+idMappa;
        try {
            FileInputStream is = new FileInputStream(nomeFile);
            return is;
        }catch(FileNotFoundException e) {
            System.out.println("impossibile trovare il file di configurazione della mappa");
            proxyViews.forEach((InterfacciaView view) -> view.erroreDiConnessione());
            Thread.currentThread().interrupt();
        }
        finally {
            return null; //solo se lanciata l'eccezione
        }
    }

    private ArrayList<Consigliere> creaRiservaConsiglieri() {
        ArrayList<Consigliere> consiglieri = new ArrayList<>();
        Arrays.stream(ColoreConsigliere.values()).forEach((ColoreConsigliere colore) -> {
            for (int i = 0; i < Costanti.NUM_CONSIGLIERI_PER_COLORE; i++)
                consiglieri.add(new Consigliere(colore));
        });
        return consiglieri;
    }

    //crea un insieme contenente tutte le città, leggendo da file i colori
    private HashSet<Città> creaCittàDaFile(InputStream is, ArrayList<InterfacciaView> proxyViews){
        HashSet<Città> cittàs = new HashSet<>();
        Properties pro = new Properties();
        try {
            pro.load(is);
            Arrays.stream(NomeCittà.values()).forEach((NomeCittà nomeCittà) -> {
                String coloreStringa = pro.getProperty(nomeCittà.toString() + ".colore");
                ColoreCittà colore = ColoreCittà.valueOf(coloreStringa);
                cittàs.add(new Città(nomeCittà, colore, creaBonus(), proxyViews));
            });
            return cittàs;
        }catch (IOException e){
            System.out.println("impossibile trovare file di configurazione mappa");
            proxyViews.forEach((InterfacciaView view) -> view.erroreDiConnessione());
            Thread.currentThread().interrupt();
        }
        finally {
            return null; //solo se è stata lanciata l'accezione
        }
    }

    private void creaSentieriCittàDaFile(InputStream is, HashSet<Città> tutteLeCittà, ArrayList<InterfacciaView> proxyViews){
        Properties pro = new Properties();
        HashMap<String, Città> mapCittà = new HashMap(); //mappa conetente (nomecittà, città) utile per l'algoritmo
        tutteLeCittà.forEach((Città città) -> mapCittà.put(città.getNome().toString(), città));
        try{
            pro.load(is);
            tutteLeCittà.forEach((Città città) ->{
                String stringaCittàAdiacenti = pro.getProperty(città.getNome().toString()); //ottiene una stringa del tipo "NOMECITTà1,NOMECITTà2"
                String[] nomiCittàAdiacente = stringaCittàAdiacenti.split(","); //ottiene un array di stringhe contenente i nomi delle città
                Arrays.stream(nomiCittàAdiacente).forEach((String nomeCittà) -> città.addCittàAdiacenti(mapCittà.get(nomeCittà)));
            });
        }catch (IOException e) {
            System.out.println("impossibile trovare file di configurazione mappa");
            proxyViews.forEach((InterfacciaView view) -> view.erroreDiConnessione());
            Thread.currentThread().interrupt();
        }
    }

    private HashSet<Regione> creaRegioni(InputStream fileMappa, HashSet<Città> tutteLeCittà, ArrayList<Consigliere> riservaConsiglieri, ArrayList<InterfacciaView> proxyViews) {
        HashSet<Regione> regioni = new HashSet<>();
        Arrays.stream(NomeRegione.values()).forEach((NomeRegione nomeRegione) -> {
            ArrayList<Città> cittàRegione = new ArrayList<>(Costanti.NUM_CITTA_PER_REGIONE);
            cittàRegione.addAll(cittàInRegioniDaFile(fileMappa, nomeRegione, tutteLeCittà, proxyViews)); //cittàRegione contiene solo le città apparteneti alla regione corrente
            Mazzo<CartaPermessoCostruzione> mazzoCartePermesso = creaMazzoCartePermesso(cittàRegione);
            mazzoCartePermesso.mischia();
            CartaBonusRegione cartaBonusRegione = new CartaBonusRegione(Costanti.PUNTI_VITTORIA_BONUS_REGIONE);
            BalconeDelConsiglio balcone = creaBalcone(IdBalcone.valueOf(nomeRegione.toString()), riservaConsiglieri, proxyViews);
            regioni.add(new Regione(nomeRegione, mazzoCartePermesso, balcone, cartaBonusRegione, proxyViews));
        });
        return regioni;
    }

    //crea un mazzo carte permesso aventi città possibili in cui costruire quelle contenute in città
    private Mazzo<CartaPermessoCostruzione> creaMazzoCartePermesso(ArrayList<Città> cittàs) {
        Mazzo<CartaPermessoCostruzione> mazzo = new Mazzo<>();
        //crea 5 carte con una sola città
        cittàs.forEach((Città città) -> {
            ArrayList<Città> cittàCarta = new ArrayList(1);
            cittàCarta.add(città);
            mazzo.addCarta(new CartaPermessoCostruzione(creaBonus(), cittàCarta));
        });
        //crea le restanti NUM_CARTE_PERMESSO_REGIONE -5 carte permesso, assegnando città randomiche
        Random rand = new Random();
        for (int i = 0; i < Costanti.NUM_CARTE_PERMESSO_REGIONE - 5; i++) {
            ArrayList<Città> cittàCarta = new ArrayList<>(2);
            int numCittàCarta = 2 + rand.nextInt(2); //numCittàCarta può valere 2 oppure 3
            ArrayList<Città> cittàPossibili = new ArrayList<>();
            cittàs.forEach((Città c) -> cittàPossibili.add(c)); //città possibili contiene le città che possono essere assegnate alla carta corrente
            for (int j = 0; j < numCittàCarta; j++) {
                int posizioneCittàDaAggiungere = rand.nextInt(cittàPossibili.size());
                cittàCarta.add(cittàPossibili.get(posizioneCittàDaAggiungere));
                cittàPossibili.remove(posizioneCittàDaAggiungere); //permette di evitare di aggiungere due volte la stessa città
            }
            mazzo.addCarta(new CartaPermessoCostruzione(creaBonus(), cittàCarta));
        }
        return mazzo;
    }

    private Bonus creaBonus() {
        Bonus bonus = NullBonus.getInstance();
        Random rand = new Random();
        int numeroSottobonus = Costanti.MIN_NUM_SOTTOBONUS_PER_BONUS + rand.nextInt(Costanti.MAX_NUM_SOTTOBONUS_PER_BONUS - Costanti.MIN_NUM_SOTTOBONUS_PER_BONUS) + 1;
        for (int i = 0; i < numeroSottobonus; i++) {
            int sceltaBonus = (int) rand.nextInt(5);
            int valoreBonus = Costanti.MIN_VALORE_SOTTOBONUS + rand.nextInt(Costanti.MAX_VALORE_SOTTOBONUS - Costanti.MIN_VALORE_SOTTOBONUS) + 1;
            switch (sceltaBonus){
                case 0:
                    bonus = new BonusAiutanti(valoreBonus, bonus);
                    break;
                case 1:
                    bonus = new BonusMonete(valoreBonus, bonus);
                    break;
                case 2:
                    bonus = new BonusPuntiVittoria(valoreBonus, bonus);
                    break;
                case 3:
                    bonus = new BonusAvanzaPercorsoNobiltà(valoreBonus, bonus);
                    break;
                case 4:
                    bonus = new BonusPescaCartaPolitica(valoreBonus, bonus);
                    break;
                default:
                    break;
            }
        }
        return bonus;
    }

    private BalconeDelConsiglio creaBalcone(IdBalcone id, ArrayList<Consigliere> riservaConsiglieri, ArrayList<InterfacciaView> views){
        ArrayList<Consigliere> consiglieri = new ArrayList<>();
        for (int i = 0; i < Costanti.NUM_CONSIGLIERI_BALCONE; i++) {
            consiglieri.add(riservaConsiglieri.remove(consiglieri.size()-1));
        }
        return new BalconeDelConsiglio(id, views, consiglieri);
    }

    //prende in input tutte le citta assegna alla regione richiesta quelle giuste leggendolo da file
    private HashSet<Città> cittàInRegioniDaFile(InputStream is, NomeRegione nomeRegione, HashSet<Città> tutteLeCittà, ArrayList<InterfacciaView> views){
        Properties pro = new Properties();
        HashMap<String, Città> mapCittà = new HashMap<>();
        HashSet<Città> appartenentiRegione = new HashSet<>();
        //crea una mappa contenente (NomeCittà, città) utile per l'algoritmo
        tutteLeCittà.forEach((Città città) -> mapCittà.put(città.getNome().toString(), città));
        try {
            pro.load(is);
            String cittàStringa = pro.getProperty(nomeRegione.toString()); //ottiene una stringa del tipo "NOMECITTà1,NOMECITTà2"
            String[] nomiCittà = cittàStringa.split(","); //ottiene un array di stringhe contenente il nome delle città
            Arrays.stream(nomiCittà).forEach((String nomeCittà) -> appartenentiRegione.add(mapCittà.get(nomeCittà)));
            return appartenentiRegione;
        }catch(IOException e) {
            System.out.println("imposssibile trovare file di configurazione mappa");
            views.forEach((InterfacciaView view) -> view.erroreDiConnessione());
            Thread.currentThread().interrupt();
        }
        finally {
            return null; //solo se lanciata eccezione
        }
    }

    private Re creaRe(HashSet<Città> tutteLeCittà, ArrayList<InterfacciaView> proxyViews) {
        NomeCittà nomeCittàRe = NomeCittà.valueOf(Costanti.CITTÀ_RE);
        for (Città città : tutteLeCittà) {
            if (città.getNome() == nomeCittàRe) {
                return new Re (città, proxyViews);
            }
        }
        //se la città re non è stata trovata
        proxyViews.forEach((InterfacciaView view) -> view.erroreDiConnessione());
        Thread.currentThread().interrupt();
        return null;
    }

    private List<Bonus> creaPercorsoNobiltà () {
        List<Bonus> percorso = new ArrayList<>(Costanti.MAX_POS_NOBILTA);
        Random random = new Random();
        for(int i = 0; i < Costanti.MAX_POS_NOBILTA; i++){
            if(random.nextDouble() < Costanti.PERCENTUALE_BONUS_PERCORSO_NOBILTA){
                percorso.add(creaBonus());
            }
            else percorso.add(NullBonus.getInstance());
        }
        return percorso;
    }

    private Mazzo<CartaPolitica> creaMazzoCartePolitica () {
        Mazzo<CartaPolitica> mazzo = new Mazzo<>();
        Arrays.stream(ColoreCartaPolitica.values()).forEach((ColoreCartaPolitica colore) ->{
            for (int i = 0; i < Costanti.NUM_CARTE_POLITICA_PER_COLORE; i++) {
                mazzo.addCarta(new CartaPolitica(colore));
            }
        });
        mazzo.mischia();
        return mazzo;
    }

    private Mazzo<CartaPremioDelRe> creaMazzoCartePremioRe() {
        Mazzo<CartaPremioDelRe> mazzo = new Mazzo<>();
        for (int i = 0; i < Costanti.NUM_CARTE_PREMIO_RE; i++) {
            CartaPremioDelRe carta = new CartaPremioDelRe(Costanti.PUNTI_CARTA_PREMIO_RE[i]);
            carta.setVisibile(true);
        }
        return mazzo;
    }

    private HashSet<CartaBonusColoreCittà> creaMazzoBonusColoreCittà () {
        HashMap<ColoreCittà, Integer> puntiColoreMap = new HashMap<>();
        puntiColoreMap.put(ColoreCittà.ORO, Costanti.PUNTI_BONUS_COLORE_CITTA_ORO);
        puntiColoreMap.put(ColoreCittà.ARGENTO, Costanti.PUNTI_BONUS_COLORE_CITTA_ARGENTO);
        puntiColoreMap.put(ColoreCittà.BRONZO, Costanti.PUNTI_BONUS_COLORE_CITTA_BRONZO);
        puntiColoreMap.put(ColoreCittà.FERRO, Costanti.PUNTI_BONUS_COLORE_CITTA_FERRO);
        HashSet<CartaBonusColoreCittà> carte = new HashSet<>();
        Arrays.stream(ColoreCittà.values()).forEach((ColoreCittà colore) -> {
            CartaBonusColoreCittà carta = new CartaBonusColoreCittà(puntiColoreMap.get(colore), colore);
            carte.add(carta);
        });
        return carte;
    }

    private ArrayList<Giocatore> creaGiocatori(ArrayList<InterfacciaView> proxyViews) {
        ArrayList<Giocatore> giocatori = new ArrayList<>();
        for (int i = 0; i < proxyViews.size(); i++) {
            InterfacciaView viewCorrente = proxyViews.get(i);
            int idGiocatore = viewCorrente.getIdGiocatore();
            giocatori.add (new Giocatore(idGiocatore, Costanti.MONETE_INIZIALI_GIOCATORI[i], Costanti.AIUTANTI_INIZIALI_GIIOCATORI[i], proxyViews));
        }
        return giocatori;
    }
}
