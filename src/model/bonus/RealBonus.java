package model.bonus;

public abstract class RealBonus extends Bonus {
    private RealBonus decoratedBonus;

    public RealBonus (RealBonus decorated){
        this.decoratedBonus = decorated;
    }

    public void ottieniDecoratedBonus(Giocatore giocatore){
        if (decoratedBonus!= null) {
            decoratedBonus.ottieni(giocatore);
        }
    }

}