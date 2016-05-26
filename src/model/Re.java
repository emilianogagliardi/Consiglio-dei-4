package model;

import model.eccezioni.ReNonInizializzatoException;

/**
 * Singleton con parametro
 */
public class Re {
    private static Re instance;
    private Città città;

    private Re (Città città) {
        this.città = città;
    }

    //inizializza il singleton
    public static void init (Città città) {
        if (instance == null)
            instance = new Re (città);
    }

    //ritorna l'istanza del singleton
    public static Re getInstance () throws ReNonInizializzatoException{
        if (instance == null) throw new ReNonInizializzatoException();
        return instance;
    }

    public Città getCittà(){return città;}

    public void sposta(Città c) throws IllegalArgumentException{
        if (!città.getCittàAdiacenti().contains(c)) throw new IllegalArgumentException("Impossibile spostare re in città non adiacente");
        else città = c;
    }
}
