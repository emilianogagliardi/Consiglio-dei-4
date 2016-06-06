package model;

import model.carte.CartaPolitica;
import proxyview.InterfacciaView;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import static java.util.Arrays.asList;
import static model.Costanti.NUM_CONSIGLIERI_BALCONE;

public class BalconeDelConsiglio extends Observable {
    private IdBalcone IdBalcone;
    private Queue<Consigliere> balcone = new LinkedBlockingQueue<>(NUM_CONSIGLIERI_BALCONE); //viene fissata una capacità massima della FIFO. Essendo una FIFO bloccante è opportuno
    //prima rimuovere il consigliere in cima (a destra nel balcone) e poi inserire un nuovo consigliere in coda (a sinistra nel balcone)

    public BalconeDelConsiglio(IdBalcone IdBalcone, ArrayList<InterfacciaView> views, Consigliere... consiglieri) throws NullPointerException, IllegalArgumentException {
            super(views);
            this.IdBalcone = IdBalcone;
            if (consiglieri.length != NUM_CONSIGLIERI_BALCONE) {
                throw new IllegalArgumentException("Il numero di consiglieri per balcone deve essere " + NUM_CONSIGLIERI_BALCONE);
            }
            balcone.addAll(asList(consiglieri));
            updateView();
        }

    public IdBalcone getIdBalcone(){
        return IdBalcone;
    }

    public Consigliere addConsigliere(Consigliere consigliere) throws NullPointerException { //viene lanciata una IllegalStateException se non c'è spazio
        //nella coda per aggiungere un nuovo elemento; NullPointerException se consigliere è null. Viene ritornato il consigliere "caduto" dal balcone
        Consigliere consigliereCaduto = balcone.element();
        balcone.remove(consigliereCaduto);
        balcone.add(consigliere);
        updateView();
        return consigliereCaduto;
    }

    public ArrayList<ColoreConsigliere> getColoriConsiglieri() {
        ArrayList<ColoreConsigliere> colori = new ArrayList<>();
        for (Consigliere consigliere : balcone) {
            colori.add(consigliere.getColore());
        }
        return colori;
    }

    public boolean soddisfaConsiglio(CartaPolitica... cartePolitica) throws IllegalArgumentException {
        if (cartePolitica.length <= 0 || cartePolitica.length > NUM_CONSIGLIERI_BALCONE) {
            throw new IllegalArgumentException("Il numero di carte politica scartate è negativo o nullo oppure maggiore di " + NUM_CONSIGLIERI_BALCONE);
        } else {
            //metto i colori del balcone e delle carte politica in HashMap con chiave il colore e valore il numero di volte che appare il colore nella collezione
            HashMap<Colore, Integer> mappaColoriBalcone = new HashMap<>();
            Integer contatore = 1;
            for(ColoreConsigliere coloreConsigliere : this.getColoriConsiglieri()) { //getColoriConsiglieri restituisce un ArrayList di colori consigliere
                if(mappaColoriBalcone.containsKey(coloreConsigliere.toColore())){
                    contatore++;
                }
                else contatore = 1;
                mappaColoriBalcone.put(coloreConsigliere.toColore(), contatore);
            }
            HashMap<Colore, Integer> mappaColoriCartePolitica = new HashMap<>();
            contatore = 1;
            for (CartaPolitica cartaPolitica : cartePolitica){
                if (!cartaPolitica.getColore().toColore().equals(Colore.JOLLY)) {
                    if (mappaColoriCartePolitica.containsKey(cartaPolitica.getColore().toColore())){
                        contatore++;
                    }
                    else contatore = 1;
                    mappaColoriCartePolitica.put(cartaPolitica.getColore().toColore(), contatore);
                }
            }
            for(Map.Entry<Colore, Integer> entry : mappaColoriBalcone.entrySet()) {
                Colore keyBalcone = entry.getKey();
                Integer valueBalcone = entry.getValue();
                try {
                    if (valueBalcone - mappaColoriCartePolitica.get(keyBalcone) < 0) {
                        //per ogni colore di consigliere faccio la differenza tra il numero di consiglieri di quel colore e il numero di carte politica dello stesso colore
                        //se la differenza è negativa significa che non si può soddisfare il consiglio perchè non ci sono abbastanza consiglieri di quel colore
                        return false;
                    }
                    else mappaColoriCartePolitica.put(keyBalcone, 0); //se è possibile soddisfare il consiglio con le carte di questo particolare colore
                    // azzero il valore corrispondente alla chiave rappresentata dal colore suddetto
                } catch (NullPointerException exc){ //viene lanciata una NullPointerException se nel balcone è presente un colore che non c'è nelle carte politica. Poco male!
                    //do nothing
                }
            }
            for(Map.Entry<Colore, Integer> entry : mappaColoriCartePolitica.entrySet()) { //controllo se tutti i valori sono stati azzerati, altrimenti significa che
                //esistono carte politica di un colore non presente nel balcone
                if (entry.getValue() != 0)
                    return false;
            }
            return true;
        }
    }

    private void updateView(){
        super.notifyViews((InterfacciaView view) -> view.updateBalcone(   IdBalcone.toString(),
                                                                    getColoriConsiglieri().get(0).toString(),
                                                                    getColoriConsiglieri().get(1).toString(),
                                                                    getColoriConsiglieri().get(2).toString(),
                                                                    getColoriConsiglieri().get(3).toString()));
    }
}
