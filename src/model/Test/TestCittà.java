package model.Test;

import model.Città;
import model.NomeCittà;
import model.bonus.NullBonus;
import model.carte.ColoreCittà;
import model.eccezioni.CittàAdiacenteSeStessaException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emilianogagliardi on 22/05/16.
 */
public class TestCittà {
    private Città città1;
    private Città città2;
    private Città città3;
    private Città città4;

    public TestCittà(){
        città1 = new Città(NomeCittà.ARKON, ColoreCittà.ARGENTO, NullBonus.getInstance());
        try {
            città2 = new Città(NomeCittà.BURGEN, ColoreCittà.BRONZO, NullBonus.getInstance(), città1);
            città3 = new Città(NomeCittà.CASTRUM, ColoreCittà.ARGENTO, NullBonus.getInstance(), città1, città2);
            città4 = new Città(NomeCittà.ESTI, ColoreCittà.BRONZO, NullBonus.getInstance(), città3);
            città1.addCittàAdiacente(città2);
        }catch (CittàAdiacenteSeStessaException e){
            e.printStackTrace();
        }
    }

    @Test
    public void prova(){
        try {
            città2 = new Città(NomeCittà.BURGEN, ColoreCittà.BRONZO, NullBonus.getInstance(), città1);
        } catch (CittàAdiacenteSeStessaException e) {
            e.printStackTrace();
        }
    }
/*
    @Test (expected = CittàAdiacenteSeStessaException.class)
    public void aggiungiSeStessaTest() {
        Città città = new Città(NomeCittà.ARKON, ColoreCittà.FERRO, NullBonus.getInstance());
        try {
            città.addCittàAdiacente(città);
        }catch(CittàAdiacenteSeStessaException e) {e.printStackTrace();}
    }

    @Test
    public void collegamentiCittàTest() {
        assertTrue(città1.getCittàAdiacenti().contains(città2));
        assertTrue(città2.getCittàAdiacenti().contains(città1));
        assertTrue(città3.getCittàAdiacenti().contains(città1) && città3.getCittàAdiacenti().contains(città2));
        assertTrue(città4.getCittàAdiacenti().contains(città3));
        assertFalse(città1.getCittàAdiacenti().contains(città3));
        assertFalse(città1.getCittàAdiacenti().contains(città4));
        assertFalse(città2.getCittàAdiacenti().contains(città4));
    }
    */
}
