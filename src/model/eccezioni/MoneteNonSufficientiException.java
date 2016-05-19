package model.eccezioni;


/**
 * Created by emilianogagliardi on 18/05/16.
 */
public class MoneteNonSufficientiException extends RisorseNonSufficientiException {
    public MoneteNonSufficientiException(){
        super("Monete non sufficienti");
    }
}
