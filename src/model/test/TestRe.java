import model.Città;
import model.NomeCittà;
import model.Re;
import model.bonus.NullBonus;
import model.ColoreCittà;
import org.junit.Before;
import org.junit.Test;
import proxyview.InterfacciaView;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestRe {
    private Re re;
    private Città città1;
    private Città città2;
    private Città città3;

    public TestRe () {
        città1 = new Città(NomeCittà.INDUR, ColoreCittà.BRONZO, NullBonus.getInstance(), new ArrayList<InterfacciaView>());
        città2 = new Città(NomeCittà.BURGEN, ColoreCittà.ARGENTO, NullBonus.getInstance(), new ArrayList<InterfacciaView>());
        città3 = new Città(NomeCittà.INDUR, ColoreCittà.FERRO, NullBonus.getInstance(), new ArrayList<InterfacciaView>());
        re = new Re(città1, new ArrayList<InterfacciaView>());
    }

    @Before
    public void setup() {
        città1.addCittàAdiacenti(città2);
        città2.addCittàAdiacenti(città3);
    }

    @Test
    public void spostaReTest() {
        assertEquals(re.getCittà(), città1);
        re.setPosizione(città2);
        assertEquals(re.getCittà(), città2);
        re.setPosizione(città3);
        assertEquals(re.getCittà(),città3);
    }

}
