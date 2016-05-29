package model;

public class Re {
    private Città città;

    public Re (Città città) {
        this.città = città;
    }

    public Città getCittà(){return città;}

    public void sposta(Città c) throws IllegalArgumentException{
        if (!città.getCittàAdiacenti().contains(c)) throw new IllegalArgumentException("Impossibile spostare re in città non adiacente");
        else città = c;
    }
}
