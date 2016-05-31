package model.bonus;

import model.Giocatore;
import model.Mazzo;
import model.carte.CartaPolitica;

public class BonusPescaCartaPolitica extends RealBonus {
    private int numeroCarte;
    private Mazzo<CartaPolitica> mazzo;

    public BonusPescaCartaPolitica(int numeroCarte, Mazzo<CartaPolitica> mazzo, Bonus decorated) throws IllegalArgumentException{
        super(decorated);
        if (numeroCarte <= 0) throw new IllegalArgumentException("Non è possibile generare un bonus pesca carte politica con un numero nullo o negativo di carte");
        this.numeroCarte = numeroCarte;
        this.mazzo = mazzo;
    }

    public void ottieni(Giocatore giocatore) {
        for (int i = 0; i < numeroCarte; i++) {
            giocatore.addCarta(mazzo.ottieniCarta());
        }
        super.ottieniDecoratedBonus(giocatore);
    }

}