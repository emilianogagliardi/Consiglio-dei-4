package model;

import model.carte.Carta;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import static java.util.Arrays.asList;
import static java.util.Collections.shuffle;

public class Mazzo <T extends Carta> {
    private Collection<T> mazzo = new LinkedBlockingQueue<T>(); //l'interfaccia LinkedBlockingQueue implementa BlockingQueue che estende Queue. Queue rappresenta una FIFO.
    //BlockingQueue è un'interfaccia che rende bloccante la FIFO: è bloccato il recupero di un elemento fino a che la pila diventa non vuota.

    public void addCarta(T carta) {
        mazzo.add(carta);
    }

    public void addCarte(List<T> carte) {
        mazzo.addAll(carte);
    }

    public T getCarta() {
        return mazzo.remove(mazzo.element());
    }

    public void mischia(){
        shuffle(asList(mazzo.toArray())); //shuffle è un metodo static di Collections a cui si può passare in input solo un oggetto di tipo List
    }

    public boolean isEmpty() {
        return mazzo.isEmpty();
    }
}
