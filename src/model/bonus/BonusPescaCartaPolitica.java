package model.bonus;

import model.Giocatore;
import model.Mazzo;
import model.carte.CartaPolitica;

public class BonusPescaCartaPolitica extends RealBonus {
    private int numeroCarte;
    private Mazzo<CartaPolitica> mazzo;

    public BonusPescaCartaPolitica(int n, Mazzo<CartaPolitica> mazzo, Bonus decorated) throws IllegalArgumentException{
        super(decorated);
        if (n <= 0) throw new IllegalArgumentException("Non è possibile generare un bonus pesca carte politica con un numero nullo o negativo di carte");
        numeroCarte = n;
        this.mazzo = mazzo;
    }

    public void ottieni(Giocatore giocatore) {
        for (int i = 0; i < numeroCarte; i++) {
            giocatore.addCarta(mazzo.getCarta());
        }
        super.ottieniDecoratedBonus(giocatore);
    }

}