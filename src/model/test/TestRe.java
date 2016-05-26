package model.test;

import model.Città;
import model.NomeCittà;
import model.Re;
import model.bonus.NullBonus;
import model.ColoreCittà;
import model.eccezioni.ReNonInizializzatoException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by emilianogagliardi on 25/05/16.
 */
public class TestRe {
    private Città città1;
    private Città città2;
    private Città città3;

    public TestRe () {
        città1 = new Città(NomeCittà.INDUR, ColoreCittà.BRONZO, NullBonus.getInstance());
        città2 = new Città(NomeCittà.BURGEN, ColoreCittà.ARGENTO, NullBonus.getInstance());
        città3 = new Città(NomeCittà.INDUR, ColoreCittà.FERRO, NullBonus.getInstance());
    }

    @Before
    public void setup() {
        città1.addCittàAdiacenti(città2);
        città2.addCittàAdiacenti(città3);
    }

    @Test
    public void spostaReTest() {
        Re.init(città1);
        try {
            Re re = Re.getInstance();
            assertEquals(re.getCittà(), città1);
            re.sposta(città2);
            assertEquals(re.getCittà(), città2);
            re.sposta(città3);
            assertEquals(re.getCittà(),città3);
        }catch(ReNonInizializzatoException e){e.printStackTrace();}
    }

    @Test (expected = IllegalArgumentException.class)
    public void spostaCittàNonAdiacenteTest() throws IllegalArgumentException{
        Re.init(città1);
        try {
            Re.getInstance().sposta(città3);
        }catch (ReNonInizializzatoException e){
            e.printStackTrace();
        }
    }
}
