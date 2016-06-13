package controller;

import model.carte.CartaPermessoCostruzione;

import java.util.List;

public interface InterfacciaController {
    void passaTurno(); //la View chiama passaTurno per passare il turno al giocatore successivo
    boolean eleggereConsigliere(String balcone, String coloreConsigliere);
    boolean acquistareTesseraPermessoCostruzione(String balcone, List<String> cartePolitica, int carta); //carta indica quale delle due carte permesso costruzione di regione ha scelto il giocatore (1 o 2)
    boolean costruireEmporioConTesseraPermessoCostruzione(CartaPermessoCostruzione cartaPermessoCostruzione, String città);
    boolean costruireEmporioConAiutoRe(List<String> nomiColoriCartePolitica, String nomeCittàCostruzione);
}
