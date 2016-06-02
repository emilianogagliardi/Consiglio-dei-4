package model;

import proxyview.InterfacciaView;

import java.util.ArrayList;

public abstract class Observed {
    private ArrayList<InterfacciaView> views;

    public Observed(ArrayList<InterfacciaView> v) {
        views = v;
    }

    protected ArrayList<InterfacciaView> getViews(){return views;}

    //it is a lambda expression
    //it take a lambda expression
    protected void notifyViews(ViewUpdater u) {
        getViews().forEach((InterfacciaView v) -> u.updater(v));
    }
}
