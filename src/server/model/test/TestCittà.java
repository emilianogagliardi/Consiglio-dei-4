

import classicondivise.NomeCittà;
import server.model.*;
import classicondivise.bonus.NullBonus;
import server.model.eccezioni.CittàAdiacenteSeStessaException;
import server.model.eccezioni.EmporioGiàEsistenteException;
import org.junit.Test;
import interfaccecondivise.InterfacciaView;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestCittà {
    private Città città1;
    private Città città2;
    private Città città3;
    private Città città4;

    public TestCittà(){
        città1 = new Città(NomeRegione.COLLINA, NomeCittà.ARKON, ColoreCittà.ARGENTO, NullBonus.getInstance(), new ArrayList<InterfacciaView>());
        città2 = new Città(NomeRegione.MONTAGNA, NomeCittà.BURGEN, ColoreCittà.BRONZO, NullBonus.getInstance(), new ArrayList<InterfacciaView>());
        città3 = new Città(NomeRegione.COSTA, NomeCittà.CASTRUM, ColoreCittà.ARGENTO, NullBonus.getInstance(), new ArrayList<InterfacciaView>());
        città4 = new Città(NomeRegione.COSTA, NomeCittà.ESTI, ColoreCittà.BRONZO, NullBonus.getInstance(), new ArrayList<InterfacciaView>());
    }

    @Test
    public void equalsTest() {
        Città c1 = new Città(NomeRegione.MONTAGNA, NomeCittà.BURGEN, ColoreCittà.BRONZO, NullBonus.getInstance(), new ArrayList<InterfacciaView>());
        Città c2 = new Città(NomeRegione.COLLINA, NomeCittà.BURGEN, ColoreCittà.FERRO, NullBonus.getInstance(), new ArrayList<InterfacciaView>());
        Città c3 = new Città(NomeRegione.MONTAGNA, NomeCittà.INDUR, ColoreCittà.FERRO, NullBonus.getInstance(), new ArrayList<InterfacciaView>());
        assertFalse(c1.equals(c3));
        assertFalse(c1.equals(c2));
    }

    @Test (expected = CittàAdiacenteSeStessaException.class)
    public void aggiungiSeStessaTest() {
        Città città = new Città(NomeRegione.COLLINA, NomeCittà.ARKON, ColoreCittà.FERRO, NullBonus.getInstance(), new ArrayList<InterfacciaView>());
        città.addCittàAdiacenti(città);
    }

    @Test
    public void collegamentiCittàTest() {
        città1.addCittàAdiacenti(città2);
        assertTrue(città1.getCittàAdiacenti().contains(città2));
        assertFalse(città2.getCittàAdiacenti().contains(città1));
        città3.addCittàAdiacenti(città1);
        città3.addCittàAdiacenti(città2);
        assertTrue(città3.getCittàAdiacenti().contains(città1) && città3.getCittàAdiacenti().contains(città2));
        assertFalse(città1.getCittàAdiacenti().contains(città3) && città2.getCittàAdiacenti().contains(città3));
        assertFalse(città4.getCittàAdiacenti().contains(città3));
        assertFalse(città1.getCittàAdiacenti().contains(città4));
        assertFalse(città2.getCittàAdiacenti().contains(città4));
    }

    @Test
    public void costruisciEmporiTest() throws EmporioGiàEsistenteException{
        Giocatore g1 = new Giocatore (0, 0, 0, new ArrayList<InterfacciaView>());
        Giocatore g2 = new Giocatore (1, 0, 0, new ArrayList<InterfacciaView>());
        Giocatore g3 = new Giocatore (2, 0, 0, new ArrayList<InterfacciaView>());
        città1.costruisciEmporio(new Emporio(g1.getId()));
        città1.costruisciEmporio(new Emporio(g2.getId()));
        città1.costruisciEmporio(new Emporio(g3.getId()));
        assertTrue(città1.giàCostruito(g1));
        assertTrue(città1.giàCostruito(g2));
        assertTrue(città1.giàCostruito(g3));
    }

}
