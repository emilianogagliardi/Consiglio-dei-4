package client.view.GUI;

import client.view.GUI.controllerFX.ControllerFXMosse;
import client.view.GUI.controllerFX.ControllerFXPartita;
import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaView;
import server.model.carte.CartaPermessoCostruzione;

import java.util.HashMap;
import java.util.List;

/*
    SINGLETON
    GUIView riceve gli ordini da remoto, in socket o in RMI, e a sua volta impone l'update
    a controllerFXPartita, e l'esecuzione delle mosse a controllerFXMosse.
    Quindi estende il tipo GestoreFlussoFinestra.
 */
public class GUIView extends GestoreFlussoFinestra implements InterfacciaView {
    private int idGiocatore;
    private static GUIView instance;
    private ControllerFXMosse controllerFXMosse; //utilizzato per le mosse
    private ControllerFXPartita controllerFXPartita; //utilizzato per le update

    private GUIView(ControllerFXMosse controllerFXMosse, ControllerFXPartita controllerFXPartita, FXApplication flusso){
        super.setApplication(flusso);
        this.controllerFXMosse = controllerFXMosse;
        this.controllerFXPartita = controllerFXPartita;
    }

    public static void initGUIView (ControllerFXMosse controllerFXMosse, ControllerFXPartita controllerFXPartita, FXApplication flusso){
        if (instance == null) {
            instance = new GUIView(controllerFXMosse, controllerFXPartita, flusso);
        }
    }

    public static GUIView getInstance() throws SingletonNonInizializzatoException{
        if (instance == null) throw new SingletonNonInizializzatoException();
        return instance;
    }

    @Override
    public void setIdGiocatore(int idGiocatore) {
        this.idGiocatore = idGiocatore;
    }

    @Override
    public int getIdGiocatore() {
        return idGiocatore;
    }

    @Override
    public void scegliMappa() {
        super.setNuovoStep("mappegallery.fxml");
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
}
