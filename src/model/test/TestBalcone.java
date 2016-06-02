import model.BalconeDelConsiglio;
import model.ColoreConsigliere;
import model.Consigliere;

import model.carte.CartaPolitica;
import model.carte.ColoreCartaPolitica;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestBalcone {
    private BalconeDelConsiglio balcone;

    @Test(expected = IllegalArgumentException.class)
    public void balconeVuotoTest () {
        balcone = new BalconeDelConsiglio();
    }

    @Test(expected = IllegalArgumentException.class)
    public void balconeQuasiPienoTest () {
        balcone = new BalconeDelConsiglio(new Consigliere(ColoreConsigliere.ARANCIONE), new Consigliere(ColoreConsigliere.AZZURRO));
    }

    @Test(expected = NullPointerException.class)
    public void balconeNullTest () {
        balcone = new BalconeDelConsiglio(null);
    }

    @Test
    public void inserisciConsiglieriTest() {
        balcone = new BalconeDelConsiglio(  new Consigliere(ColoreConsigliere.ARANCIONE),
                                            new Consigliere(ColoreConsigliere.AZZURRO),
                                            new Consigliere(ColoreConsigliere.BIANCO),
                                            new Consigliere(ColoreConsigliere.NERO));
        ArrayList<ColoreConsigliere> c = new ArrayList<>();
        c.add(ColoreConsigliere.ARANCIONE);
        c.add(ColoreConsigliere.AZZURRO);
        c.add(ColoreConsigliere.BIANCO);
        c.add(ColoreConsigliere.NERO);
        for (int i = 0; i < 4; i++){
            assertEquals(c.get(i), balcone.getColoriConsiglieri().get(i));
        }
    }

    @Test
    public void shiftConsiglieriTest(){
        balcone = new BalconeDelConsiglio(  new Consigliere(ColoreConsigliere.ARANCIONE),
                                            new Consigliere(ColoreConsigliere.AZZURRO),
                                            new Consigliere(ColoreConsigliere.BIANCO),
                                            new Consigliere(ColoreConsigliere.NERO));
        balcone.addConsigliere(new Consigliere(ColoreConsigliere.ROSA));
        ArrayList<ColoreConsigliere> c = new ArrayList<>();
        c.add(ColoreConsigliere.AZZURRO);
        c.add(ColoreConsigliere.BIANCO);
        c.add(ColoreConsigliere.NERO);
        c.add(ColoreConsigliere.ROSA);
        for (int i = 0; i < 4; i++){
            assertEquals(c.get(i), balcone.getColoriConsiglieri().get(i));
        }
        balcone.addConsigliere(new Consigliere(ColoreConsigliere.VIOLA));
        c = new ArrayList<>();
        c.add(ColoreConsigliere.BIANCO);
        c.add(ColoreConsigliere.NERO);
        c.add(ColoreConsigliere.ROSA);
        c.add(ColoreConsigliere.VIOLA);
        for (int i = 0; i < 4; i++){
            assertEquals(c.get(i), balcone.getColoriConsiglieri().get(i));
        }
        balcone.addConsigliere(new Consigliere(ColoreConsigliere.ARANCIONE));
        c = new ArrayList<>();
        c.add(ColoreConsigliere.NERO);
        c.add(ColoreConsigliere.ROSA);
        c.add(ColoreConsigliere.VIOLA);
        c.add(ColoreConsigliere.ARANCIONE);
        for (int i = 0; i < 4; i++){
            assertEquals(c.get(i), balcone.getColoriConsiglieri().get(i));
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void overflowTest () {
        balcone = new BalconeDelConsiglio(  new Consigliere(ColoreConsigliere.ARANCIONE),
                                            new Consigliere(ColoreConsigliere.AZZURRO),
                                            new Consigliere(ColoreConsigliere.BIANCO),
                                            new Consigliere(ColoreConsigliere.NERO),
                                            new Consigliere(ColoreConsigliere.NERO));
    }

    @Test
    public void soddisfaBalconeTest(){
        balcone = new BalconeDelConsiglio(  new Consigliere(ColoreConsigliere.ARANCIONE),
                                        new Consigliere(ColoreConsigliere.AZZURRO),
                                        new Consigliere(ColoreConsigliere.BIANCO),
                                         new Consigliere(ColoreConsigliere.NERO));
        assertTrue(balcone.soddisfaConsiglio(new CartaPolitica(ColoreCartaPolitica.NERO), new CartaPolitica(ColoreCartaPolitica.AZZURRO)));
        assertFalse(balcone.soddisfaConsiglio(new CartaPolitica(ColoreCartaPolitica.VIOLA)));
        assertFalse(balcone.soddisfaConsiglio(new CartaPolitica(ColoreCartaPolitica.AZZURRO), new CartaPolitica(ColoreCartaPolitica.AZZURRO)));
        assertFalse(balcone.soddisfaConsiglio(new CartaPolitica(ColoreCartaPolitica.JOLLY), new CartaPolitica(ColoreCartaPolitica.VIOLA)));
        assertTrue(balcone.soddisfaConsiglio(new CartaPolitica(ColoreCartaPolitica.JOLLY),new CartaPolitica(ColoreCartaPolitica.JOLLY),new CartaPolitica(ColoreCartaPolitica.JOLLY),new CartaPolitica(ColoreCartaPolitica.JOLLY)));
        assertTrue(balcone.soddisfaConsiglio(new CartaPolitica(ColoreCartaPolitica.JOLLY),new CartaPolitica(ColoreCartaPolitica.NERO)));
        balcone = new BalconeDelConsiglio(  new Consigliere(ColoreConsigliere.ARANCIONE),
                new Consigliere(ColoreConsigliere.AZZURRO),
                new Consigliere(ColoreConsigliere.BIANCO),
                new Consigliere(ColoreConsigliere.ARANCIONE));
        assertTrue(balcone.soddisfaConsiglio(new CartaPolitica(ColoreCartaPolitica.ARANCIONE), new CartaPolitica(ColoreCartaPolitica.ARANCIONE)));
        assertFalse(balcone.soddisfaConsiglio(new CartaPolitica(ColoreCartaPolitica.ARANCIONE), new CartaPolitica(ColoreCartaPolitica.ARANCIONE), new CartaPolitica(ColoreCartaPolitica.ARANCIONE)));
    }

}
