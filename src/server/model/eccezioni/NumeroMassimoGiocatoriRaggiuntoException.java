package server.model.eccezioni;


import server.sistema.CostantiSistema;

public class NumeroMassimoGiocatoriRaggiuntoException extends RuntimeException {
    public NumeroMassimoGiocatoriRaggiuntoException(){
        super("Non si possono avere più di " + CostantiSistema.NUM_GOCATORI_MAX + " per partita");
    }
}
