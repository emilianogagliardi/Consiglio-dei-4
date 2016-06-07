import model.*;
import model.bonus.*;
import model.carte.*;
import org.junit.Before;
import org.junit.Test;
import proxyView.InterfacciaView;

import java.util.*;

public class PartitaTest {
    private Partita partita1;
    private Partita partita2;
    private static int idCounter = 1;
    private HashSet<CartaBonusColoreCittà> carteBonusColoreCittà1 = new HashSet<>(Costanti.NUM_CARTE_BONUS_COLORE_CITTA);
    private Mazzo<CartaPolitica> mazzoCartePolitica1 = new Mazzo<>();
    private Mazzo<CartaPremioDelRe> mazzoCartaPremioRe1 =  new Mazzo<>();
    List<Bonus> percorsoDellaNobiltà1 = new ArrayList<>(Costanti.MAX_POS_NOBILTA);
    HashSet<Regione> regioni1 = new HashSet<>(Costanti.NUM_REGIONI);
    Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzioneCosta1 = new Mazzo<>();
    Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzioneCollina1 = new Mazzo<>();
    Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzioneMontagna1 = new Mazzo<>();

    @Before
    public void setUp() throws Exception {
        partita1 = new Partita(new ArrayList<InterfacciaView>());
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
                    break;
                case 1:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.AZZURRO));
                    break;
                case 2:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.BIANCO));
                    break;
                case 3:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.NERO));
                    break;
                case 4:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.ROSA));
                    break;
                case 5:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.VIOLA));
                    break;
                case 6:
                    mazzoCartePolitica1.addCarta(new CartaPolitica(ColoreCartaPolitica.JOLLY));
                    break;
                default:
                    break;
            }
        }
        mazzoCartePolitica1.mischia();
        mazzoCartaPremioRe1.addCarta(new CartaPremioDelRe(5));
        mazzoCartaPremioRe1.addCarta(new CartaPremioDelRe(10));
        mazzoCartaPremioRe1.addCarta(new CartaPremioDelRe(20));
        mazzoCartaPremioRe1.addCarta(new CartaPremioDelRe(25));
        mazzoCartaPremioRe1.addCarta(new CartaPremioDelRe(30));
        Random random = new Random();
        for(int i = 0; i < Costanti.MAX_POS_NOBILTA; i++){
            if(random.nextDouble() < 0.3){
                switch (random.nextInt(4)){
                    case 0:
                        percorsoDellaNobiltà1.add(new BonusPuntiVittoria(10, NullBonus.getInstance()));
                        break;
                    case 1:
                        percorsoDellaNobiltà1.add(new BonusAiutanti(1, NullBonus.getInstance()));
                        break;
                    case 2:
                        percorsoDellaNobiltà1.add(new BonusAvanzaPercorsoNobiltà(3, new BonusAiutanti(3, NullBonus.getInstance())));
                        break;
                    case 3:
                        percorsoDellaNobiltà1.add(new BonusMonete(5, new BonusPescaCartaPolitica(2, NullBonus.getInstance())));
                        break;
                    default:
                        break;
                }
            }
            else percorsoDellaNobiltà1.add(NullBonus.getInstance());
        }
        for (int i = 0; i < Costanti.NUM_CARTE_PERMESSO_REGIONE; i++){
            mazzoCartePermessoCostruzioneCosta1.addCarta(new CartaPermessoCostruzione(new BonusPuntiVittoria(2, NullBonus.getInstance())));
        }
        regioni1.add(new Regione(NomeRegione.COSTA, mazzoCartePermessoCostruzioneCosta1, new BalconeDelConsiglio(NomeRegione.COSTA, new ArrayList<InterfacciaView>(), new Consigliere(ColoreConsigliere.VIOLA), new Consigliere(ColoreConsigliere.BIANCO), new Consigliere(ColoreConsigliere.AZZURRO), new Consigliere(ColoreConsigliere.VIOLA)), new CartaBonusRegione(6)));
        for (int i = 0; i < Costanti.NUM_CARTE_PERMESSO_REGIONE; i++){
            mazzoCartePermessoCostruzioneCollina1.addCarta(new CartaPermessoCostruzione(new BonusPuntiVittoria(6, NullBonus.getInstance())));
        }
        regioni1.add(new Regione(NomeRegione.COLLINA, mazzoCartePermessoCostruzioneCollina1, new BalconeDelConsiglio(NomeRegione.COLLINA, new ArrayList<InterfacciaView>(), new Consigliere(ColoreConsigliere.ARANCIONE), new Consigliere(ColoreConsigliere.AZZURRO), new Consigliere(ColoreConsigliere.AZZURRO), new Consigliere(ColoreConsigliere.VIOLA)), new CartaBonusRegione(3)));
        for (int i = 0; i < Costanti.NUM_CARTE_PERMESSO_REGIONE; i++){
            mazzoCartePermessoCostruzioneMontagna1.addCarta(new CartaPermessoCostruzione(new BonusPuntiVittoria(10, NullBonus.getInstance())));
        }
        regioni1.add(new Regione(NomeRegione.MONTAGNA, mazzoCartePermessoCostruzioneMontagna1, new BalconeDelConsiglio(NomeRegione.MONTAGNA, new ArrayList<InterfacciaView>(), new Consigliere(ColoreConsigliere.NERO), new Consigliere(ColoreConsigliere.ROSA), new Consigliere(ColoreConsigliere.NERO), new Consigliere(ColoreConsigliere.NERO)), new CartaBonusRegione(1)));

        partita2 = new Partita(new ArrayList<InterfacciaView>());
    }

    @Test
    public void setMethods() throws Exception{
        partita1.addGiocatore(new Giocatore(idCounter++, 10, 1, new ArrayList<InterfacciaView>()));
        partita1.addGiocatore(new Giocatore(idCounter++, 11, 2, new ArrayList<InterfacciaView>()));
        partita1.setBalconeDelConsiglioRe(new BalconeDelConsiglio(  NomeRegione.COSTA,
                                                                    new ArrayList<InterfacciaView>(),
                                                                    new Consigliere(ColoreConsigliere.AZZURRO),
                                                                    new Consigliere(ColoreConsigliere.BIANCO),
                                                                    new Consigliere(ColoreConsigliere.BIANCO),
                                                                    new Consigliere(ColoreConsigliere.NERO)));
        partita1.setCarteBonusColoreCittà(carteBonusColoreCittà1);
        partita1.setMazzoCartePolitica(mazzoCartePolitica1);
        partita1.setCartePoliticaScartate(new Mazzo<CartaPolitica>());
        partita1.setMazzoCartePremioRe(mazzoCartaPremioRe1);
        partita1.setPercorsoDellaNobiltà(percorsoDellaNobiltà1);
        partita1.setRegioni(regioni1);
    }

}