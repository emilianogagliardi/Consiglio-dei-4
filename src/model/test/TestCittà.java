package model.test;

import model.Città;
import model.NomeCittà;
import model.bonus.NullBonus;
import model.carte.ColoreCittà;
import model.eccezioni.CittàAdiacenteSeStessaException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emilianogagliardi on 22/05/16.
 */
public class TestCittà {
    private Città città1;
    private Città città2;
    private Città città3;
    private Città città4;

    public TestCittà(){
        città1 = new Città(NomeCittà.ARKON, ColoreCittà.ARGENTO, NullBonus.getInstance());
        città2 = new Città(NomeCittà.BURGEN, ColoreCittà.BRONZO, NullBonus.getInstance());
        città3 = new Città(NomeCittà.CASTRUM, ColoreCittà.ARGENTO, NullBonus.getInstance());
        città4 = new Città(NomeCittà.ESTI, ColoreCittà.BRONZO, NullBonus.getInstance());
    }

    @Before
    @Test
    public void aggiungiNullaTest() {
        città1.addCittàAdiacenti();
        assertTrue(città1.getCittàAdiacenti().isEmpty());
    }

    @Test
    public void equalsTest() {
        Città c1 = new Città(NomeCittà.BURGEN, ColoreCittà.BRONZO, NullBonus.getInstance());
        Città c2 = new Città(NomeCittà.BURGEN, ColoreCittà.FERRO, NullBonus.getInstance());
        Città c3 = new Città(NomeCittà.INDUR, ColoreCittà.FERRO, NullBonus.getInstance());
        assertFalse(c1.equals(c3));
        assertTrue(c1.equals(c2));
    }

    @Test (expected = CittàAdiacenteSeStessaException.class)
    public void aggiungiSeStessaTest() {
        Città città = new Città(NomeCittà.ARKON, ColoreCittà.FERRO, NullBonus.getInstance());
        città.addCittàAdiacenti(città);
    }

    @Test
    public void collegamentiCittàTest() {
        città1.addCittàAdiacenti(città2);
        assertTrue(città1.getCittàAdiacenti().contains(città2));
        assertTrue(città2.getCittàAdiacenti().contains(città1));
        città3.addCittàAdiacenti(città1, città2);
        assertTrue(città3.getCittàAdiacenti().contains(città1) && città3.getCittàAdiacenti().contains(città2));
        assertTrue(città1.getCittàAdiacenti().contains(città3) && città2.getCittàAdiacenti().contains(città3));
        assertFalse(città4.getCittàAdiacenti().contains(città3));
        assertFalse(città1.getCittàAdiacenti().contains(città4));
        assertFalse(città2.getCittàAdiacenti().contains(città4));
    }
}
