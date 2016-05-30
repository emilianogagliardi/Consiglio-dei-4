package model.carte;
import model.ColoreCittà;
import model.bonus.BonusPuntiVittoria;
import model.bonus.NullBonus;

public class CartaBonusColoreCittà extends CartaConBonus {
    private ColoreCittà colore;

    public CartaBonusColoreCittà(int puntiVittoria, ColoreCittà colore) {
        super(new BonusPuntiVittoria(puntiVittoria, NullBonus.getInstance()));
        this.colore = colore;
    }

    public ColoreCittà getColore(){
        return colore;
    }
}
