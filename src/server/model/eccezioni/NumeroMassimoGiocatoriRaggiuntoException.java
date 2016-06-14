package server.model.eccezioni;

import static server.model.CostantiModel.MAX_GIOCATORI;

public class NumeroMassimoGiocatoriRaggiuntoException extends RuntimeException {
    public NumeroMassimoGiocatoriRaggiuntoException(){
        super("Non si possono avere più di " + MAX_GIOCATORI + " per partita");
    }
}
