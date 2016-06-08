import model.*;
import model.bonus.NullBonus;
import model.carte.*;
import model.eccezioni.AiutantiNonSufficientiException;
import model.eccezioni.MoneteNonSufficientiException;
import org.junit.After;
import org.junit.Test;
import proxyView.InterfacciaView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGiocatore {
    private Giocatore giocatore;

    public TestGiocatore() {
        giocatore = new Giocatore(0, 0, 0, new ArrayList<InterfacciaView>());
    }

    /*
        test underflow
     */
    @Test (expected = MoneteNonSufficientiException.class)
    public void underflowMoneteTest() throws MoneteNonSufficientiException{
        Giocatore giocatore1 = new Giocatore(0,0,0, new ArrayList<InterfacciaView>());
        giocatore1.pagaMonete(1);
    }

    @Test (expected = AiutantiNonSufficientiException.class)
    public void underflowAiutantiTest() throws AiutantiNonSufficientiException{
        Giocatore giocatore1 = new Giocatore(0,0,0, new ArrayList<InterfacciaView>());
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
        CartaBonusRegione cbr = new CartaBonusRegione(NomeRegione.COSTA, 2);
        CartaBonusColoreCittà cbcc = new CartaBonusColoreCittà(1, ColoreCittà.FERRO);
        CartaPremioDelRe cpr = new CartaPremioDelRe(2);
        ArrayList<Città> città = new ArrayList<>();
        città.add(new Città(NomeCittà.BURGEN, ColoreCittà.BRONZO, NullBonus.getInstance(), new ArrayList<InterfacciaView>()));
        CartaPermessoCostruzione cpc = new CartaPermessoCostruzione(NullBonus.getInstance(),città);
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
        Giocatore g1 = new Giocatore(0,0,0, new ArrayList<InterfacciaView>());
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.AZZURRO));
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.JOLLY));
        g1.addCarta(new CartaPolitica(ColoreCartaPolitica.JOLLY));
        ArrayList<ColoreCartaPolitica> colori = new ArrayList<>();
        colori.add(ColoreCartaPolitica.ARANCIONE);
        colori.add(ColoreCartaPolitica.JOLLY);
        colori.add(ColoreCartaPolitica.ARANCIONE);
        colori.add(ColoreCartaPolitica.AZZURRO);
        g1.scartaCartePolitica(colori);
        List<CartaPolitica> mano = g1.getManoCartePolitica();
        assertFalse(mano.contains(new CartaPolitica(ColoreCartaPolitica.AZZURRO)));
        assertTrue(mano.contains(new CartaPolitica(ColoreCartaPolitica.ARANCIONE)));
        assertTrue(mano.contains(new CartaPolitica(ColoreCartaPolitica.JOLLY)));
        assertTrue(mano.size() == 2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void scartaCartaNonEsistente() {
        Giocatore gi = new Giocatore(0,0,0, new ArrayList<InterfacciaView>());
        ArrayList<ColoreCartaPolitica> colori = new ArrayList<>();
        colori.add(ColoreCartaPolitica.ARANCIONE);
        gi.scartaCartePolitica(colori);
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
