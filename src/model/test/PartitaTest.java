

import model.*;
import model.bonus.BonusPuntiVittoria;
import model.bonus.NullBonus;
import model.carte.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

public class PartitaTest {
    private Partita partita1;
    private Partita partita2;
    private static int idCounter = 1;
    private Set<CartaBonusColoreCittà> carteBonusColoreCittà1 = new HashSet<>(Costanti.NUM_CARTE_BONUS_COLORE_CITTA);
    private Mazzo<CartaPolitica> mazzoCartePolitica1 = new Mazzo<>();
    private Mazzo<CartaPremioDelRe> mazzoCartaPremioRe1 =  new Mazzo<>();

    @Before
    public void setUp() throws Exception {
        partita1 = new Partita();
        carteBonusColoreCittà1.add(new CartaBonusColoreCittà(5, ColoreCittà.ARGENTO));
        carteBonusColoreCittà1.add(new CartaBonusColoreCittà(10, ColoreCittà.BRONZO));
        carteBonusColoreCittà1.add(new CartaBonusColoreCittà(15, ColoreCittà.FERRO));
        carteBonusColoreCittà1.add(new CartaBonusColoreCittà(20, ColoreCittà.ORO));
        int cheColore = 0;
        for (int i = 0; i < 90; i++){
           if ((i % 13) == 12)
               cheColore++;
            switch(cheColore){
                case 0:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
                case 1:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.AZZURRO));
                case 2:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.BIANCO));
                case 3:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.NERO));
                case 4:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.ROSA));
                case 5:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.VIOLA));
                case 6:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.JOLLY));

            }
        }
        mazzoCartePolitica1.mischia();

        partita2 = new Partita();
    }

    @Test
    public void setMethods() throws Exception{
        partita1.addGiocatore(new Giocatore(idCounter++, 10, 1));
        partita1.addGiocatore(new Giocatore(idCounter++, 11, 2));
        partita1.setBalconeDelConsiglioRe(new BalconeDelConsiglio(new Consigliere(ColoreConsigliere.AZZURRO),
                                                                    new Consigliere(ColoreConsigliere.BIANCO),
                                                                    new Consigliere(ColoreConsigliere.BIANCO),
                                                                    new Consigliere(ColoreConsigliere.NERO)));
        partita1.setCarteBonusColoreCittà(carteBonusColoreCittà1);
        partita1.setMazzoCartePolitica(mazzoCartePolitica1);
        partita1.setCartePoliticaScartate(new Mazzo<CartaPolitica>());
        //partita1.setMazzoCartePremioRe();
    }

}