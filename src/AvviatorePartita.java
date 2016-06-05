
import controller.Controller;
import model.*;
import model.bonus.NullBonus;
import model.eccezioni.CittàAdiacenteSeStessaException;
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
        ArrayList <InterfacciaView> views = creaViews();
        Partita nuovaPartita = creaPartita(views);
        executors.submit(new Controller(nuovaPartita, views));
    }

    private ArrayList<InterfacciaView> creaViews (){
        ArrayList <InterfacciaView> views = new ArrayList<>(2);
        modiComunicazione.forEach((idGiocatore, modoComunicazione) -> {
            if (modoComunicazione.equals("RMI")) views.add(new RMIProxyView(idGiocatore));
            else views.add(new SocketProxyView(idGiocatore, sockets.get(idGiocatore)));
        });
        return views;
    }

    private Partita creaPartita(ArrayList<InterfacciaView> views){
        Partita partita = new Partita(views);

        return partita;
    }

    private void costruisciMappaDaFileConf (InputStream is, HashSet<Città> cittàSet, HashSet<Regione> regioniSet) throws IOException {
        Properties pro = new Properties();
        pro.load(is);  //lancia io exception se non trova il file
        HashMap<String, Città> mapCittà = new HashMap<>();  //creazione e popolamento di una hashMap contenente (nomeCittà, Città)
        cittàSet.forEach((Città c) -> mapCittà.put(c.getNome().toString(), c));
        HashMap<String, Regione> mapRegioni = new HashMap<>(); //creazione e popolamento di una hashMao contenente (nomeRegione, Regione)
        regioniSet.forEach((Regione r) -> mapRegioni.put(r.getNome().toString(), r));
        //aggiunta delle città alle regioni
        Iterator<Regione> itR = regioniSet.iterator();
        while (itR.hasNext()) {
            Regione regione = itR.next();
            String stringaCittàAppartenenti = pro.getProperty(regione.getNome().toString()); //ottiene una string di tipo "NOMECITTà1,NOMECITTà2,NOMECITTà3"
            String[] nomiCittàAppartenenti = stringaCittàAppartenenti.split(","); //ottiene un array contenente i nomi delle città appartenenti alla regione
            for (String nomeCittàAppartenente : nomiCittàAppartenenti) {
                regione.addCittà(mapCittà.get(nomeCittàAppartenente));
            }
        }
        //aggiunta alle città delle città adiacenti
        Iterator<Città> itC = cittàSet.iterator();
        while (itC.hasNext()){
            Città città = itC.next();
            String stringaCittàAdiacenti = pro.getProperty(città.getNome().toString()); //ottiene una stringa di tipo "NOMECITTà1,NOMECITTà2,NOMECITTà3"
            String[] nomiCittàAdiacenti = stringaCittàAdiacenti.split(","); //ottiene un array contenente i nomi delle città adiacenti alla città corrente
            for (String nomeCittàAdiacente : nomiCittàAdiacenti) {
                try {
                    città.addCittàAdiacenti(mapCittà.get(nomeCittàAdiacente));
                }catch (CittàAdiacenteSeStessaException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
