package model.carte;
import model.bonus.Bonus;
import model.bonus.NullBonus;

public abstract class CartaConBonus extends Carta {
    private Bonus bonus;
    private Bonus bonusDaRitornare;

    public CartaConBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    Bonus getBonus() {
        bonusDaRitornare = bonus;
        this.bonus = NullBonus.getInstance();
        return bonusDaRitornare;

    }
}
