package classicondivise.carte;

import classicondivise.bonus.Bonus;

import java.util.Objects;

public abstract class CartaConBonus extends Carta {
    private Bonus bonus;

    public CartaConBonus(Bonus bonus) {
        this.bonus = Objects.requireNonNull(bonus);
    }

    public CartaConBonus(){ //per la serializzazione di carta permesso
        //do nothing
    }

    public Bonus getBonus() {
        return bonus;
    }

}
