package model.eccezioni;

import model.Costanti;

public class NumeroCarteBonusColoreCittàNonValidoException extends RuntimeException{
    public NumeroCarteBonusColoreCittàNonValidoException(){
        super("Le carte bonus colore città devono essere" + Costanti.NUM_CARTE_BONUS_COLORE_CITTA);
    }
}
