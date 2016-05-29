import model.*;
import model.bonus.BonusPuntiVittoria;
import model.bonus.NullBonus;
import model.carte.CartaBonusColoreCittà;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by riccardo on 29/05/16.
 */
public class PartitaTest {
    private Partita partita1;
    private Partita partita2;
    private static int idCounter = 1;
    private Set<CartaBonusColoreCittà> carteBonusColoreCittà = new HashSet<>(Costanti.NUM_CARTE_BONUS_COLORE_CITTA);

    @Before
    public void setUp() throws Exception {
        partita1 = new Partita();
        carteBonusColoreCittà.add(new CartaBonusColoreCittà(5, ColoreCittà.ARGENTO));
        carteBonusColoreCittà.add(new CartaBonusColoreCittà(10, ColoreCittà.BRONZO));


        partita2 = new Partita();
    }

    @Test
    public void setMethods() throws Exception{
        partita1.addGiocatore(new Giocatore(idCounter++, 10, 1));
        partita1.addGiocatore(new Giocatore(idCounter++, 10, 1));
        partita1.setBalconeDelConsiglioRe(new BalconeDelConsiglio(new Consigliere(ColoreConsigliere.AZZURRO)));
    }

}