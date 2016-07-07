package server.model.eccezioni;


public class MoneteNonSufficientiException extends Exception {
    public MoneteNonSufficientiException(){
        super("Monete non sufficienti");
    }
}
