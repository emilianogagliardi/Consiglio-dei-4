package controller;

import model.bonus.Bonus;
import model.carte.CartaPermessoCostruzione;

import java.util.ArrayList;

public interface InterfacciaController {
    boolean fineTurno();
    void ottieniBonus(Bonus bonus);
    boolean eleggereConsigliere(String balcone, String coloreConsigliere);
    boolean acquistareTesseraPermessoCostruzione(String balcone, ArrayList<String> cartePolitica, String nomeRegione, int carta);
  
}
