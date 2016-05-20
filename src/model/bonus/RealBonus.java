package model.bonus;

import model.Giocatore;

public abstract class RealBonus extends Bonus {
    private Bonus decoratedBonus;

    public RealBonus (Bonus decorated){
        this.decoratedBonus = decorated;
    }

    public void ottieniDecoratedBonus(Giocatore giocatore){
        if (decoratedBonus!= null) {
            decoratedBonus.ottieni(giocatore);
        }
    }

}