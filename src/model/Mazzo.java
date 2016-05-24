package model;

import model.carte.Carta;
import model.carte.NullCarta;
import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.shuffle;

public class Mazzo <T extends Carta> {
    List<T> mazzo = new ArrayList<T>();

    public void addCarta(T carta) {
        mazzo.add(carta);
    }

    public void addCarte(List<T> carte) {
        mazzo.addAll(carte);
    }

    public T pescaCarta() {
        if (mazzo.isEmpty()) {
            //return NullCarta.getInstance(); //TODO: Qual'è il problema?!?!?!?
        }
        else {
            int indiceCartaDaRimuovere = mazzo.size() - 1; //toglie le carte dalla cima del mazzo
            return mazzo.remove(indiceCartaDaRimuovere); //TODO: verificare se remove lancia un'eccezione se la lista è vuota
        }
        return null; //aggiunto solo per la compilazione
    }

    public void mischia(){
        shuffle(mazzo); //shuffle è un metodo static di Collections a cui si può passare in input solo un oggetto di tipo List
    }

    public boolean isEmpty() {
        return mazzo.isEmpty();
    }
}
