package model.bonus;

import model.Giocatore;

public class BonusPuntiVittoria extends RealBonus {
    private int puntiVittoria;

    public BonusPuntiVittoria(int p, Bonus decorated) throws IllegalArgumentException{
        super(decorated);
        if (p <= 0) throw new IllegalArgumentException("Non è possibile creare bonus punti vittoria con un numero negativo o nullo di punti");
        puntiVittoria = p;
    }

    public void ottieni(Giocatore giocatore) {
        giocatore.guadagnaPuntiVittoria(puntiVittoria);
        ottieniDecoratedBonus(giocatore);
    }
}
