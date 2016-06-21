

import classicondivise.*;
import classicondivise.bonus.*;
import classicondivise.carte.CartaPermessoCostruzione;
import server.controller.Controller;
import server.model.*;
import server.model.carte.*;
import org.junit.Test;
import interfaccecondivise.InterfacciaView;
import server.sistema.AvviatorePartita;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.rmi.RemoteException;
import java.util.*;


import static org.junit.Assert.*;

public class ControllerTest {
    private ArrayList<InterfacciaView> proxyViews;
    private Controller controller;
    private Field azioniPrincipaliDisponibili;
    private Field azioneVeloceEseguita;
    private Field giocatoreCorrente;
    private AvviatorePartita avviatorePartita;

    //attributi di partita
    private Partita partita;
    private static int idCounter = 1;
    private HashSet<CartaBonusColoreCittà> carteBonusColoreCittà1 = new HashSet<>(CostantiModel.NUM_CARTE_BONUS_COLORE_CITTA);
    private Mazzo<CartaPolitica> mazzoCartePolitica1 = new Mazzo<>();
    private Mazzo<CartaPremioDelRe> mazzoCartaPremioRe1 =  new Mazzo<>();
    private List<Bonus> percorsoDellaNobiltà1 = new ArrayList<>(CostantiModel.MAX_POS_NOBILTA);
    private HashSet<Regione> regioni1 = new HashSet<>(CostantiModel.NUM_REGIONI);
    private Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzioneCosta1 = new Mazzo<>();
    private Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzioneCollina1 = new Mazzo<>();
    private Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzioneMontagna1 = new Mazzo<>();
    private Properties pro;
    private HashSet<Città> tutteLeCittà, cittàRegione;
    private ArrayList<Giocatore> giocatori;
    private CartaPermessoCostruzione cartaPermessoCostruzione, cartaPermessoCostruzione2, cartaPermessoCostruzione3, cartaPermessoCostruzione4, cartaPermessoCostruzione5;

    public ControllerTest() throws RemoteException {
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
            mazzoCartePermessoCostruzioneCosta1.addCarta(new CartaPermessoCostruzione(new BonusPuntiVittoria(2, NullBonus.getInstance()), new HashSet<NomeCittà>()));
        }
        ArrayList<Consigliere> consiglieri = new ArrayList<>();
        consiglieri.addAll(Arrays.asList(new Consigliere(ColoreConsigliere.VIOLA), new Consigliere(ColoreConsigliere.ARANCIONE), new Consigliere(ColoreConsigliere.AZZURRO), new Consigliere(ColoreConsigliere.ARANCIONE)));
        regioni1.add(new Regione(NomeRegione.COSTA, mazzoCartePermessoCostruzioneCosta1, new BalconeDelConsiglio(IdBalcone.COSTA, new ArrayList<InterfacciaView>(), consiglieri), new CartaBonusRegione(NomeRegione.COSTA, 6), new ArrayList<InterfacciaView>()));
        HashSet<NomeCittà> cittàCartePermessoCostruzione = new HashSet<>();
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
        consiglieri.add(new Consigliere(ColoreConsigliere.ARANCIONE));
        consiglieri.add(new Consigliere(ColoreConsigliere.ARANCIONE));
        consiglieri.add(new Consigliere(ColoreConsigliere.BIANCO));
        consiglieri.add(new Consigliere(ColoreConsigliere.NERO));
        partita.setBalconeDelConsiglioRe(new BalconeDelConsiglio(IdBalcone.RE, new ArrayList<InterfacciaView>(), consiglieri));
        partita.setCarteBonusColoreCittà(carteBonusColoreCittà1);
        partita.setMazzoCartePolitica(mazzoCartePolitica1);
        partita.setMazzoCartePremioRe(mazzoCartaPremioRe1);
        partita.setPercorsoDellaNobiltà(percorsoDellaNobiltà1);
        partita.setRegioni(regioni1);
        partita.setRe(new Re(new Città(NomeRegione.COLLINA, NomeCittà.JUVELAR, ColoreCittà.ARGENTO, new BonusRipetiAzionePrincipale(NullBonus.getInstance()),new ArrayList<InterfacciaView>()), new ArrayList<InterfacciaView>()));
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
            public void scegliMappa() {

            }

            @Override
            public void iniziaAGiocare(int idMappa) throws RemoteException {

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
            public void updateBonusCittà(Bonus bonus) throws RemoteException {

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

            @Override
            public void fineTurno() throws RemoteException {

            }

            @Override
            public void mostraMessaggio(String messaggio) {

            }

            @Override
            public void vendi() throws RemoteException {

            }

            @Override
            public void compra() throws RemoteException {

            }

            @Override
            public void updateVetrinaMarket(VetrinaMarket vetrinaMarket) throws RemoteException {

            }
        }); //passo un'implementazione di InterfacciaView al volo con tutti i metodi vuoti tranne scegliMappa e
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
            public void scegliMappa() {
            }

            @Override
            public void iniziaAGiocare(int idMappa) throws RemoteException {

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
            public void updateBonusCittà(Bonus bonus) throws RemoteException {

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

            @Override
            public void fineTurno() throws RemoteException {

            }

            @Override
            public void mostraMessaggio(String messaggio) {

            }

            @Override
            public void vendi() throws RemoteException {

            }

            @Override
            public void compra() throws RemoteException {

            }

            @Override
            public void updateVetrinaMarket(VetrinaMarket vetrinaMarket) throws RemoteException {

            }
        });

        avviatorePartita = new AvviatorePartita(proxyViews, 0);
        //ottengo le città dal file di properties
        try {
            FileInputStream is = new FileInputStream("./serverResources/fileconfigmappe/mappa1");
            pro = new Properties();
            pro.load(is);
        }catch(FileNotFoundException e) {
            System.out.println("impossibile trovare il file di configurazione della mappa");
        } catch (IOException e) {
            System.out.println("impossibile trovare il file di configurazione della mappa");
        }
        tutteLeCittà = creaCittàDaFile(pro);
        creaSentieriCittàDaFile(pro, tutteLeCittà);
        cittàRegione = cittàInRegioniDaFile(pro, NomeRegione.COLLINA, tutteLeCittà);
        partita.getRegione(NomeRegione.COLLINA).addCittà(cittàRegione);
        cittàRegione = cittàInRegioniDaFile(pro, NomeRegione.COSTA, tutteLeCittà);
        partita.getRegione(NomeRegione.COSTA).addCittà(cittàRegione);
        cittàRegione = cittàInRegioniDaFile(pro, NomeRegione.MONTAGNA, tutteLeCittà);
        partita.getRegione(NomeRegione.MONTAGNA).addCittà(cittàRegione);


        //creo i giocatori
        giocatori = new ArrayList<>();
        for (int i = 0; i < proxyViews.size(); i++) {
            InterfacciaView viewCorrente = proxyViews.get(i);
            int idGiocatore = 0;
            try {
                idGiocatore = viewCorrente.getIdGiocatore();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Giocatore giocatore = new Giocatore(idGiocatore, CostantiModel.MONETE_INIZIALI_GIOCATORI[i], CostantiModel.AIUTANTI_INIZIALI_GIIOCATORI[i], proxyViews);
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.JOLLY));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.NERO));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.VIOLA));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.AZZURRO));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.AZZURRO));
            HashSet< NomeCittà> cittàs = new HashSet<>();
            cittàs.add(NomeCittà.ARKON);
            cartaPermessoCostruzione = new CartaPermessoCostruzione(new BonusAvanzaPercorsoNobiltà(2,NullBonus.getInstance()), cittàs);
            cittàs = new HashSet<>();
            cittàs.add(NomeCittà.CASTRUM);
            cartaPermessoCostruzione2 = new CartaPermessoCostruzione(new BonusAvanzaPercorsoNobiltà(2,NullBonus.getInstance()), cittàs);
            cittàs = new HashSet<>();
            cittàs.add(NomeCittà.BURGEN);
            cartaPermessoCostruzione3 = new CartaPermessoCostruzione(new BonusAvanzaPercorsoNobiltà(2,NullBonus.getInstance()), cittàs);
            cittàs = new HashSet<>();
            cittàs.add(NomeCittà.DORFUL);
            cartaPermessoCostruzione4 = new CartaPermessoCostruzione(new BonusAvanzaPercorsoNobiltà(2,NullBonus.getInstance()), cittàs);
            cittàs = new HashSet<>();
            cittàs.add(NomeCittà.ESTI);
            cartaPermessoCostruzione5 = new CartaPermessoCostruzione(new BonusAvanzaPercorsoNobiltà(2,NullBonus.getInstance()), cittàs);
            giocatore.addCarta(cartaPermessoCostruzione);
            giocatore.addCarta(cartaPermessoCostruzione2);
            giocatore.addCarta(cartaPermessoCostruzione3);
            giocatore.addCarta(cartaPermessoCostruzione4);
            giocatore.addCarta(cartaPermessoCostruzione5);
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
            giocatoreCorrente = Controller.class.getDeclaredField("giocatoreCorrente");
            giocatoreCorrente.setAccessible(true);
        } catch (NoSuchFieldException exc){
            exc.printStackTrace();
        } catch (IllegalAccessException exc){
            exc.printStackTrace();
        }
    }

    private HashSet<Città> creaCittàDaFile(Properties pro){
        try {
            Method method = AvviatorePartita.class.getDeclaredMethod("creaCittàDaFile", Properties.class);
            method.setAccessible(true);
            return (HashSet<Città>) method.invoke(avviatorePartita, pro);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exc) {
            exc.printStackTrace();
            return null;
        }
    }


    private HashSet<Città> cittàInRegioniDaFile(Properties pro, NomeRegione nomeRegione, HashSet<Città> tutteLeCittà){
        try {
            Method method = AvviatorePartita.class.getDeclaredMethod("cittàInRegioniDaFile", Properties.class, NomeRegione.class, HashSet.class);
            method.setAccessible(true);
            return (HashSet<Città>) method.invoke(avviatorePartita, pro, nomeRegione, tutteLeCittà);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    private void creaSentieriCittàDaFile(Properties pro, HashSet<Città> tutteLeCittà){
        try {
            Method method = AvviatorePartita.class.getDeclaredMethod("creaSentieriCittàDaFile", Properties.class, HashSet.class);
            method.setAccessible(true);
            method.invoke(avviatorePartita, pro, tutteLeCittà);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exc) {
            exc.printStackTrace();
        }
    }


    @Test
    public void eleggiConsigliereTest() throws RemoteException {
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
    public void acquistareTesseraPermessoCostruzione() throws RemoteException {
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
    public void costruireEmporioConTesseraPermessoCostruzioneTutteRegione() throws  RemoteException{
        try {
            Giocatore giocatore = (Giocatore) giocatoreCorrente.get(controller);
            resetGiocatore();
            assertTrue(controller.costruireEmporioConTesseraPermessoCostruzione(cartaPermessoCostruzione, NomeCittà.ARKON.toString()));
            resetGiocatore();
            assertTrue(controller.costruireEmporioConTesseraPermessoCostruzione(cartaPermessoCostruzione2, NomeCittà.CASTRUM.toString()));
            resetGiocatore();
            assertTrue(controller.costruireEmporioConTesseraPermessoCostruzione(cartaPermessoCostruzione3, NomeCittà.BURGEN.toString()));
            resetGiocatore();
            assertTrue(controller.costruireEmporioConTesseraPermessoCostruzione(cartaPermessoCostruzione4, NomeCittà.DORFUL.toString()));
            resetGiocatore();
            assertTrue(controller.costruireEmporioConTesseraPermessoCostruzione(cartaPermessoCostruzione5, NomeCittà.ESTI.toString()));
        } catch (IllegalAccessException exc){
            exc.printStackTrace();
        }
    }

    @Test
    public void costruireEmporioConAiutoRe() throws RemoteException{
        try {
            Giocatore giocatore = (Giocatore) giocatoreCorrente.get(controller);
            resetGiocatore();
            ArrayList<String> nomiColoriCartePolitica = new ArrayList<>();
            nomiColoriCartePolitica.add("ARANCIONE");
            nomiColoriCartePolitica.add("ARANCIONE");
            nomiColoriCartePolitica.add("JOLLY");
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.ARANCIONE));
            giocatore.addCarta(new CartaPolitica(ColoreCartaPolitica.JOLLY));
            assertTrue(controller.costruireEmporioConAiutoRe(nomiColoriCartePolitica,  NomeCittà.ESTI.toString()));
        } catch (IllegalAccessException exc){
            exc.printStackTrace();
        }
    }


    private void resetGiocatore(){
        try {
            //utilizzo la reflection
            azioniPrincipaliDisponibili.set(controller, 1);
            azioneVeloceEseguita.set(controller, false);
            Giocatore giocatore = (Giocatore) giocatoreCorrente.get(controller);
            giocatore.guadagnaMonete(5);
            giocatore.guadagnaAiutanti(5);
        } catch (IllegalAccessException exc){
            exc.printStackTrace();
        }
    }
}