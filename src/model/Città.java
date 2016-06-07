package model;


import model.bonus.Bonus;
import model.eccezioni.CittàAdiacenteSeStessaException;
import model.eccezioni.EmporioGiàEsistenteException;
import proxyView.InterfacciaView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Città extends Observable {
    private Bonus bonus;
    private Set<Città> cittàAdiacenti;
    private ColoreCittà colore;
    private NomeCittà nome;
    private ArrayList<Emporio> empori;

    public Città (NomeCittà n, ColoreCittà c, Bonus b, ArrayList<InterfacciaView> views){
        super (views);
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

    public void addCittàAdiacenti (Città città) throws CittàAdiacenteSeStessaException, NullPointerException{
        if (città.equals(this)) throw new CittàAdiacenteSeStessaException();
        else cittàAdiacenti.add(città);
    }

    public void costruisciEmporio (Emporio e) throws EmporioGiàEsistenteException{
        if (empori.contains(e)) throw new EmporioGiàEsistenteException();
        empori.add(e);
        updateView();
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

    private void updateView(){
        ArrayList<Integer> ids = new ArrayList<>();
        empori.stream().forEach((Emporio e) -> ids.add(e.getIdGiocatore()));
        super.notifyViews((InterfacciaView v) -> v.updateEmporiCittà(nome.toString(), ids));
    }
}