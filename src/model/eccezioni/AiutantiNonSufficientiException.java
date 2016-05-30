package model.eccezioni;

public class AiutantiNonSufficientiException extends RisorseNonSufficientiException {
    public AiutantiNonSufficientiException(){
        super("Aiutanti non sufficienti");
    }
}