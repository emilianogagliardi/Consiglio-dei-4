package model;


import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GrafoCittà {
    private ArrayList<Città> grafoCittà;

    public GrafoCittà(ArrayList<Città> città){
        this.grafoCittà = città;
    }

    public ArrayList<Città> bfs(Città cittàSorgente, BiConsumer<Città, ArrayList<Città>> consumer) { //bfs prende in input una città sorgente e un BiConsumer e restituisce una lista di città collegate alla sorgente in cui il giocatore
        //ha costruito un emporio. Il BiConsumer detrmina se l'algoritmo avrà un'esecuzione standard (il metodo accept di BiConsumer non fa nulla) oppure se l'algoritmo
        //stopperà l'esplorazione attraverso quelle città in cui non è presente un emporio del giocatoreCorrente (settando a true il flag delle città).
        //Come side-effect questo algoritmo setta gli attributi distanza di tutti i nodi raggiungibili dalla sorgente e crea l'albero dei cammini minimi
        Queue<Città> coda = new LinkedBlockingQueue<>();
        ArrayList<Città> cittàCollegate = new ArrayList<>();
        for (Città elCittà : grafoCittà){
            elCittà.setFlag(false);
            elCittà.setDistanza(Integer.MAX_VALUE);
            elCittà.setPredecessore(null);
        }
        cittàSorgente.setDistanza(0);
        cittàSorgente.setPredecessore(null);
        coda.offer(cittàSorgente);
        Città città;
        while (coda.size() != 0){
            città = coda.remove();
            for (Città cittàAdiacente : città.getCittàAdiacenti()) {
                if (cittàAdiacente.getFlag() == false) {
                    consumer.accept(cittàAdiacente, cittàCollegate);
                    cittàAdiacente.setDistanza(città.getDistanza() + 1);
                    cittàAdiacente.setPredecessore(città);
                    coda.offer(cittàAdiacente);
                }
            }
            città.setFlag(true);
        }
        return cittàCollegate;
    }
}
