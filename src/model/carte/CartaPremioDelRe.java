package model.carte;

import model.bonus.BonusPuntiVittoria;
import model.bonus.NullBonus;

public class CartaPremioDelRe extends CartaConBonus {
    private static int contatore = 1; //TODO: questa variabile static non mi convince troppo... DA CAMBIARE!
    private int numeroOrdine;

    public CartaPremioDelRe(int puntiVittoria) {
        super(new BonusPuntiVittoria(puntiVittoria, NullBonus.getInstance()));
        numeroOrdine = contatore;
        contatore++;
    }

    public int getNumeroOrdine() { return numeroOrdine;}
}
