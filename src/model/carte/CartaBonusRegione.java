package model.carte;
import model.bonus.Bonus;
import model.bonus.BonusPuntiVittoria;
import model.bonus.NullBonus;

public class CartaBonusRegione extends CartaConBonus {

    public CartaBonusRegione (int puntiVittoria){
        super(new BonusPuntiVittoria(puntiVittoria, NullBonus.getInstance()));
    }
}
