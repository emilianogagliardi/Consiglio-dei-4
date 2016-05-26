package model.eccezioni;

public class NumeroRegioniNonValidoException extends RuntimeException {
    public NumeroRegioniNonValidoException(){
        super("Le regioni devono essere 3");
    }
}
