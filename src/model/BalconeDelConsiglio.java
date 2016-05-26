package model;


import model.carte.CartaPolitica;
import model.eccezioni.NumeroCartePoliticaNonValidoException;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import static java.util.Arrays.asList;

public class BalconeDelConsiglio {
    private Queue<Consigliere> balcone = new LinkedBlockingQueue<>(4); //viene fissata una capacità massima della FIFO. Essendo una FIFO bloccante è opportuno
    //prima rimuovere il consigliere in cima (a destra nel balcone) e poi inserire un nuovo consigliere in coda (a sinistra nel balcone)

    public BalconeDelConsiglio(Consigliere... consiglieri) throws IllegalStateException, NullPointerException {
        balcone.addAll(asList(consiglieri));
    }

    public void addConsigliere(Consigliere consigliere) throws IllegalStateException, NullPointerException { //viene lanciata una IllegalStateException se non c'è spazio
        //nella coda per aggiungere un nuovo elemento
        balcone.remove(balcone.element());
        balcone.add(consigliere);
    }

    public ArrayList<String> getColoriConsiglieri(){
        ArrayList<String> colori = new ArrayList<>();
        for (Consigliere consigliere : balcone) {
            colori.add(consigliere.)
        }
    }

    public void soddisfaConsiglio(CartaPolitica... cartePolitica) throws NumeroCartePoliticaNonValidoException {
        if (cartePolitica.length <= 0 || cartePolitica.length > 4) {
            throw new NumeroCartePoliticaNonValidoException();
        } else {
            //metto i colori delle carte politica e dei consiglieri in arrays e poi si usa il metodo containsAll di Collection. Quindi c'è da overridare il metodo equals
            //delle enum ColoreCartaPolitica e ColoreConsigliere. Il colore JOLLY è uguale a qualsiasi colore
            ArrayList<String> coloriCartePolitica = new ArrayList<>();
            ArrayList<String> colori

        }
    }
}
