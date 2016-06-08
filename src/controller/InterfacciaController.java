package controller;

import model.BalconeDelConsiglio;
import model.bonus.Bonus;
import model.carte.CartaPolitica;

import java.util.AbstractList;
import java.util.ArrayList;

public interface InterfacciaController {
    boolean fineTurno();
    void ottieniBonus(Bonus bonus);
    boolean eleggereConsigliere(String coloreConsigliere, String balcone);
    boolean acquistareTesseraPermessoCostruzione(String balcone, ArrayList<String> cartePolitica, String nomeRegione, int carta);
}
