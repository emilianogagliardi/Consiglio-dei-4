package server.model.carte;

import server.model.bonus.BonusPuntiVittoria;
import server.model.bonus.NullBonus;

import static server.model.Costanti.NUM_CARTE_PREMIO_RE;

public class CartaPremioDelRe extends CartaConBonus {
    private static int contatore = 1;
    private int numeroOrdine;

    public CartaPremioDelRe(int puntiVittoria) {
        super(new BonusPuntiVittoria(puntiVittoria, NullBonus.getInstance()));
        numeroOrdine = contatore;
        contatore++;
        if(contatore == (NUM_CARTE_PREMIO_RE + 1))
            contatore = 1;
    }

    public int getNumeroOrdine() { return numeroOrdine;}
}
