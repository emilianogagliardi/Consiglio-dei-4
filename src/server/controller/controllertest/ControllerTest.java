

import server.controller.Controller;
import server.model.*;
import server.model.bonus.*;
import server.model.carte.*;
import org.junit.Test;
import interfaccecondivise.InterfacciaView;

import java.lang.reflect.Field;
import java.util.*;


import static org.junit.Assert.*;

public class ControllerTest {
    private ArrayList<InterfacciaView> proxyViews;
    private Controller controller;
    Field azioniPrincipaliDisponibili;
    Field azioneVeloceEseguita;

    //attributi di partita
    private Partita partita;
    private static int idCounter = 1;
    private HashSet<CartaBonusColoreCittà> carteBonusColoreCittà1 = new HashSet<>(CostantiModel.NUM_CARTE_BONUS_COLORE_CITTA);
    private Mazzo<CartaPolitica> mazzoCartePolitica1 = new Mazzo<>();
    private Mazzo<CartaPremioDelRe> mazzoCartaPremioRe1 =  new Mazzo<>();
    List<Bonus> percorsoDellaNobiltà1 = new ArrayList<>(CostantiModel.MAX_POS_NOBILTA);
    HashSet<Regione> regioni1 = new HashSet<>(CostantiModel.NUM_REGIONI);
    Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzioneCosta1 = new Mazzo<>();
    Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzioneCollina1 = new Mazzo<>();
    Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzioneMontagna1 = new Mazzo<>();
    private ArrayList<Giocatore> giocatori;

    public ControllerTest(){
        partita = new Partita(new ArrayList<InterfacciaView>());
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
        for(int i = 0; i < CostantiModel.MAX_POS_NOBILTA; i++){
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
        for (int i = 0; i < CostantiModel.NUM_CARTE_PERMESSO_REGIONE; i++){
            mazzoCartePermessoCostruzioneCosta1.addCarta(new CartaPermessoCostruzione(new BonusPuntiVittoria(2, NullBonus.getInstance()), new ArrayList<>()));
        }
        ArrayList<Consigliere> consiglieri = new ArrayList<>();
        consiglieri.addAll(Arrays.asList(new Consigliere(ColoreConsigliere.VIOLA), new Consigliere(ColoreConsigliere.ARANCIONE), new Consigliere(ColoreConsigliere.AZZURRO), new Consigliere(ColoreConsigliere.ARANCIONE)));
        regioni1.add(new Regione(NomeRegione.COSTA, mazzoCartePermessoCostruzioneCosta1, new BalconeDelConsiglio(IdBalcone.COSTA, new ArrayList<InterfacciaView>(), consiglieri), new CartaBonusRegione(NomeRegione.COSTA, 6), new ArrayList<InterfacciaView>()));
        ArrayList<NomeCittà> cittàCartePermessoCostruzione = new ArrayList<>();
        cittàCartePermessoCostruzione.add(NomeCittà.ARKON);
        for (int i = 0; i < CostantiModel.NUM_CARTE_PERMESSO_REGIONE; i++){
            mazzoCartePermessoCostruzioneCollina1.addCarta(new CartaPermessoCostruzione(new BonusPuntiVittoria(6, NullBonus.getInstance()), cittàCartePermessoCostruzione));
        }
        regioni1.add(new Regione(NomeRegione.COLLINA, mazzoCartePermessoCostruzioneCollina1, new BalconeDelConsiglio(IdBalcone.COLLINA, new ArrayList<InterfacciaView>(), consiglieri), new CartaBonusRegione(NomeRegione.COLLINA, 6),new ArrayList<InterfacciaView>()));
        for (int i = 0; i < CostantiModel.NUM_CARTE_PERMESSO_REGIONE; i++){
            mazzoCartePermessoCostruzioneMontagna1.addCarta(new CartaPermessoCostruzione(new BonusPuntiVittoria(10, NullBonus.getInstance()), cittàCartePermessoCostruzione));
        }
        regioni1.add(new Regione(NomeRegione.MONTAGNA, mazzoCartePermessoCostruzioneMontagna1, new BalconeDelConsiglio(IdBalcone.MONTAGNA, new ArrayList<InterfacciaView>(), consiglieri), new CartaBonusRegione(NomeRegione.MONTAGNA, 1), new  ArrayList<InterfacciaView>()));
        consiglieri = new ArrayList<>();
        consiglieri.add(new Consigliere(ColoreConsigliere.AZZURRO));
        consiglieri.add(new Consigliere(ColoreConsigliere.BIANCO));
        consiglieri.add(new Consigliere(ColoreConsigliere.BIANCO));
        consiglieri.add(new Consigliere(ColoreConsigliere.NERO));
        partita.setBalconeDelConsiglioRe(new BalconeDelConsiglio(IdBalcone.RE, new ArrayList<InterfacciaView>(), consiglieri));
        partita.setCarteBonusColoreCittà(carteBonusColoreCittà1);
        partita.setMazzoCartePolitica(mazzoCartePolitica1);
        partita.setMazzoCartePremioRe(mazzoCartaPremioRe1);
        partita.setPercorsoDellaNobiltà(percorsoDellaNobiltà1);
        partita.setRegioni(regioni1);
        partita.setRe(new Re(new Città(NomeRegione.COLLINA, NomeCittà.INDUR, ColoreCittà.ARGENTO, new BonusRipetiAzionePrincipale(NullBonus.getInstance()),new ArrayList<InterfacciaView>()), new ArrayList<InterfacciaView>()));
        ArrayList<Consigliere> riservaConsiglieri = new ArrayList<>();
        Consigliere consigliereRiserva;
        riservaConsiglieri.add( new Consigliere(ColoreConsigliere.ARANCIONE));
        riservaConsiglieri.add(new Consigliere(ColoreConsigliere.BIANCO));
        riservaConsiglieri.add(new Consigliere(ColoreConsigliere.NERO));
        riservaConsiglieri.add(new Consigliere(ColoreConsigliere.ROSA));
        riservaConsiglieri.add(new Consigliere(ColoreConsigliere.VIOLA));
        riservaConsiglieri.add(new Consigliere(ColoreConsigliere.BIANCO));
        partita.setRiservaConsiglieri(riservaConsiglieri);
        proxyViews = new ArrayList<>();
        proxyViews.add(new InterfacciaView() {
            @Override
            public void setIdGiocatore(int idGiocatore) {

            }

            @Override
            public int getIdGiocatore() {
                return 0;
            }

            @Override
            public int scegliMappa() {
                return 1;
            }

            @Override
            public void erroreDiConnessione() {

            }

            @Override
            public void updatePuntiVittoriaGiocatore(int idGiocatore, int punti) {

            }

            @Override
            public void updateBalcone(String regione, String colore1, String colore2, String colore3, String colore4) {

            }

            @Override
            public void updateMonete(int idGiocatore, int idMonete) {

            }

            @Override
            public void updateCartePoliticaAvversari(int idGiocatore, int numCarte) {

            }

            @Override
            public void updateCartePoliticaProprie(List<String> carte) {

            }

            @Override
            public void updateCartePermessoRegione(String regione, CartaPermessoCostruzione c1, CartaPermessoCostruzione c2) {

            }

            @Override
            public void updateCartePermessoGiocatore(int idGiocatore, List<CartaPermessoCostruzione> mano) {

            }

            @Override
            public void updateAiutanti(int idGiocatore, int numAiutanti) {

            }

            @Override
            public void updateRiservaAiutanti(int numAiutanti) {

            }

            @Override
            public void updateRiservaConsiglieri(List<String> coloriConsiglieri) {

            }

            @Override
            public void updatePercorsoNobiltà(int idGiocatore, int posizione) {

            }

            @Override
            public void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori) {

            }

            @Override
            public void updateEmporiDisponibiliGiocatore(int idGiocatore, int num) {

            }

            @Override
            public void updateCarteBonusColoreCittàGiocatore(int idGiocatore, HashMap<String, Integer> mapCarte) {

            }

            @Override
            public void updateCarteBonusColoreCittàTabellone(HashMap<String, Integer> coloriEPunti) {

            }

            @Override
            public void updateCarteBonusReGiocatore(int idGiocatore, HashMap<String, Integer> mapCarte) {

            }

            @Override
            public void updateCarteBonusReTabellone(int puntiVIttoriaPrimaCarta) {

            }

            @Override
            public void updateCarteBonusRegioneGiocatore(int idGiocatore, HashMap<String, Integer> carte) {

            }

            @Override
            public void updateCarteBonusRegioneTabellone(String nomeRegione, int puntiCarta) {

            }

            @Override
            public void updatePosizioneRe(String città) {

            }

            @Override
            public void eseguiTurno() {

            }}); //passo un'implementazione di InterfacciaView al volo con tutti i metodi vuoti tranne scegliMappa e
        //getIdGiocatore che ritornano il valore 1 e 0 rispettivamente
        proxyViews.add(new InterfacciaView() {
            @Override
            public void setIdGiocatore(int idGiocatore) {

            }

            @Override
            public int getIdGiocatore() {
                return 1;
            }

            @Override
            public int scegliMappa() {
                return 1;
            }

            @Override
            public void erroreDiConnessione() {

            }

            @Override
            public void updatePuntiVittoriaGiocatore(int idGiocatore, int punti) {

            }

            @Override
            public void updateBalcone(String regione, String colore1, String colore2, String colore3, String colore4) {

            }

            @Override
            public void updateMonete(int idGiocatore, int idMonete) {

            }

            @Override
            public void updateCartePoliticaAvversari(int idGiocatore, int numCarte) {

            }

            @Override
            public void updateCartePoliticaProprie(List<String> carte) {

            }

            @Override
            public void updateCartePermessoRegione(String regione, CartaPermessoCostruzione c1, CartaPermessoCostruzione c2) {

            }

            @Override
            public void updateCartePermessoGiocatore(int idGiocatore, List<CartaPermessoCostruzione> mano) {

            }

            @Override
            public void updateAiutanti(int idGiocatore, int numAiutanti) {

            }

            @Override
            public void updateRiservaAiutanti(int numAiutanti) {

            }

            @Override
            public void updateRiservaConsiglieri(List<String> coloriConsiglieri) {

            }

            @Override
            public void updatePercorsoNobiltà(int idGiocatore, int posizione) {

            }

            @Override
            public void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori) {

            }

            @Override
            public void updateEmporiDisponibiliGiocatore(int idGiocatore, int num) {

            }

            @Override
            public void updateCarteBonusColoreCittàGiocatore(int idGiocatore, HashMap<String, Integer> mapCarte) {

            }

            @Override
            public void updateCarteBonusColoreCittàTabellone(HashMap<String, Integer> coloriEPunti) {

            }

            @Override
            public void updateCarteBonusReGiocatore(int idGiocatore, HashMap<String, Integer> mapCarte) {

            }

            @Override
            public void updateCarteBonusReTabellone(int puntiVIttoriaPrimaCarta) {

            }

            @Override
            public void updateCarteBonusRegioneGiocatore(int idGiocatore, HashMap<String, Integer> carte) {

            }

            @Override
            public void updateCarteBonusRegioneTabellone(String nomeRegione, int puntiCarta) {

            }

            @Override
            public void updatePosizioneRe(String città) {

            }

            @Override
            public void eseguiTurno() {

            }
        });
        //creo i giocatori
        giocatori = new ArrayList<>();
        for (int i = 0; i < proxyViews.size(); i++) {
            InterfacciaView viewCorrente = proxyViews.get(i);
            int idGiocatore = viewCorrente.getIdGiocatore();
            Giocatore giocatore = new Giocatore(idGiocatore, CostantiModel.MONETE_INIZIALI_GIOCATORI[i], CostantiModel.AIUTANTI_INIZIALI_GIIOCATORI[i], proxyViews);
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.JOLLY));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.NERO));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.VIOLA));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.AZZURRO));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.AZZURRO));
            giocatori.add(giocatore);
        }
        giocatori.forEach(partita::addGiocatore);
        controller = new Controller(partita, proxyViews);
        try {
            azioniPrincipaliDisponibili = Controller.class.getDeclaredField("azioniPrincipaliDisponibili");
            azioniPrincipaliDisponibili.setAccessible(true);
            azioniPrincipaliDisponibili.set(controller, 1);
            azioneVeloceEseguita = Controller.class.getDeclaredField("azioneVeloceEseguita");
            azioneVeloceEseguita.setAccessible(true);
            azioneVeloceEseguita.set(controller, false);
        } catch (NoSuchFieldException exc){
            exc.printStackTrace();
        } catch (IllegalAccessException exc){
            exc.printStackTrace();
        }
    }

    @Test
    public void eleggiConsigliereTest(){
        //il giocatore pesca una carta politica
        resetGiocatore();
        giocatori.get(0).addCarta(partita.ottieniCartaPolitica());
        int moneteOld = giocatori.get(0).getMonete();
        assertTrue(controller.eleggereConsigliere("COSTA", "BIANCO"));
        assertTrue(giocatori.get(0).getMonete() == (moneteOld + 4));
        resetGiocatore();
        assertTrue(controller.eleggereConsigliere("COSTA", "BIANCO"));
        resetGiocatore();
        assertFalse(controller.eleggereConsigliere("COSTA", "BIANCO"));
    }

    @Test
    public void acquistareTesseraPermessoCostruzione(){
        resetGiocatore();
        giocatori.get(0).addCarta(partita.ottieniCartaPolitica());
        ArrayList<String> nomiColoriCartePolitica = new ArrayList<>();
        nomiColoriCartePolitica.add("ARANCIONE");
        nomiColoriCartePolitica.add("ARANCIONE");
        nomiColoriCartePolitica.add("JOLLY");
        int moneteOld = giocatori.get(0).getMonete();
        int numTessereCostruzioneOld = giocatori.get(0).getManoCartePermessoCostruzione().size();
        int puntiVittoriaOld = giocatori.get(0).getPuntiVittoria();
        assertTrue(controller.acquistareTesseraPermessoCostruzione("COSTA", nomiColoriCartePolitica, 2));
        assertTrue(giocatori.get(0).getMonete() == moneteOld - 5);
        assertEquals("Non è stata aggiunta la carta permesso", numTessereCostruzioneOld+1, giocatori.get(0).getManoCartePermessoCostruzione().size());
        assertEquals("Non sono stati asseganti i bonus", puntiVittoriaOld+2, giocatori.get(0).getPuntiVittoria());
        resetGiocatore();
        nomiColoriCartePolitica = new ArrayList<>();
        nomiColoriCartePolitica.add("NERO");
        nomiColoriCartePolitica.add("ARANCIONE");
        nomiColoriCartePolitica.add("JOLLY");
        assertFalse(controller.acquistareTesseraPermessoCostruzione("COSTA", nomiColoriCartePolitica, 1));

    }

    @Test
    public void costruireEmporioConTesseraPermessoCostruzione(){

    }

    private void resetGiocatore(){
        try {
            azioniPrincipaliDisponibili.set(controller, 1);
            azioneVeloceEseguita.set(controller, false);
        } catch (IllegalAccessException exc){
            exc.printStackTrace();
        }
    }
}