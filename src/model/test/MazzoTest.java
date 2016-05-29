package model.test;

import model.ColoreCittà;
import model.Mazzo;
import model.bonus.BonusPuntiVittoria;
import model.bonus.NullBonus;
import model.carte.*;
import org.junit.Before;
import org.junit.Test;


public class MazzoTest {
    private CartaBonusColoreCittà cartaBonusColoreCittà1, cartaBonusColoreCittà2;
    private CartaBonusRegione cartaBonusRegione;
    private CartaPermessoCostruzione cartaPermessoCostruzione;
    private CartaPremioDelRe cartaPremioDelRe;
    private Mazzo<CartaBonusColoreCittà> mazzoCartaBonusColoreCittà;

    @Before
    public void setUp() throws Exception {
        cartaBonusColoreCittà1 = new CartaBonusColoreCittà(1, ColoreCittà.ORO);
        cartaBonusColoreCittà2 = new CartaBonusColoreCittà(100000000, ColoreCittà.ORO);
        mazzoCartaBonusColoreCittà = new Mazzo<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void creaCartaNonValida(){
        CartaBonusColoreCittà cartaBonusColoreCittàNonValida = new CartaBonusColoreCittà(0, ColoreCittà.BRONZO);
    }

    @Test
    public void addCarta() throws Exception {
        mazzoCartaBonusColoreCittà.addCarta(cartaBonusColoreCittà1);
        mazzoCartaBonusColoreCittà.addCarta(cartaBonusColoreCittà2);
    }

    @Test
    public void addCarte() throws Exception {

    }

    @Test
    public void getCarta() throws Exception {

    }

    @Test
    public void mischia() throws Exception {
    }

    @Test
    public void isEmpty() throws Exception {

    }

}