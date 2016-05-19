package model.carte;

import model.bonus.BonusPuntiVittoria;

public class CartaPremioDelRe extends CartaConBonus {
    private static int contatore = 1;
    private int numeroOrdine;

    public CartaPremioDelRe(BonusPuntiVittoria bonus) {
        super(bonus);
        numeroOrdine = contatore;
        contatore++;
    }

    public int getNumeroOrdine() { return numeroOrdine;}
}
