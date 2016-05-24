package model;


import model.bonus.Bonus;
import model.carte.ColoreCittà;
import model.eccezioni.CittàAdiacenteSeStessaException;

import java.util.ArrayList;

public class Città {
    private Bonus bonus;
    private ArrayList<Città> cittàAdiacenti;
    private ColoreCittà colore;
    private NomeCittà nome;

    public Città (NomeCittà n, ColoreCittà c, Bonus b){
        nome = n;
        colore = c;
        bonus = b;
    }

    public Città (NomeCittà n, ColoreCittà c, Bonus b, Città primaCittà, Città... citAd) throws CittàAdiacenteSeStessaException{
        this (n, c, b);
        this.addCittàAdiacente(citAd);
    }

    @Override
    public boolean equals (Object o) {
        if (o instanceof Città){
            Città c = (Città) o;
            if (c.getNome().equals(this.getNome()))
                return true;
        }
        return false;
    }

     //getter
    public NomeCittà getNome () {return nome;}
    public ColoreCittà getColore() {return colore;}
    public ArrayList<Città> getCittàAdiacenti(){return cittàAdiacenti;}
    public Bonus getBonus() {return bonus;}

    public void addCittàAdiacente (Città... città) throws CittàAdiacenteSeStessaException{
        for (int i = 0; i < città.length; i++) {
            if (città[i].equals(this)) throw new CittàAdiacenteSeStessaException();
        }
        //se non è presente la città stessa allora aggiungi le città adiacenti
        for (int i = 0; i < città.length; i++) {
            cittàAdiacenti.add(città[i]);
        }
    }
}
