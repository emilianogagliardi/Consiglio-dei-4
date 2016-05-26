package model.eccezioni;


import model.Costanti;

public class NumeroConsiglieriRiservaNonValidoException extends RuntimeException {
    public NumeroConsiglieriRiservaNonValidoException(){
        super("Il numero di consiglieri in riserva deve essere " + Costanti.NUM_CONSIGLIERI_RISERVA);
    }

}

