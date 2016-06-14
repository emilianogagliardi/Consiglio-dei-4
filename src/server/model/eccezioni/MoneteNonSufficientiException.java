package server.model.eccezioni;


public class MoneteNonSufficientiException extends RisorseNonSufficientiException {
    public MoneteNonSufficientiException(){
        super("Monete non sufficienti");
    }
}
