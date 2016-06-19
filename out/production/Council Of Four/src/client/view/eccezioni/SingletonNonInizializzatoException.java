package client.view.eccezioni;


public class SingletonNonInizializzatoException extends Exception{
    public SingletonNonInizializzatoException() {
        super("singleton non inizializzato non può essere ottenuto da getInstance, chiamare prima init");
    }
}
