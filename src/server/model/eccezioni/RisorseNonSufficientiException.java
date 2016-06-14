package server.model.eccezioni;

public class RisorseNonSufficientiException extends Exception{
    public RisorseNonSufficientiException(){
        super();
    }
    public RisorseNonSufficientiException(String s){
        super (s);
    }
}
