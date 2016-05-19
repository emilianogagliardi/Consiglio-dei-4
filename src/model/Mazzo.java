package model;

import model.carte.Carta;
import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.shuffle;

public class Mazzo {
    List<Carta> mazzo = new ArrayList<Carta>(1);

    public void addCarta(Carta carta) {
        mazzo.add(carta);
    }

    public void addCarte(List<Carta> carte) {
        mazzo.addAll(carte);
    }

    public Carta getCarta() {
        if (mazzo.isEmpty()) {
            //TODO: ritornare cartaNull?
        }
        else {
            int indiceCartaDaRimuovere = mazzo.size() - 1; //toglie le carte dalla cima del mazzo
            return mazzo.remove(indiceCartaDaRimuovere);
        }
    }

    public void mischia(){
        shuffle(mazzo);
    }

}
