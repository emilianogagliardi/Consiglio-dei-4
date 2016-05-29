import model.BalconeDelConsiglio;
import model.Giocatore;
import model.Partita;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by riccardo on 29/05/16.
 */
public class PartitaTest {
    private Partita partita1;
    private Partita partita2;
    private static int idCounter = 1;

    @Before
    public void setUp() throws Exception {
        partita1 = new Partita();
        partita2 = new Partita();
    }

    @Test
    public void setMethods() throws Exception{
        partita1.addGiocatore(new Giocatore(idCounter++, 10, 1, 0));
        partita1.addGiocatore(new Giocatore(idCounter++, 10, 1, 0));
        partita1.setBalconeDelConsiglioRe(new BalconeDelConsiglio(new ));
    }

}