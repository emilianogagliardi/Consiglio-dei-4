package model.test;

import model.Costanti;
import model.Giocatore;
import model.eccezioni.AiutantiNonSufficientiException;
import model.eccezioni.MoneteNonSufficientiException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by emilianogagliardi on 24/05/16.
 */
public class TestGiocatore {
    private Giocatore giocatore;

    public TestGiocatore() {
        giocatore = new Giocatore(0, 0, 0, 0);
    }

    /*
        test di underflow
     */
    @Before
    @Test (expected = MoneteNonSufficientiException.class)
    public void underflowMoneteTest(){
        try {
            giocatore.pagaMonete(1);
        }catch(MoneteNonSufficientiException e){e.printStackTrace();}
    }

    @Before
    @Test (expected = AiutantiNonSufficientiException.class)
    public void underflowAiutantiTest(){
        try {
            giocatore.pagaAiutanti(1);
        }catch(AiutantiNonSufficientiException e) {e.printStackTrace();}
    }

    /*
        test guadagna risorse
     */
    @Test
    public void guadagnaMoneteTest() {
        giocatore.guadagnaMonete(3);
        assertEquals(3, giocatore.getMonete());
        giocatore.guadagnaMonete(5);
        assertEquals(8, giocatore.getMonete());
    }

    @Test
    public void guadagnaAiutantiTest() {
        giocatore.guadagnaAiutanti(3);
        assertEquals(3, giocatore.getAiutanti());
        giocatore.guadagnaAiutanti(4);
        assertEquals(7, giocatore.getAiutanti());
    }

    @Test
    public void avanzaPercorsoNobiltàTest() {
        giocatore.avanzaPercorsoNobiltà(2);
        assertEquals(2, giocatore.getPosizionePercorsoNobiltà());
        giocatore.avanzaPercorsoNobiltà(3);
        assertEquals(5, giocatore.getPosizionePercorsoNobiltà());
    }

    /*
        test overflow
     */

    @After
    @Test
    public void overflowMoneteTest() {
        giocatore.guadagnaMonete(Costanti.MAX_MONETE);
        assertEquals(Costanti.MAX_MONETE, giocatore.getMonete());
        giocatore.guadagnaMonete(13);
        assertEquals(Costanti.MAX_MONETE, giocatore.getMonete());
    }

    @After
    @Test
    public void overflowPercorsoNobiltàTest() {
        giocatore.avanzaPercorsoNobiltà(Costanti.MAX_POS_NOBILTA);
        assertEquals(Costanti.MAX_POS_NOBILTA, giocatore.getPosizionePercorsoNobiltà());
        giocatore.avanzaPercorsoNobiltà(21);
        assertEquals(Costanti.MAX_POS_NOBILTA, giocatore.getPosizionePercorsoNobiltà());
    }
}
