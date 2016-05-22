package model.test;

import model.Giocatore;
import model.bonus.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by emilianogagliardi on 20/05/16.
 */
public class TestBonusSingoli {
    private Giocatore giocatore;
    private BonusAiutanti bonusA;
    private BonusMonete bonusM;
    private BonusAvanzaPercorsoNobiltà bonusN;
    private BonusPuntiVittoria bonusP;
    //TODO testi bonus pesca carta politica

    @Before
    public void setUp(){
        giocatore = new Giocatore(1, 0, 0, 0);
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

    @Test (expected = IllegalArgumentException.class)
    public void buonusErratoTest(){
        RealBonus b = new BonusAiutanti(3, null);
    }
}
