package server.model.carte;
import server.model.NomeRegione;
import server.model.bonus.BonusPuntiVittoria;
import server.model.bonus.NullBonus;

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
