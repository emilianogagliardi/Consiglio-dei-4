package server.model.carte;
import classicondivise.carte.CartaConBonus;
import server.model.NomeRegione;
import classicondivise.bonus.BonusPuntiVittoria;
import classicondivise.bonus.NullBonus;

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
