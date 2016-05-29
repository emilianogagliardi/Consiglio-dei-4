

import model.BalconeDelConsiglio;
import model.ColoreConsigliere;
import model.Consigliere;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void inserisciConsiglieriTest() {
        balcone = new BalconeDelConsiglio(  new Consigliere(ColoreConsigliere.ARANCIONE),
                                            new Consigliere(ColoreConsigliere.AZZURRO),
                                            new Consigliere(ColoreConsigliere.BIANCO),
                                            new Consigliere(ColoreConsigliere.NERO));
        ArrayList<ColoreConsigliere> c = new ArrayList<>();
        c.add(ColoreConsigliere.NERO);
        c.add(ColoreConsigliere.BIANCO);
        c.add(ColoreConsigliere.AZZURRO);
        c.add(ColoreConsigliere.ARANCIONE);
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
        c.add(ColoreConsigliere.ROSA);
        /*c.add(ColoreConsigliere.NERO);
        c.add(ColoreConsigliere.BIANCO);
        c.add(ColoreConsigliere.AZZURRO);*/
        c.add(ColoreConsigliere.ARANCIONE);
        c.add(ColoreConsigliere.AZZURRO);
        c.add(ColoreConsigliere.BIANCO);
        for (int i = 0; i < 4; i++){
            assertEquals(c.get(i), balcone.getColoriConsiglieri().get(i));
        }
        balcone.addConsigliere(new Consigliere(ColoreConsigliere.VIOLA));
        c = new ArrayList<>();
        c.add(ColoreConsigliere.VIOLA);
        c.add(ColoreConsigliere.ROSA);
        c.add(ColoreConsigliere.NERO);
        c.add(ColoreConsigliere.BIANCO);
        for (int i = 0; i < 4; i++){
            assertEquals(c.get(i), balcone.getColoriConsiglieri().get(i));
        }
        balcone.addConsigliere(new Consigliere(ColoreConsigliere.ARANCIONE));
        c = new ArrayList<>();
        c.add(ColoreConsigliere.ARANCIONE);
        c.add(ColoreConsigliere.VIOLA);
        c.add(ColoreConsigliere.ROSA);
        c.add(ColoreConsigliere.NERO);
        for (int i = 0; i < 4; i++){
            assertEquals(c.get(i), balcone.getColoriConsiglieri().get(i));
        }
    }

    @After
    @Test (expected = IllegalStateException.class)
    public void overflowTest () {
        balcone.addConsigliere(new Consigliere(ColoreConsigliere.NERO));
    }

}
