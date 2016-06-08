import model.*;

import model.carte.ColoreCartaPolitica;
import org.junit.Test;
import proxyView.InterfacciaView;;


import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestBalcone {
    private BalconeDelConsiglio balcone;

    @Test(expected = IllegalArgumentException.class)
    public void balconeVuotoTest () {
        balcone = new BalconeDelConsiglio(IdBalcone.MONTAGNA, new ArrayList<InterfacciaView>(), new ArrayList<Consigliere>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void balconeQuasiPienoTest () {
        ArrayList<Consigliere> consiglieri = new ArrayList<Consigliere>();
        consiglieri.add(new Consigliere(ColoreConsigliere.ARANCIONE));
        consiglieri.add(new Consigliere(ColoreConsigliere.AZZURRO));
        balcone = new BalconeDelConsiglio(  IdBalcone.MONTAGNA,
                                            new ArrayList<InterfacciaView>(), consiglieri);
    }

    @Test(expected = NullPointerException.class)
    public void balconeNullTest () {
        balcone = new BalconeDelConsiglio(IdBalcone.COSTA, new ArrayList<InterfacciaView>(), null);
    }

    @Test
    public void inserisciConsiglieriTest() {
        ArrayList<Consigliere> consiglieri = new ArrayList<>();
        consiglieri.add(new Consigliere(ColoreConsigliere.ARANCIONE));
        consiglieri.add(new Consigliere(ColoreConsigliere.AZZURRO));
        consiglieri.add(new Consigliere(ColoreConsigliere.BIANCO));
        consiglieri.add(new Consigliere(ColoreConsigliere.NERO));
        balcone = new BalconeDelConsiglio(IdBalcone.COLLINA, new ArrayList<InterfacciaView>(), consiglieri);
        ArrayList<Colore> c = new ArrayList<>();
        c.add(ColoreConsigliere.ARANCIONE.toColore());
        c.add(ColoreConsigliere.AZZURRO.toColore());
        c.add(ColoreConsigliere.BIANCO.toColore());
        c.add(ColoreConsigliere.NERO.toColore());
        for (int i = 0; i < 4; i++){
            assertEquals(c.get(i), balcone.getColoriConsiglieri().get(i));
        }
    }

    @Test
    public void shiftConsiglieriTest(){
        ArrayList<Consigliere> consiglieri = new ArrayList<>();
        consiglieri.add(new Consigliere(ColoreConsigliere.ARANCIONE));
        consiglieri.add(new Consigliere(ColoreConsigliere.AZZURRO));
        consiglieri.add(new Consigliere(ColoreConsigliere.BIANCO));
        consiglieri.add(new Consigliere(ColoreConsigliere.NERO));
        balcone = new BalconeDelConsiglio(  IdBalcone.COSTA, new ArrayList<InterfacciaView>(), consiglieri);
        balcone.addConsigliere(new Consigliere(ColoreConsigliere.ROSA));
        ArrayList<Colore> c = new ArrayList<>();
        c.add(ColoreConsigliere.AZZURRO.toColore());
        c.add(ColoreConsigliere.BIANCO.toColore());
        c.add(ColoreConsigliere.NERO.toColore());
        c.add(ColoreConsigliere.ROSA.toColore());
        for (int i = 0; i < 4; i++){
            assertEquals(c.get(i), balcone.getColoriConsiglieri().get(i));
        }
        balcone.addConsigliere(new Consigliere(ColoreConsigliere.VIOLA));
        c = new ArrayList<>();
        c.add(ColoreConsigliere.BIANCO.toColore());
        c.add(ColoreConsigliere.NERO.toColore());
        c.add(ColoreConsigliere.ROSA.toColore());
        c.add(ColoreConsigliere.VIOLA.toColore());
        for (int i = 0; i < 4; i++){
            assertEquals(c.get(i), balcone.getColoriConsiglieri().get(i));
        }
        balcone.addConsigliere(new Consigliere(ColoreConsigliere.ARANCIONE));
        c = new ArrayList<>();
        c.add(ColoreConsigliere.NERO.toColore());
        c.add(ColoreConsigliere.ROSA.toColore());
        c.add(ColoreConsigliere.VIOLA.toColore());
        c.add(ColoreConsigliere.ARANCIONE.toColore());
        for (int i = 0; i < 4; i++){
            assertEquals(c.get(i), balcone.getColoriConsiglieri().get(i));
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void overflowTest () {
        ArrayList<Consigliere> consiglieri = new ArrayList<>();
        consiglieri.add(new Consigliere(ColoreConsigliere.ARANCIONE));
        consiglieri.add(new Consigliere(ColoreConsigliere.AZZURRO));
        consiglieri.add(new Consigliere(ColoreConsigliere.BIANCO));
        consiglieri.add(new Consigliere(ColoreConsigliere.NERO));
        consiglieri.add(new Consigliere(ColoreConsigliere.NERO));
        balcone = new BalconeDelConsiglio(IdBalcone.COLLINA, new ArrayList<InterfacciaView>(), consiglieri);
    }

    @Test
    public void soddisfaBalconeTest(){
        ArrayList<Consigliere> consiglieri = new ArrayList<>();
        consiglieri.add(new Consigliere(ColoreConsigliere.ARANCIONE));
        consiglieri.add(new Consigliere(ColoreConsigliere.AZZURRO));
        consiglieri.add(new Consigliere(ColoreConsigliere.BIANCO));
        consiglieri.add(new Consigliere(ColoreConsigliere.NERO));
        balcone = new BalconeDelConsiglio(IdBalcone.COLLINA, new ArrayList<InterfacciaView>(), consiglieri);
        ArrayList<ColoreCartaPolitica> coloriCartePolitica = new ArrayList<>();
        coloriCartePolitica.add(ColoreCartaPolitica.NERO);
        coloriCartePolitica.add(ColoreCartaPolitica.AZZURRO);
        assertTrue(balcone.soddisfaConsiglio(coloriCartePolitica));
        coloriCartePolitica = new ArrayList<>();
        coloriCartePolitica.add(ColoreCartaPolitica.VIOLA);
        assertFalse(balcone.soddisfaConsiglio(coloriCartePolitica));
        coloriCartePolitica = new ArrayList<>();
        coloriCartePolitica.add(ColoreCartaPolitica.AZZURRO);
        coloriCartePolitica.add(ColoreCartaPolitica.AZZURRO);
        assertFalse(balcone.soddisfaConsiglio(coloriCartePolitica));
        coloriCartePolitica = new ArrayList<>();
        coloriCartePolitica.add(ColoreCartaPolitica.JOLLY);
        coloriCartePolitica.add(ColoreCartaPolitica.VIOLA);
        assertFalse(balcone.soddisfaConsiglio(coloriCartePolitica));
        coloriCartePolitica = new ArrayList<>();
        coloriCartePolitica.add(ColoreCartaPolitica.JOLLY);
        coloriCartePolitica.add(ColoreCartaPolitica.JOLLY);
        coloriCartePolitica.add(ColoreCartaPolitica.JOLLY);
        coloriCartePolitica.add(ColoreCartaPolitica.JOLLY);
        assertTrue(balcone.soddisfaConsiglio(coloriCartePolitica));
        coloriCartePolitica = new ArrayList<>();
        coloriCartePolitica.add(ColoreCartaPolitica.JOLLY);
        coloriCartePolitica.add(ColoreCartaPolitica.NERO);
        assertTrue(balcone.soddisfaConsiglio(coloriCartePolitica));
        consiglieri = new ArrayList<>();
        consiglieri.add(new Consigliere(ColoreConsigliere.ARANCIONE));
        consiglieri.add(new Consigliere(ColoreConsigliere.AZZURRO));
        consiglieri.add(new Consigliere(ColoreConsigliere.BIANCO));
        consiglieri.add(new Consigliere(ColoreConsigliere.ARANCIONE));
        balcone = new BalconeDelConsiglio( IdBalcone.COLLINA, new ArrayList<InterfacciaView>(), consiglieri);
        coloriCartePolitica = new ArrayList<>();
        coloriCartePolitica.add(ColoreCartaPolitica.ARANCIONE);
        coloriCartePolitica.add(ColoreCartaPolitica.ARANCIONE);
        assertTrue(balcone.soddisfaConsiglio(coloriCartePolitica));
        coloriCartePolitica = new ArrayList<>();
        coloriCartePolitica.add(ColoreCartaPolitica.ARANCIONE);
        coloriCartePolitica.add(ColoreCartaPolitica.ARANCIONE);
        coloriCartePolitica.add(ColoreCartaPolitica.ARANCIONE);
        assertFalse(balcone.soddisfaConsiglio(coloriCartePolitica));
    }

}
