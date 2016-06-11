import model.Città;
import model.carte.CartaPermessoCostruzione;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import proxyView.InterfacciaView;
import server.AvviatorePartita;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by emilianogagliardi on 11/06/16.
 */
public class EMI_AvviatorePartitaTest {
    AvviatorePartita avviatorePartita;

    @Before
    public void setUp(){ //inizializza una interfaccia view fasulla
        ArrayList<InterfacciaView> proxyViews = new ArrayList<>();
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
        avviatorePartita = new AvviatorePartita(proxyViews);
    }

    /*
        questo metodo contiene una assert pur non essendo marcato come Test, perchè chiamato dai metodi
        che utilizzano il file di configurazione ritornato
     */
    public InputStream sceltaMappa(){
        Method method = null;
        try {
            method = AvviatorePartita.class.getDeclaredMethod("sceltaMappa");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
            method.setAccessible(true);
        try {
            InputStream is = (InputStream) method.invoke(avviatorePartita);
            assertNotNull(is);
            return is;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testCreaCittàDaFile() {
        //il test è basato sul contenuto del file mappa1
        InputStream is = sceltaMappa();
        Method method = null;
        try {
            method = AvviatorePartita.class.getDeclaredMethod("creaCittàDaFile", InputStream.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            HashSet<Città> tutteLeCittà = (HashSet<Città>) method.invoke(avviatorePartita, is);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
