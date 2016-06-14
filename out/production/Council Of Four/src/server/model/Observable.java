package server.model;

import interfaccecondivise.InterfacciaView;

import java.util.ArrayList;

public abstract class Observable {
    private ArrayList<InterfacciaView> views;

    public Observable(ArrayList<InterfacciaView> views) {
        this.views = views;
    }

    protected ArrayList<InterfacciaView> getViews(){return views;}

    //it is a lambda expression
    //it take a lambda expression
    protected void notifyViews(ViewUpdater updater) {
        getViews().forEach((InterfacciaView view) -> updater.update(view));
    }
}
