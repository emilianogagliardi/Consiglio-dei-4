package model.test;

import model.*;
import model.bonus.BonusPuntiVittoria;
import model.bonus.NullBonus;
import model.carte.*;
import model.eccezioni.AiutantiNonSufficientiException;
import model.eccezioni.MoneteNonSufficientiException;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGiocatore {
    private Giocatore giocatore;

    public TestGiocatore() {
        giocatore = new Giocatore(0, 0, 0);
    }

    /*
        test underflow
     */
    @Test (expected = MoneteNonSufficientiException.class)
    public void underflowMoneteTest() throws MoneteNonSufficientiException{
        Giocatore giocatore1 = new Giocatore(0,0,0);
        giocatore1.pagaMonete(1);
    }

    @Test (expected = AiutantiNonSufficientiException.class)
    public void underflowAiutantiTest() throws AiutantiNonSufficientiException{
        Giocatore giocatore1 = new Giocatore(0,0,0);
        giocatore.pagaAiutanti(1);
    }

    /*
        test valori negativi
     */
    @Test(expected = IllegalArgumentException.class)
    public void moneteNegativeTest() {
        giocatore.guadagnaMonete(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void aiutantiNegativiTest(){
        giocatore.guadagnaAiutanti(-2);
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

    @Test
    public void pescaCartaTest(){
        CartaPolitica cp = new CartaPolitica(ColoreCartaPolitica.ARANCIONE);
        CartaBonusRegione cbr = new CartaBonusRegione(2);
        CartaBonusColoreCittà cbcc = new CartaBonusColoreCittà(1, ColoreCittà.FERRO);
        CartaPremioDelRe cpr = new CartaPremioDelRe(2);
        CartaPermessoCostruzione cpc = new CartaPermessoCostruzione(NullBonus.getInstance(), new Città(NomeCittà.BURGEN, ColoreCittà.BRONZO, NullBonus.getInstance()));
        giocatore.addCarta(cp);
        assertTrue(giocatore.getManoCartePolitica().contains(cp));
        giocatore.addCarta(cbr);
        assertTrue(giocatore.getManoCarteBonusRegione().contains(cbr));
        giocatore.addCarta(cbcc);
        assertTrue(giocatore.getManoCarteBonusColoreCittà().contains(cbcc));
        giocatore.addCarta(cpr);
        assertTrue(giocatore.getManoCartePremioDelRe().contains(cpr));
        giocatore.addCarta(cpc);
        assertTrue(giocatore.getManoCartePermessoCostruzione().contains(cpc));
    }

    @Test
    public void scartaCartePoliticaTest() {
        Giocatore g1 = new Giocatore(0,0,0);
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.AZZURRO));
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.JOLLY));
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.JOLLY));
        g1.scartaCartePolitica( new CartaPolitica(ColoreCartaPolitica.ARANCIONE),
                                new CartaPolitica(ColoreCartaPolitica.JOLLY),
                                new CartaPolitica(ColoreCartaPolitica.ARANCIONE),
                                new CartaPolitica(ColoreCartaPolitica.AZZURRO));
        List<CartaPolitica> mano = g1.getManoCartePolitica();
        assertFalse(mano.contains(new CartaPolitica(ColoreCartaPolitica.AZZURRO)));
        assertTrue(mano.contains(new CartaPolitica(ColoreCartaPolitica.ARANCIONE)));
        assertTrue(mano.contains(new CartaPolitica(ColoreCartaPolitica.JOLLY)));
        assertTrue(mano.size() == 2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void scartaCartaNonEsistente() {
        Giocatore gi = new Giocatore(0,0,0);
        gi.scartaCartePolitica(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
    }

    @Test
    public void copriCartePermessoCostruzioneTest() {
        Giocatore g1 = new Giocatore(0,0,0);
        CartaPermessoCostruzione c1 = new CartaPermessoCostruzione(NullBonus.getInstance(), new Città(NomeCittà.BURGEN, ColoreCittà.ARGENTO, NullBonus.getInstance()));
        CartaPermessoCostruzione c2 = new CartaPermessoCostruzione(NullBonus.getInstance(), new Città(NomeCittà.BURGEN, ColoreCittà.ARGENTO, NullBonus.getInstance()));
        CartaPermessoCostruzione c3 = new CartaPermessoCostruzione(NullBonus.getInstance(), new Città(NomeCittà.INDUR, ColoreCittà.BRONZO, NullBonus.getInstance()));
        g1.addCarta(c1);
        g1.addCarta(c2);
        g1.addCarta(c3);
        g1.copriCartaPermessoCostruzione(c1);
        assertTrue(g1.getCartePermessoCostruzioneCoperte().contains(c1));
        assertTrue(g1.getCartePermessoCostruzioneCoperte().size() == 1);
        assertTrue (g1.getManoCartePermessoCostruzione().size() == 2);
        ArrayList<CartaPermessoCostruzione> a = new ArrayList<>(); //carte ancora in mano in questo punto
        a.add(c2);
        a.add(c3);
        assertTrue(g1.getManoCartePermessoCostruzione().containsAll(a));
        assertTrue(a.containsAll(g1.getManoCartePermessoCostruzione()));
        g1.copriCartaPermessoCostruzione(c3);
        ArrayList<CartaPermessoCostruzione> a2 = new ArrayList<>(); //carte già coperte
        a2.add(c1);
        a2.add(c3);
        assertTrue(g1.getCartePermessoCostruzioneCoperte().containsAll(a2));
        assertTrue(a2.containsAll(g1.getCartePermessoCostruzioneCoperte()));
        assertTrue(g1.getManoCartePermessoCostruzione().contains(c2));
        assertTrue(g1.getManoCartePermessoCostruzione().size() == 1);
        g1.copriCartaPermessoCostruzione(c2);
        assertTrue(g1.getManoCartePermessoCostruzione().isEmpty());
    }

    @Test (expected = IllegalArgumentException.class)
    public void copriCartePermessoCostruzioneNonEsistentiTest() {
        Giocatore g1 = new Giocatore(0,0,0);
        CartaPermessoCostruzione c = new CartaPermessoCostruzione(NullBonus.getInstance(), new Città(NomeCittà.BURGEN, ColoreCittà.ARGENTO, NullBonus.getInstance()));
        g1.copriCartaPermessoCostruzione(c);
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
