package controller;

import model.Partita;
import proxyview.InterfacciaView;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller implements Runnable{
    private Partita model;
    private ArrayList<InterfacciaView> views;

    public Controller(Partita p, ArrayList<InterfacciaView> v) {
        model = p;
        views = v;
    }

    @Override
    public void run() {

    }
}
