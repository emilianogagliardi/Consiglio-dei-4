package model.eccezioni;

import model.Re;

/**
 * Created by emilianogagliardi on 25/05/16.
 */
public class ReNonInizializzatoException extends Exception {
    public ReNonInizializzatoException(){
        super ("Re è un singleton con parametro, deve essere inizializzato");
    }
}
