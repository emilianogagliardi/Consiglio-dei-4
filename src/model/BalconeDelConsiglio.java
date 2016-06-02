package model;

import model.carte.CartaPolitica;
import proxyview.InterfacciaView;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import static java.util.Arrays.asList;
import static model.Costanti.NUM_CONSIGLIERI_BALCONE;

public class BalconeDelConsiglio extends Observed{
    private NomeRegione regione;
    private Queue<Consigliere> balcone = new LinkedBlockingQueue<>(NUM_CONSIGLIERI_BALCONE); //viene fissata una capacità massima della FIFO. Essendo una FIFO bloccante è opportuno
    //prima rimuovere il consigliere in cima (a destra nel balcone) e poi inserire un nuovo consigliere in coda (a sinistra nel balcone)

    public BalconeDelConsiglio(NomeRegione regione, ArrayList<InterfacciaView> views, Consigliere... consiglieri) throws NullPointerException, IllegalArgumentException {
        super(views);
        this.regione = regione;
        if (consiglieri.length != NUM_CONSIGLIERI_BALCONE) {
            throw new IllegalArgumentException("Il numero di consiglieri per balcone deve essere " + NUM_CONSIGLIERI_BALCONE);
        }
        balcone.addAll(asList(consiglieri));
        updateView();
    }

    public Queue<Consigliere> getConsiglieri(){
        return balcone;
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
            //metto i colori delle carte politica e dei consiglieri in arraylist e poi uso il metodo containsAll di Collection per verificare se i colori delle carte politica
            //sono contenuti nei colori dei consigieri del baclone scelto
            HashMap<Colore, Integer> mappaColoriBalcone = new HashMap<>();
            Integer contatore = 1;
            for(ColoreConsigliere coloreConsigliere : this.getColoriConsiglieri()) {//getColoriConsiglieri restituisce un ArrayList di colori consigliere
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
                    if (valueBalcone - mappaColoriCartePolitica.get(keyBalcone) < 0)
                        return false;
                    else mappaColoriCartePolitica.put(keyBalcone, 0);
                } catch (NullPointerException exc){
                    //do nothing
                }
            }
            for(Map.Entry<Colore, Integer> entry : mappaColoriCartePolitica.entrySet()) {
                if (entry.getValue() != 0)
                    return false;
            }
            return true;
        }
    }

    private void updateView(){
        super.notifyViews((InterfacciaView v) -> v.updateBalcone(   regione.toString(),
                                                                    getColoriConsiglieri().get(0).toString(),
                                                                    getColoriConsiglieri().get(0).toString(),
                                                                    getColoriConsiglieri().get(0).toString(),
                                                                    getColoriConsiglieri().get(0).toString()));
    }
}
