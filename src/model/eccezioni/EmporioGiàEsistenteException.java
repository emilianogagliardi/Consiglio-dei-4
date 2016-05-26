package model.eccezioni;

import model.Emporio;

/**
 * Created by emilianogagliardi on 26/05/16.
 */
public class EmporioGiàEsistenteException extends Exception{
    public EmporioGiàEsistenteException(){
        super("esiste già un emporio del colore aggiunto");
    }
}
