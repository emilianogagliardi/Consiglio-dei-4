package server.model.carte;

import classicondivise.carte.CartaConBonus;
import classicondivise.bonus.BonusPuntiVittoria;
import classicondivise.bonus.NullBonus;

import static server.model.CostantiModel.NUM_CARTE_PREMIO_RE;

public class CartaPremioDelRe extends CartaConBonus {
    private static int contatore = 1;
    private int numeroOrdine;

    public CartaPremioDelRe(int puntiVittoria) {
        super(new BonusPuntiVittoria(puntiVittoria, NullBonus.getInstance()));
        numeroOrdine = contatore;
        contatore++;
        if(contatore == (NUM_CARTE_PREMIO_RE + 1)) //le carte premio del re sono contraddistinte da un numero ordine che viene incrementato di uno ogni nuova carta creata
        //e resettato quando raggiunge un certo valore soglia
            contatore = 1;
    }

    public int getNumeroOrdine() { return numeroOrdine;}
}
