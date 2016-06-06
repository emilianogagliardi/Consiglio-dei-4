
import controller.Controller;
import model.*;
import model.bonus.*;
import model.carte.CartaBonusRegione;
import model.carte.CartaPermessoCostruzione;
import proxyview.InterfacciaView;
import proxyview.RMIProxyView;
import proxyview.SocketProxyView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AvviatorePartita implements Runnable {
    private HashMap<Integer, Socket> sockets;
    private HashMap<Integer, String> modiComunicazione;
    private static ExecutorService executors = Executors.newCachedThreadPool();

    public AvviatorePartita(HashMap<Integer, Socket> sockets, HashMap<Integer, String> modiComunicazione) {
        this.modiComunicazione = modiComunicazione;
        this.sockets = sockets;
    }

    @Override
    public void run() {
        ArrayList<InterfacciaView> proxyViews = creaProxyViews();
        Partita nuovaPartita = creaPartita(proxyViews);
        executors.submit(new Controller(nuovaPartita, proxyViews));
    }

    private ArrayList<InterfacciaView> creaProxyViews (){
        ArrayList <InterfacciaView> proxyViews = new ArrayList<>(2);
        modiComunicazione.forEach((idGiocatore, modoComunicazione) -> {
            if (modoComunicazione.equals("RMI")) proxyViews.add(new RMIProxyView(idGiocatore));
            else proxyViews.add(new SocketProxyView(idGiocatore, sockets.get(idGiocatore)));
        });
        return proxyViews;
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
        //TODO precorso nobiltà
        //TODO mazzo carte politica
        //TODO mazzo carte premio re
        //TODO mazzo carte bonus colore città
        //TODO giocatori
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
            //TODO kill this thread
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
            //TODO kill this thread
        }
        finally {
            return null; //solo se è stat lanciata l'accezione
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
            //TODO kill this thread
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
            //TODO kill this thread
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
        //TODO kill this thread
        return null;
    }
}
