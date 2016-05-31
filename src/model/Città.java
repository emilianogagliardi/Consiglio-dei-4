package model;


import model.bonus.Bonus;
import model.eccezioni.CittàAdiacenteSeStessaException;
import model.eccezioni.EmporioGiàEsistenteException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Città {
    private Bonus bonus;
    private Set<Città> cittàAdiacenti;
    private ColoreCittà colore;
    private NomeCittà nome;
    private ArrayList<Emporio> empori;

    public Città (NomeCittà n, ColoreCittà c, Bonus b){
        nome = n;
        colore = c;
        bonus = b;
        cittàAdiacenti = new HashSet<Città>();
        empori = new ArrayList<Emporio>();
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

    public void addCittàAdiacenti (Città... città) throws CittàAdiacenteSeStessaException{
        for (Città c : città) {
            if (c.equals(this)) throw new CittàAdiacenteSeStessaException();
        }
        //se non è presente la città stessa allora aggiungi le città adiacenti
        for (Città c : città) {
            cittàAdiacenti.add(c);
            if (!c.getCittàAdiacenti().contains(this))
                c.addCittàAdiacenti(this); //aggiunge se stessa come adiacente alla città appena aggiunta (set elimina duplicati)
        }
    }

    public void costruisciEmporio (Emporio e) throws EmporioGiàEsistenteException{
        if (empori.contains(e)) throw new EmporioGiàEsistenteException();
        empori.add(e);
        //TODO: updateEmporiCittà()
    }

    public boolean giàCostruito (Giocatore g){
        if (empori.contains(new Emporio(g.getId()))) return true;
        return false;
    }

    //getter
    public NomeCittà getNome () {return nome;}

    public ColoreCittà getColore() {return colore;}

    public Set<Città> getCittàAdiacenti(){return cittàAdiacenti;}

    public Bonus getBonus() {return bonus;}
}