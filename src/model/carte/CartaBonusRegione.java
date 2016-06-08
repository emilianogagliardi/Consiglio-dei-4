package model.carte;
import model.NomeRegione;
import model.bonus.BonusPuntiVittoria;
import model.bonus.NullBonus;

public class CartaBonusRegione extends CartaConBonus {
    private NomeRegione regione;

    public CartaBonusRegione (NomeRegione nomeRegione, int puntiVittoria){
        super(new BonusPuntiVittoria(puntiVittoria, NullBonus.getInstance()));
        regione = nomeRegione;
    }

    public NomeRegione getNomeRegione() {
        return regione;
    }
}
