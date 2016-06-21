package classicondivise.bonus;

import java.io.Serializable;

public class BonusAvanzaPercorsoNobiltà extends RealBonus {
    private int numeroPosti;

    public BonusAvanzaPercorsoNobiltà(int n, Bonus decorated) throws IllegalArgumentException{
        super (decorated);
        if (n <= 0) throw new IllegalArgumentException("impossibile creare un bonus avanzamento percorso nobiltà con un numero negativo o nullo di poszioni");
        numeroPosti = n;
    }

    public int getNumeroPosti() {
        return numeroPosti;
    }


}