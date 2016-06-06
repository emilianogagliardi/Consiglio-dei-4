package model.carte;
import model.bonus.Bonus;
import model.bonus.NullBonus;

import java.util.Objects;

public abstract class CartaConBonus extends Carta {
    private Bonus bonus;
    private Bonus bonusDaRitornare;

    public CartaConBonus(Bonus bonus) {
        this.bonus = Objects.requireNonNull(bonus);
    }

    public CartaConBonus(){} //necessario per a serializzazione di Carta permesso costruzione

    Bonus ottieniBonus() {
        bonusDaRitornare = bonus;
        this.bonus = NullBonus.getInstance();
        return bonusDaRitornare;

    }
}
