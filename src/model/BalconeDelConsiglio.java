package model;


import model.carte.CartaPolitica;
import model.eccezioni.NumeroCartePoliticaNonValidoException;
import model.eccezioni.NumeroConsiglieriNonValidoException;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import static java.util.Arrays.asList;

public class BalconeDelConsiglio {
    private Queue<Consigliere> balcone = new LinkedBlockingQueue<>(4); //viene fissata una capacità massima della FIFO. Essendo una FIFO bloccante è opportuno
    //prima rimuovere il consigliere in cima (a destra nel balcone) e poi inserire un nuovo consigliere in coda (a sinistra nel balcone)

    public BalconeDelConsiglio(Consigliere... consiglieri) throws IllegalStateException, NullPointerException, NumeroConsiglieriNonValidoException {
        if (consiglieri.length != 4) {
            throw new NumeroConsiglieriNonValidoException();
        }
        balcone.addAll(asList(consiglieri));
    }

    public Queue<Consigliere> getConsiglieri(){
        return balcone;
    }

    public void addConsigliere(Consigliere consigliere) throws IllegalStateException, NullPointerException { //viene lanciata una IllegalStateException se non c'è spazio
        //nella coda per aggiungere un nuovo elemento
        balcone.remove(balcone.element());
        balcone.add(consigliere);
    }

    public ArrayList<String> getColoriConsiglieri() {
        ArrayList<String> colori = new ArrayList<>();
        for (Consigliere consigliere : balcone) {
            colori.add(consigliere.getColore().toString());
        }
        return colori;
    }

    public boolean soddisfaConsiglio(CartaPolitica... cartePolitica) throws NumeroCartePoliticaNonValidoException {
        if (cartePolitica.length <= 0 || cartePolitica.length > 4) {
            throw new NumeroCartePoliticaNonValidoException();
        } else {
            //metto i colori delle carte politica e dei consiglieri in arraylist e poi uso il metodo containsAll di Collection per verificare se i colori delle carte politica
            //sono contenuti nei colori dei consigieri del baclone scelto
            ArrayList<String> coloriCartePolitica = new ArrayList<>();
            for (CartaPolitica cartaPolitica : cartePolitica) {
                coloriCartePolitica.add(cartaPolitica.getColore().toString());
            }
            return this.getColoriConsiglieri().containsAll(coloriCartePolitica);
        }
    }
}
