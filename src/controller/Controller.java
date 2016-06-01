package controller;

import model.Partita;
import proxyview.InterfacciaView;

import java.util.HashMap;

public class Controller implements Runnable{
    private Partita model;
    private HashMap<Integer, InterfacciaView> views;

    public Controller(Partita p, HashMap<Integer, InterfacciaView> v) {
        model = p;
        views = v;
    }

    @Override
    public void run() {

    }
}
