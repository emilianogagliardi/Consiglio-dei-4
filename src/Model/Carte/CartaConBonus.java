package Model.Carte;

/**
 * Created by riccardo on 18/05/16.
 */
public abstract class CartaConBonus extends Carta {
    Bonus getBonus() {
        return bonus;
        bonus = new NullBonus();
    }
}
