package model.bonus;

import model.Giocatore;

public class BonusAiutanti extends RealBonus {
    private int numeroAiutanti;

    public BonusAiutanti(int a, Bonus decorated) throws IllegalArgumentException{
        super(decorated);
        if (a <= 0) throw new IllegalArgumentException("Non è possibile creare un bonus aiutanti con un numero negativo o nullo di aiutanti");
        numeroAiutanti = a;
    }

    public int getNumeroAiutanti(){ return numeroAiutanti;}
}