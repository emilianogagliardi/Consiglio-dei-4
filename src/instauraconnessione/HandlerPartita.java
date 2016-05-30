package instauraconnessione;

import model.Partita;

//TODO rinominare come controller e posizionare bene nei package?

public class HandlerPartita implements Runnable{
    private Partita model;
    //private collezione di InterfacciaView views;

    public HandlerPartita (Partita p) { //InterfacciaView views
        model = p;
        //assegna views
    }

    @Override
    public void run() {

    }
}
