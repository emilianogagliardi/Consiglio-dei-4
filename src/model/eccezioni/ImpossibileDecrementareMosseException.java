package eccezioni;

/**
 * Created by emilianogagliardi on 18/05/16.
 */
public class ImpossibileDecrementareMosseException extends Exception{
    public ImpossibileDecrementareMosseException(){
        super("Non è possibile decrementare il numero di mosse del giocatore");
    }
}
