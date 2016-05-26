package model;

import java.util.ArrayList;
import java.util.List;

public class Partita {
    private List<Consigliere> riservaConsiglieri = new ArrayList<>(8);

    public void addConsigliere(Consigliere consigliere){
        riservaConsiglieri.add(consigliere);
    }

    public void getConsigliere(){

    }
}
