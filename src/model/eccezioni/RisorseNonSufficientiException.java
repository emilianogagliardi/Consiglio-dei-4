package eccezioni;

/**
 * Created by emilianogagliardi on 18/05/16.
 */
public class RisorseNonSufficientiException extends Exception{
    public RisorseNonSufficientiException(){
        super();
    }
    public RisorseNonSufficientiException(String s){
        super (s);
    }
}
