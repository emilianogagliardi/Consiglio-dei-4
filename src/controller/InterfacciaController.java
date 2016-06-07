package controller;

import model.bonus.Bonus;

public interface InterfacciaController {
    boolean fineTurno();
    void ottieniBonus(Bonus bonus);
    boolean eleggereConsigliere(String coloreConsigliere, String balcone);
}
