package model;


/**
 * Created by emilianogagliardi on 26/05/16.
 */
public class Consiglere {
    private ColoreConsigliere colore;

    public Consiglere (ColoreConsigliere colore) {
        this.colore = colore;
    }

    public ColoreConsigliere getColore(){
        return colore;
    }

    @Override
    public boolean equals (Object o) {
        if (o instanceof Consiglere){
            Consiglere c = (Consiglere) o;
            if (c.getColore() == colore)
                return true;
        }
        return false;
    }
}
