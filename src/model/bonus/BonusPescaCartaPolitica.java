package model.bonus;

import model.Giocatore;
import model.Mazzo;
import model.carte.CartaPolitica;

public class BonusPescaCartaPolitica extends RealBonus {
    private int numeroCarte;

    public BonusPescaCartaPolitica(int numeroCarte, Bonus decorated) throws IllegalArgumentException{
        super(decorated);
        if (numeroCarte <= 0) throw new IllegalArgumentException("Non è possibile generare un bonus pesca carte politica con un numero nullo o negativo di carte");
        this.numeroCarte = numeroCarte;
    }

    public int getNumeroCarte() {
        return numeroCarte;
    }
}