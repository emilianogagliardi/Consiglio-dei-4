package model.eccezioni;

public class NumeroConsiglieriNonValidoException extends RuntimeException {
    public NumeroConsiglieriNonValidoException(){
        super("E' obbligatorio inserire 4 consiglieri nel balcone");
    }
}
