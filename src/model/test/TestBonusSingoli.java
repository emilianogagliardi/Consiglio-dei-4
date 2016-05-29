package model.test;

import model.Giocatore;
import model.Mazzo;
import model.bonus.*;
import model.carte.CartaPolitica;
import model.carte.ColoreCartaPolitica;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestBonusSingoli {
    private Giocatore giocatore;
    private BonusAiutanti bonusA;
    private BonusMonete bonusM;
    private BonusAvanzaPercorsoNobiltà bonusN;
    private BonusPuntiVittoria bonusP;
    //TODO testi bonus pesca carta politica

    @Before
    public void setUp(){
        giocatore = new Giocatore(1, 0, 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void bonusAiutantiTest(){
        bonusA = new BonusAiutanti(3, NullBonus.getInstance());
        bonusA.ottieni(giocatore);
        assertEquals(3, giocatore.getAiutanti());
        bonusA = new BonusAiutanti(-1, NullBonus.getInstance());
    }

    @Test (expected = IllegalArgumentException.class)
    public void bonusMoneteTest(){
        bonusM = new BonusMonete(2, NullBonus.getInstance());
        bonusM.ottieni(giocatore);
        assertEquals(2, giocatore.getMonete());
        bonusM = new BonusMonete(-4, NullBonus.getInstance());
    }

    @Test (expected = IllegalArgumentException.class)
    public void bonusAvanzaPercorsoPoliticaTest(){
        bonusN = new BonusAvanzaPercorsoNobiltà(4, NullBonus.getInstance());
        bonusN.ottieni(giocatore);
        assertEquals(4, giocatore.getPosizionePercorsoNobiltà());
        bonusN = new BonusAvanzaPercorsoNobiltà(-2, NullBonus.getInstance());
    }

    @Test (expected = IllegalArgumentException.class)
    public void bonusPuntiVittoriaTest(){
        bonusP = new BonusPuntiVittoria(4, NullBonus.getInstance());
        bonusP.ottieni(giocatore);
        assertEquals(4, giocatore.getPuntiVittoria());
        bonusP = new BonusPuntiVittoria(-2, NullBonus.getInstance());
    }

    @Test
    public void bonusPescaCartaPoliticaTest(){
        Mazzo<CartaPolitica> m = new Mazzo<>();
        m.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
        m.addCarta(new CartaPolitica(ColoreCartaPolitica.AZZURRO));
        m.addCarta(new CartaPolitica(ColoreCartaPolitica.JOLLY));
        BonusPescaCartaPolitica b = new BonusPescaCartaPolitica(3, m, NullBonus.getInstance());
        b.ottieni(giocatore);
        ArrayList<CartaPolitica> c_aggiunte = new ArrayList<>();
        c_aggiunte.add(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
        c_aggiunte.add(new CartaPolitica(ColoreCartaPolitica.JOLLY));
        c_aggiunte.add(new CartaPolitica(ColoreCartaPolitica.AZZURRO));
        assertTrue(giocatore.getManoCartePolitica().containsAll(c_aggiunte));
    }

    @Test (expected = IllegalArgumentException.class)
    public void buonusErratoTest(){
        RealBonus b = new BonusAiutanti(3, null);
    }
}
