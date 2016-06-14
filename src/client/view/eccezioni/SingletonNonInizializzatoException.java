package client.view.eccezioni;

/**
 * Created by emilianogagliardi on 14/06/16.
 */
public class SingletonNonInizializzatoException extends Exception{
    public SingletonNonInizializzatoException() {
        super("singleton non inizializzato non può essere ottenuto da getInstance, chiamare prima init");
    }
}
