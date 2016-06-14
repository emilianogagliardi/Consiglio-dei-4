package server.model.eccezioni;

public class ImpossibileDecrementareMosseException extends RuntimeException{
    public ImpossibileDecrementareMosseException(){
        super("Non è possibile decrementare il numero di mosse del giocatore");
    }
}
