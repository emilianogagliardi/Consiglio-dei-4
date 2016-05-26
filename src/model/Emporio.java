package model;



/**
 * Created by emilianogagliardi on 20/05/16.
 */
public class Emporio {
    private int idGiocatore;

    public Emporio(int id) {
        idGiocatore = id;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Emporio) {
            Emporio e = (Emporio) o;
            if (idGiocatore == e.idGiocatore) return true;
        }
        return false;
    }
}
