package model.bonus;

import model.Giocatore;

public abstract class RealBonus extends Bonus {
    private Bonus decoratedBonus;

    public RealBonus (Bonus decorated) throws IllegalArgumentException{
        if (decorated == null) throw new IllegalArgumentException("Non è possibile passare un bonus nullo come decorato, usare NullBonus.getInstance()");
        this.decoratedBonus = decorated;
    }

    public void ottieniDecoratedBonus(Giocatore giocatore){
        decoratedBonus.ottieni(giocatore);
    }

}