import controller.Controller;
import model.Partita;
import model.carte.CartaPermessoCostruzione;
import org.junit.Test;
import proxyView.InterfacciaView;
import server.AvviatorePartita;
import server.ThreadTimeout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class AvviatorePartitaTest {
    ArrayList<InterfacciaView> proxyViews;
    private ExecutorService executors = Executors.newCachedThreadPool();
    Controller controller;
    Partita partita;

    public AvviatorePartitaTest(){
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
        partita = creaPartita(proxyViews);
        controller = new Controller(partita, proxyViews);
        executors.submit(controller);
    }

    @Test
    public void controllerTest(){

    }

    private Partita creaPartita(ArrayList<InterfacciaView> proxyViews){
        try {
            Method method = AvviatorePartita.class.getDeclaredMethod("creaPartita", null);
            method.setAccessible(true);
            try{
                AvviatorePartita avviatorePartita = new AvviatorePartita(proxyViews);
                return (Partita) method.invoke(avviatorePartita, null);
            } catch (IllegalAccessException exc){
                exc.printStackTrace();
            } catch (InvocationTargetException exc){
                exc.printStackTrace();
            }
        } catch (NoSuchMethodException exc) {
            exc.printStackTrace();
        }
        return null;
    }
}