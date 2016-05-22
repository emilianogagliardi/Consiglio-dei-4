package model.eccezioni;

import model.Città;

/**
 * Created by emilianogagliardi on 22/05/16.
 */
public class CittàAdiacenteSeStessaException extends Exception{
    public CittàAdiacenteSeStessaException(){
        super("Una città non  può essere adiacente a se stessa");
    }
}
