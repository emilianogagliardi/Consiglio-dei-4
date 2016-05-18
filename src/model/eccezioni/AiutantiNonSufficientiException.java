package model.eccezioni;

/**
 * Created by emilianogagliardi on 18/05/16.
 */
public class AiutantiNonSufficientiException extends RisorseNonSufficientiException {
    public AiutantiNonSufficientiException(){
        super("Monete non sufficienti");
    }
}