package server.model;


public class Emporio {
    private int idGiocatore;

    public Emporio(int id) {
        idGiocatore = id;
    }
    public int getIdGiocatore() { return idGiocatore;}
    @Override
    public boolean equals(Object o){
        if (o instanceof Emporio) {
            Emporio e = (Emporio) o;
            if (idGiocatore == e.idGiocatore) return true;
        }
        return false;
    }
}
