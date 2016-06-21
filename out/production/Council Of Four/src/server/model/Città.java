package server.model;


import classicondivise.bonus.Bonus;
import classicondivise.NomeCittà;
import server.model.eccezioni.CittàAdiacenteSeStessaException;
import interfaccecondivise.InterfacciaView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Città extends Observable {
    private Bonus bonus;
    private Set<Città> cittàAdiacenti;
    private ColoreCittà colore;
    private NomeCittà nome;
    private ArrayList<Emporio> empori;
    private NomeRegione nomeRegione;
    //attributi per gli algoritmi sui grafi
    private boolean flag; //il flag è true quando il nodo è stato scoperto e poi è stato tolto dalla coda dei nodi scoperti perchè scoperti tutti i suoi nodi adiacenti
    private Integer distanza; //distanza del nodo dalla sorgente (dipende su dal nodo sul quale è stato chiamato l'algoritmo)

    public Città (NomeRegione nomeRegione, NomeCittà n, ColoreCittà c, Bonus b, ArrayList<InterfacciaView> views){
        super (views);
        this.nomeRegione = nomeRegione;
        nome = n;
        colore = c;
        bonus = b;
        cittàAdiacenti = new HashSet<Città>();
        empori = new ArrayList<Emporio>();
        flag = false;
        distanza = Integer.MAX_VALUE;
        updateViewBonusCittà();
    }

    public NomeRegione getNomeRegione() {
        return nomeRegione;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setDistanza(Integer distanza) {
        this.distanza = distanza;
    }

    public Integer getDistanza() {
        return distanza;
    }

    @Override
    public boolean equals (Object o) {
        if (o instanceof Città){
            Città c = (Città) o;
            if (c.getNome().equals(this.getNome()) && this.getColore().equals(c.getColore()) && this.nomeRegione.equals(c.getNomeRegione()))
                return true;
        }
        return false;
    }

    public void addCittàAdiacenti (Città città) throws CittàAdiacenteSeStessaException, NullPointerException{
        if (città.equals(this)) throw new CittàAdiacenteSeStessaException();
        else cittàAdiacenti.add(città);
    }

    public void costruisciEmporio (Emporio e){
        empori.add(e);
        updateViewEmpori();
    }

    public int getNumeroEmporiCostruiti(){
        return empori.size();
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

    private void updateViewEmpori(){
        ArrayList<Integer> idGiocatori = new ArrayList<>();
        empori.stream().forEach((Emporio e) -> idGiocatori.add(e.getIdGiocatore()));
        super.notifyViews((InterfacciaView v) -> {
            try {
                v.updateEmporiCittà(nome.toString(), idGiocatori);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateViewBonusCittà(){
        super.notifyViews((InterfacciaView view) -> {
            try {
                view.updateBonusCittà(nome.toString(), this.bonus);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public String toString() {
        return nome+ " " + colore + " " + nomeRegione;
    }
}