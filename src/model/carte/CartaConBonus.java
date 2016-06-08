package model.carte;
import model.bonus.Bonus;
import model.bonus.NullBonus;

import java.util.Objects;

public abstract class CartaConBonus extends Carta {
    private Bonus bonus;

    public CartaConBonus(Bonus bonus) {
        this.bonus = Objects.requireNonNull(bonus);
    }

    public Bonus getBonus() {
        return bonus;

    }
}
