package client.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emilianogagliardi on 26/06/16.
 */
/*
classe che serve a memorizzare il numero di elementi disponibili eni pool di
partita in seguito alle update dal server
 */
public class RiservaPartitaView {
    private RiservaPartitaView instance;
    private List<String> consiglieri;

    private RiservaPartitaView() {
        consiglieri = new ArrayList<>();
    }

    public RiservaPartitaView getInstance(){
        if(instance == null) instance = new RiservaPartitaView();
        return instance;
    }

    public void setConsiglieri(List<String> consiglieri){this.consiglieri = consiglieri;}
    public List<String> getConsiglieri(){return consiglieri;}
}
