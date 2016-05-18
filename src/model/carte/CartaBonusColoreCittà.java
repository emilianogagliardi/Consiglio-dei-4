package model.carte;

/**
 * Created by riccardo on 18/05/16.
 */
public class CartaBonusColoreCittà extends CartaConBonus {
    private Bonus bonus;
    private ColoreCittà colore;

    public CartaBonusColoreCittà(Bonus bonus, ColoreCittà colore) {
        this.bonus = bonus;
        this.colore = colore;
    }

    public ColoreCittà getColore(){
        return colore;
    }
}
