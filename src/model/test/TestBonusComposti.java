import model.Giocatore;
import model.bonus.*;
import org.junit.Before;
import org.junit.Test;
import proxyview.InterfacciaView;

import java.util.ArrayList;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;
import static org.junit.Assert.assertEquals;


public class TestBonusComposti {
    private Bonus b;
    private Giocatore g;
    private int n_aiutanti;
    private int n_monete;
    private int n_puntiVittoria;
    private int n_politica;

    public TestBonusComposti() {
        b = NullBonus.getInstance();
        g = new Giocatore(0,0,0, new ArrayList<InterfacciaView>());
    }

    @Before
    public void generaBonus(){
        for (int i = 0; i < 10; i++) {
            int r = (int) (Math.random()*4);
            int n = (int) (Math.random()*3) + 1;
            switch (r){
                case 0:
                    b = new BonusAiutanti(n, b);
                    n_aiutanti += n;
                    break;
                case 1:
                    b = new BonusMonete(n, b);
                    n_monete += n;
                    break;
                case 2:
                    b = new BonusPuntiVittoria(n, b);
                    n_puntiVittoria += n;
                    break;
                case 3:
                    b = new BonusAvanzaPercorsoNobiltà(n, b);
                    n_politica += n;
                    break;
                default:
                    break;
            }
        }
    }

    @Test
    public void bonusGenericoTest(){
        b.ottieni(g);
        assertEquals(n_aiutanti, g.getAiutanti());
        assertEquals(n_monete, g.getMonete());
        assertEquals(n_puntiVittoria, g.getPuntiVittoria());
        assertEquals(n_politica, g.getPosizionePercorsoNobiltà());
    }
}
