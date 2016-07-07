package server.model;

import classicondivise.Colore;
import classicondivise.IdBalcone;
import server.model.carte.ColoreCartaPolitica;
import interfaccecondivise.InterfacciaView;
import server.sistema.Utility;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static server.model.CostantiModel.NUM_CONSIGLIERI_BALCONE;

public class BalconeDelConsiglio extends Observable {
    private classicondivise.IdBalcone idBalcone;
    private Queue<Consigliere> balcone = new LinkedBlockingQueue<>(NUM_CONSIGLIERI_BALCONE); //viene fissata una capacità massima della FIFO. Essendo una FIFO bloccante è necessario
    //prima rimuovere il consigliere in cima (a destra nel balcone) e poi inserire un nuovo consigliere in coda (a sinistra nel balcone)

    public BalconeDelConsiglio(IdBalcone IdBalcone, ArrayList<InterfacciaView> views, ArrayList<Consigliere> consiglieri) throws NullPointerException, IllegalArgumentException {
            super(views);
            this.idBalcone = IdBalcone;
            if (consiglieri.size() != NUM_CONSIGLIERI_BALCONE) {
                throw new IllegalArgumentException("Il numero di consiglieri per balcone deve essere " + NUM_CONSIGLIERI_BALCONE);
            }
            balcone.addAll(consiglieri);
            updateView();
        }

    public IdBalcone getIdBalcone(){
        return idBalcone;
    }

    public Consigliere addConsigliere(Consigliere consigliere) throws NullPointerException { //viene lanciata una NullPointerException se consigliere è null. Viene ritornato il consigliere "caduto" dal balcone
        Consigliere consigliereCaduto = balcone.element();
        balcone.remove(consigliereCaduto);
        balcone.add(consigliere);
        updateView();
        return consigliereCaduto;
    }

    public ArrayList<Colore> getColoriConsiglieri() {
        ArrayList<Colore> colori = new ArrayList<>();
        for (Consigliere consigliere : balcone) {
            colori.add(consigliere.getColore().toColore());
        }
        return colori;
    }


    //permette di capire se è possibile soddisfare il consiglio con le carte politica fornite in input. Il balcone si può soddisfare se i colori delle carte politica sono contenuti nei colori dei consiglieri del balcone (duplicati compresi)
    public boolean soddisfaConsiglio(List<ColoreCartaPolitica> coloriCartePolitica) throws IllegalArgumentException {
        if (coloriCartePolitica.size() <= 0 || coloriCartePolitica.size() > NUM_CONSIGLIERI_BALCONE) {
            throw new IllegalArgumentException("Il numero di carte politica scartate è negativo o nullo oppure maggiore di " + NUM_CONSIGLIERI_BALCONE);
        } else {
            //metto i colori del balcone e delle carte politica in HashMap con chiave il colore e valore il numero di volte che appare il colore nella collezione
            HashMap<Colore, Integer> mappaColoriBalcone;
            mappaColoriBalcone = Utility.listToHashMap(this.getColoriConsiglieri());
            HashMap<Colore, Integer> mappaColoriCartePolitica;
            ArrayList<Colore> coloriCartePoliticaSenzaJolly = new ArrayList<>();
            for(ColoreCartaPolitica colore : coloriCartePolitica)
                if(!colore.toColore().equals(Colore.JOLLY))            //le carte color JOLLY non rientrano nella verifica
                    coloriCartePoliticaSenzaJolly.add(colore.toColore());
            mappaColoriCartePolitica = Utility.listToHashMap(coloriCartePoliticaSenzaJolly);
            return Utility.hashMapContainsAllWithDuplicates(mappaColoriBalcone, mappaColoriCartePolitica);
        }
    }

    private void updateView(){
        super.notifyViews((InterfacciaView view) -> {
            try {
                view.updateBalcone(idBalcone.toString(),
                        getColoriConsiglieri().get(0).toString(),
                        getColoriConsiglieri().get(1).toString(),
                        getColoriConsiglieri().get(2).toString(),
                        getColoriConsiglieri().get(3).toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
