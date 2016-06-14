package server.model;


import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class GrafoCittà {
    private ArrayList<Città> grafoCittà;

    public GrafoCittà(ArrayList<Città> città){
        this.grafoCittà = città;
    }

    public ArrayList<Città> bfs(Città cittàSorgente, BiConsumer<Città, ArrayList<Città>> consumer) { //bfs prende in input una città sorgente e un BiConsumer e restituisce una lista di città collegate alla sorgente in cui il giocatore
        //ha costruito un emporio. Il BiConsumer detrmina se l'algoritmo avrà un'esecuzione standard (il metodo accept di BiConsumer non fa nulla) oppure se l'algoritmo
        //stopperà l'esplorazione attraverso quelle città in cui non è presente un emporio del giocatoreCorrente (settando a true il flag delle città).
        //Come side-effect questo algoritmo setta gli attributi distanza di tutti i nodi raggiungibili dalla sorgente
        Queue<Città> coda = new LinkedBlockingQueue<>();
        ArrayList<Città> cittàCollegate = new ArrayList<>();
        for (Città elCittà : grafoCittà){
            elCittà.setFlag(false);
            elCittà.setDistanza(Integer.MAX_VALUE);
        }
        cittàSorgente.setDistanza(0);
        coda.offer(cittàSorgente);
        Città città;
        while (coda.size() != 0){
            città = coda.remove();
            for (Città cittàAdiacente : città.getCittàAdiacenti()) {
                if (cittàAdiacente.getFlag() == false) {
                    consumer.accept(cittàAdiacente, cittàCollegate);
                    cittàAdiacente.setDistanza(città.getDistanza() + 1);
                    coda.offer(cittàAdiacente);
                }
            }
            città.setFlag(true);
        }
        return cittàCollegate;
    }

    public boolean dfs(BiFunction<Città, Boolean, Boolean> biFunction) {
        //l'algortimo di dfs in questione viene utilizzato per esplorare tutto il grafo di città e verificare se il giocatore ha costruito in tutte le città di un detrminato colore
        //oppure in tutte le città di una regione. cittàCostruzione è la città dove il giocatore ha appena costruito. Bisogna verificare se il giocatore ha, oppure no, empori
        //in tutte le città dello stesso colore di cittàCostruzione oppure ha empori in tutte le città dove cittàCostruzione risiede.
        for (Città elCittà : grafoCittà) {
            elCittà.setFlag(false);
        }
        boolean valoreDaRitornare = true;
        for (Città elCittà : grafoCittà) {
            if (!elCittà.getFlag()) {
                valoreDaRitornare = dfsVisit(elCittà, biFunction, valoreDaRitornare);
            }
        }
        return valoreDaRitornare;
    }

    public boolean dfsVisit(Città elCittà, BiFunction<Città, Boolean, Boolean> biFunction, boolean valoreDaRitornare) {
        elCittà.setDistanza(0);
        for (Città cittàAdiacente : elCittà.getCittàAdiacenti()) {
            if (!cittàAdiacente.getFlag()) {
                valoreDaRitornare = biFunction.apply(cittàAdiacente, valoreDaRitornare);
                valoreDaRitornare = dfsVisit(cittàAdiacente, biFunction, valoreDaRitornare);
            }
        }
        elCittà.setFlag(true);
        return valoreDaRitornare;
    }
}
