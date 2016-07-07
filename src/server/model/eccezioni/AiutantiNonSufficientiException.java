package server.model.eccezioni;

public class AiutantiNonSufficientiException extends Exception {
    public AiutantiNonSufficientiException(){
        super("Aiutanti non sufficienti");
    }
}