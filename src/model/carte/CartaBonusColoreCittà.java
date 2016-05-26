package model.carte;
import model.ColoreCittà;
import model.bonus.BonusPuntiVittoria;

public class CartaBonusColoreCittà extends CartaConBonus {
    private ColoreCittà colore;

    public CartaBonusColoreCittà(BonusPuntiVittoria bonus, ColoreCittà colore) {
        super(bonus);
        this.colore = colore;
    }

    public ColoreCittà getColore(){
        return colore;
    }
}
