import interfaccecondivise.InterfacciaView;
import org.junit.Before;
import org.junit.Test;
import server.model.*;
import server.model.bonus.NullBonus;
import server.model.carte.CartaPermessoCostruzione;
import server.sistema.AvviatorePartita;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.*;

import static org.junit.Assert.assertEquals;


public class AvviatorePartitaTest {
    AvviatorePartita avviatorePartita;
    ArrayList<InterfacciaView> proxyViews;
    @Before
    public void setUp(){ //inizializza una interfaccia view fasulla
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
            public void iniziaAGiocare() throws RemoteException {

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
        avviatorePartita = new AvviatorePartita(proxyViews, 0);
    }

    @Test
    public void creaPartita() throws IOException {
        Partita partita;
        try {
            Method method = AvviatorePartita.class.getDeclaredMethod("creaPartita", Properties.class);
            method.setAccessible(true);
            try{
                AvviatorePartita avviatorePartita = new AvviatorePartita(proxyViews, 0);
                Properties pro = new Properties();
                pro.load(new FileInputStream("./serverResources/fileconfigmappe/mappa1"));
                partita = (Partita) method.invoke(avviatorePartita, pro);
            } catch (IllegalAccessException exc){
                exc.printStackTrace();
            } catch (InvocationTargetException exc){
                exc.printStackTrace();
            }
        } catch (NoSuchMethodException exc) {
            exc.printStackTrace();
        }
       partita = null;
    }

    @Test
    public void creaCittàDaFileTest() throws IOException {
        //il test è basato sul contenuto del file mappa1
        Method method = null;
        try {
            method = AvviatorePartita.class.getDeclaredMethod("creaCittàDaFile", Properties.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            Properties pro = new Properties();
            pro.load(new FileInputStream("./serverResources/fileconfigmappe/mappa1"));
            HashSet<Città> cittàDaAlgoritmo = (HashSet<Città>) method.invoke(avviatorePartita, pro);
            ArrayList<Città> cittàHardCoded = new ArrayList<>();
            //istanze di città solo ugiali se hanno lo stesso nome, nome regione, colore
            cittàHardCoded.add(new Città(NomeRegione.COSTA, NomeCittà.ARKON, ColoreCittà.FERRO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.COSTA, NomeCittà.CASTRUM, ColoreCittà.ARGENTO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.COSTA, NomeCittà.BURGEN, ColoreCittà.ORO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.COSTA, NomeCittà.DORFUL, ColoreCittà.ARGENTO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.COSTA, NomeCittà.ESTI, ColoreCittà.BRONZO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.COLLINA, NomeCittà.FRAMEK, ColoreCittà.ORO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.COLLINA, NomeCittà.INDUR, ColoreCittà.BRONZO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.COLLINA, NomeCittà.GRADEN, ColoreCittà.ARGENTO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.COLLINA, NomeCittà.JUVELAR, ColoreCittà.CITTA_RE, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.COLLINA, NomeCittà.HELLAR, ColoreCittà.ORO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.MONTAGNA, NomeCittà.KULTOS, ColoreCittà.ORO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.MONTAGNA, NomeCittà.NARIS, ColoreCittà.BRONZO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.MONTAGNA, NomeCittà.LYRAM, ColoreCittà.FERRO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.MONTAGNA, NomeCittà.OSIUM, ColoreCittà.ORO, NullBonus.getInstance(), proxyViews));
            cittàHardCoded.add(new Città(NomeRegione.MONTAGNA, NomeCittà.MERKATIM, ColoreCittà.ARGENTO, NullBonus.getInstance(), proxyViews));
            ArrayList<Città> arrayCittàAlgoritmo = new ArrayList<>();
            cittàDaAlgoritmo.stream().forEach(arrayCittàAlgoritmo::add);
            Comparator cittàComparator = (c1, c2) -> c1.toString().compareTo(c2.toString());
            arrayCittàAlgoritmo.sort(cittàComparator);
            cittàHardCoded.sort(cittàComparator);
            assertEquals(arrayCittàAlgoritmo, cittàHardCoded);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void creaSentieriCittàDaFileTest() throws IOException{
        //il test è basato sul contenuto della mappa1
        Method methodOttieniCittà = null;
        Method methodCreaSentieri = null;
        try {
            methodOttieniCittà = AvviatorePartita.class.getDeclaredMethod("creaCittàDaFile", Properties.class);
            methodCreaSentieri = AvviatorePartita.class.getDeclaredMethod("creaSentieriCittàDaFile", Properties.class, HashSet.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        methodOttieniCittà.setAccessible(true);
        methodCreaSentieri.setAccessible(true);
        try{
            Properties pro = new Properties();
            pro.load(new FileInputStream("./serverResources/fileconfigmappe/mappa1"));
            HashSet<Città> cittàDaAlgoritmo = (HashSet<Città>) methodOttieniCittà.invoke(avviatorePartita, pro);
            //modifica le città in cittàDaAlgoritmo, assegnando le città adiacenti
            methodCreaSentieri.invoke(avviatorePartita, pro, cittàDaAlgoritmo);
            cittàDaAlgoritmo.forEach(cittàCorrente -> {
                //crea un arrayList che contiene i nomi delle città adiacenti alla città corrente e lo ordina alfabeticamente
                ArrayList<NomeCittà> nomiCittàAdiacentiCittàCorrente = new ArrayList<>();
                cittàCorrente.getCittàAdiacenti().forEach(città -> nomiCittàAdiacentiCittàCorrente.add(città.getNome()));
                //crea un arrayList che contiene i nomi delle città adiacenti alla città corrente, come nel file di configurazione mappa1 e lo ordina alfabeticamente
                ArrayList<NomeCittà> nomiCittàAdiacentiHardCoded = new ArrayList<>();
                switch (cittàCorrente.getNome()){
                    case ARKON:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.CASTRUM);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.BURGEN);
                        break;
                    case BURGEN:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.ARKON);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.DORFUL);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.ESTI);
                        break;
                    case CASTRUM:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.ARKON);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.FRAMEK);
                        break;
                    case DORFUL:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.BURGEN);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.GRADEN);
                        break;
                    case ESTI:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.BURGEN);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.HELLAR);
                        break;
                    case FRAMEK:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.CASTRUM);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.INDUR);
                        break;
                    case INDUR:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.FRAMEK);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.JUVELAR);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.KULTOS);
                        break;
                    case GRADEN:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.DORFUL);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.JUVELAR);
                        break;
                    case HELLAR:
                    nomiCittàAdiacentiHardCoded.add(NomeCittà.JUVELAR);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.ESTI);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.MERKATIM);
                        break;
                    case JUVELAR:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.INDUR);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.GRADEN);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.LYRAM);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.HELLAR);
                        break;
                    case KULTOS:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.INDUR);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.LYRAM);
                        break;
                    case NARIS:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.LYRAM);
                        break;
                    case LYRAM:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.NARIS);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.KULTOS);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.JUVELAR);
                        break;
                    case OSIUM:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.MERKATIM);
                        break;
                    case MERKATIM:
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.OSIUM);
                        nomiCittàAdiacentiHardCoded.add(NomeCittà.HELLAR);
                        break;
                }
                Comparator comparator = (n1, n2) -> n1.toString().compareTo(n2.toString());
                nomiCittàAdiacentiHardCoded.sort(comparator);
                nomiCittàAdiacentiCittàCorrente.sort(comparator);
                //verifica che i nomi città adiacenti scritti hardCoded sono equivalenti a quelli scritti dall'algoritmo che crea i sentieri
                assertEquals(nomiCittàAdiacentiCittàCorrente, nomiCittàAdiacentiHardCoded);
            });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cittàInRegioniDaFileTest() throws IOException {
        //il test è basato sul contenuto del file mappa1
        Method methodCreaCittàDaFile = null;
        Method methodCittàInRegioniDaFile = null;
        try {
            methodCreaCittàDaFile = AvviatorePartita.class.getDeclaredMethod("creaCittàDaFile", Properties.class);
            methodCittàInRegioniDaFile = AvviatorePartita.class.getDeclaredMethod("cittàInRegioniDaFile", Properties.class, NomeRegione.class, HashSet.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        methodCittàInRegioniDaFile.setAccessible(true);
        methodCreaCittàDaFile.setAccessible(true);
        Properties pro = new Properties();
        pro.load(new FileInputStream("./serverResources/fileconfigmappe/mappa1"));
        try {
            HashSet<Città> tutteLeCittà = (HashSet<Città>) methodCreaCittàDaFile.invoke(avviatorePartita, pro);
            //assegnamento delle città alla regione costa
            HashSet<Città> cittàInCosta = (HashSet<Città>) methodCittàInRegioniDaFile.invoke(avviatorePartita, pro, NomeRegione.COSTA, tutteLeCittà);
            /*
            creazione di un arrayList di NomiCittà e inserimento dei NomiCittà delle città
            appartementi alla regione costa hardcoded, prese da file mappa1.
             */
            ArrayList<NomeCittà> cittàArrayListHardCoded = new ArrayList<>();
            cittàArrayListHardCoded.add(NomeCittà.ARKON);
            cittàArrayListHardCoded.add(NomeCittà.CASTRUM);
            cittàArrayListHardCoded.add(NomeCittà.BURGEN);
            cittàArrayListHardCoded.add(NomeCittà.DORFUL);
            cittàArrayListHardCoded.add(NomeCittà.ESTI);
            /*
            crea un arrayList contenente i nomi delle città ottenute tramite l'algoritmo di
            cittàInRegione da file, per poi confrontarlo con quello costruito hardcoded
             */
            ArrayList<NomeCittà> cittàArrayList = new ArrayList<>();
            cittàInCosta.forEach(città -> cittàArrayList.add(città.getNome()));
            Comparator comparator = (n1, n2) -> n1.toString().compareTo(n2.toString());
            cittàArrayList.sort(comparator);
            cittàArrayListHardCoded.sort(comparator);
            assertEquals(cittàArrayList, cittàArrayListHardCoded);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
