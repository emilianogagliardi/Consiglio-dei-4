package model.bonus;

import model.Giocatore;

public class NullBonus extends Bonus {
    private static NullBonus instance;

    private NullBonus(){
    }

    public static synchronized NullBonus getInstance(){
        if (instance == null){
            instance = new NullBonus();
        }
        return instance;
    }

    public void ottieni(Giocatore giocatore) {
    }
}