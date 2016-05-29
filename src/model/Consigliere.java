package model;

public class Consigliere {
    private ColoreConsigliere colore;

    public Consigliere (ColoreConsigliere colore) {
        this.colore = colore;
    }

    public ColoreConsigliere getColore(){
        return colore;
    }

    @Override
    public boolean equals (Object o) {
        if (o instanceof Consigliere){
            Consigliere c = (Consigliere) o;
            if (c.getColore() == colore)
                return true;
        }
        return false;
    }
}
