package server.model;

import classicondivise.carte.Carta;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import static java.util.Arrays.asList;
import static java.util.Collections.shuffle;

public class Mazzo <T extends Carta> {
    private Queue<T> mazzo = new LinkedBlockingQueue<T>(); //l'interfaccia LinkedBlockingQueue implementa BlockingQueue che estende Queue. Queue rappresenta una FIFO.
    //BlockingQueue è un'interfaccia che rende bloccante la FIFO: è bloccato il recupero di un elemento fino a che la pila diventa non vuota.

    public void addCarta(T carta){
        mazzo.add(carta);
    }

    public void addCarte(Queue<T> carte) {
        mazzo.addAll(carte);
    }

    public T ottieniCarta() throws NoSuchElementException { //NoSuchElementException viene lanciata quando non ci sono carte nel mazzo. ottieniCarta() restituisce e rimuove la carta
        //in cima la mazzo
        T cartaDaRestituire = getCarta();
        mazzo.remove(cartaDaRestituire);
        return cartaDaRestituire;
    }

    public void mischia(){
        shuffle(asList(mazzo.toArray())); //shuffle è un metodo static di Collections a cui si può passare in input solo un oggetto di tipo List
    }

    public boolean isEmpty() {
        return mazzo.isEmpty();
    }

    public T getCarta() throws NoSuchElementException{ //il metodo getCarta() restituisce ma non rimuove la carta in cima al mazzo
        return mazzo.element();
    }
}
