package model.eccezioni;

import model.Città;

/**
 * Created by emilianogagliardi on 22/05/16.
 */
public class CittàAdiacenteSeStessaException extends RuntimeException{
    public CittàAdiacenteSeStessaException(){
        super("Una città non  può essere adiacente a se stessa");
    }
}
