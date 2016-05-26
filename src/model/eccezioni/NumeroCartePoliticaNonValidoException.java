package model.eccezioni;


public class NumeroCartePoliticaNonValidoException extends RuntimeException {
    public NumeroCartePoliticaNonValidoException(){
        super("Il numero di carte politica scartate è negativo o nullo oppure maggiore di 4");
    }
}
