package model.eccezioni;


public class EmporioGiàEsistenteException extends Exception{
    public EmporioGiàEsistenteException(){
        super("esiste già un emporio del colore aggiunto");
    }
}
