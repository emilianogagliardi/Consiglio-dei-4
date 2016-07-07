package server.model;

import interfaccecondivise.InterfacciaView;

import java.util.ArrayList;

public abstract class Observable {
    private ArrayList<InterfacciaView> views;

    public Observable(ArrayList<InterfacciaView> views) {
        this.views = views;
    }

    protected ArrayList<InterfacciaView> getViews(){return views;}

    /*
    chiama il metodo update su ognuno degli osservatori. Chiamand questo
    metodo dai sottotipi non è necessario preoccuparsi di chi siano e di quanti
    siano gli observer, è solo necessario definire l'implementazione del metodo update
     */
    protected void notifyViews(ViewUpdater updater) {
        getViews().forEach((InterfacciaView view) -> updater.update(view));
    }
}
