package model;


import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class GrafoCittà {
    private ArrayList<Città> grafoCittà;

    public GrafoCittà(ArrayList<Città> città){
        this.grafoCittà = città;
    }

    public void bfs(Città cittàSorgente){
        Queue<Città> coda = new LinkedBlockingQueue<>();
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
                    cittàAdiacente.setDistanza(città.getDistanza() + 1);
                    cittàAdiacente.setPredecessore(città);
                    coda.offer(cittàAdiacente);
                }
            }
            città.setFlag(true);
        }

    }



}
