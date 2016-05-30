package model.eccezioni;

public class CittàAdiacenteSeStessaException extends RuntimeException{
    public CittàAdiacenteSeStessaException(){
        super("Una città non  può essere adiacente a se stessa");
    }
}
