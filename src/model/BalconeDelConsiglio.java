package model;


import model.carte.CartaPolitica;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import static java.util.Arrays.asList;
import static model.Costanti.NUM_CONSIGLIERI_BALCONE;

public class BalconeDelConsiglio {
    private Queue<Consigliere> balcone = new LinkedBlockingQueue<>(NUM_CONSIGLIERI_BALCONE); //viene fissata una capacità massima della FIFO. Essendo una FIFO bloccante è opportuno
    //prima rimuovere il consigliere in cima (a destra nel balcone) e poi inserire un nuovo consigliere in coda (a sinistra nel balcone)

    public BalconeDelConsiglio(Consigliere... consiglieri) throws IllegalStateException, NullPointerException, IllegalArgumentException {
        if (consiglieri.length != NUM_CONSIGLIERI_BALCONE) {
            throw new IllegalArgumentException("Il numero di consiglieri per balcone deve essere " + NUM_CONSIGLIERI_BALCONE);
        }
        balcone.addAll(asList(consiglieri));
    }

    public Queue<Consigliere> getConsiglieri(){
        return balcone;
    }

    public Consigliere addConsigliere(Consigliere consigliere) throws IllegalStateException, NullPointerException { //viene lanciata una IllegalStateException se non c'è spazio
        //nella coda per aggiungere un nuovo elemento; NullPointerException se consigliere è null. Viene ritornato il consigliere "caduto" dal balcone
        Consigliere consigliereCaduto = balcone.element();
        balcone.remove(consigliereCaduto);
        balcone.add(consigliere);
        return consigliereCaduto;
    }

    public ArrayList<String> getColoriConsiglieri() {
        ArrayList<String> colori = new ArrayList<>();
        for (Consigliere consigliere : balcone) {
            colori.add(consigliere.getColore().toString());
        }
        return colori;
    }

    public boolean soddisfaConsiglio(CartaPolitica... cartePolitica) throws IllegalArgumentException {
        if (cartePolitica.length <= 0 || cartePolitica.length > NUM_CONSIGLIERI_BALCONE) {
            throw new IllegalArgumentException("Il numero di carte politica scartate è negativo o nullo oppure maggiore di " + NUM_CONSIGLIERI_BALCONE);
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
