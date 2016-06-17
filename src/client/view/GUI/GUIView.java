package client.view.GUI;

import client.view.GUI.controllerFX.ControllerFXMosse;
import client.view.GUI.controllerFX.ControllerFXPartita;
import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaView;
import javafx.application.Platform;
import server.model.carte.CartaPermessoCostruzione;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

/*
    SINGLETON
    GUIView riceve gli ordini da remoto, in socket o in RMI, e a sua volta impone l'update
    a controllerFXPartita, e l'esecuzione delle mosse a controllerFXMosse.
    Quindi estende il tipo GestoreFlussoFinestra.
 */
public class GUIView extends GestoreFlussoFinestra implements InterfacciaView, Remote {
    private int idGiocatore;
    private static GUIView instance;
    private ControllerFXMosse controllerFXMosse; //utilizzato per le mosse
    private ControllerFXPartita controllerFXPartita; //utilizzato per le update

    private GUIView(ControllerFXMosse controllerFXMosse, ControllerFXPartita controllerFXPartita, FXApplication flusso) throws RemoteException {
        super.setApplication(flusso);
        this.controllerFXMosse = controllerFXMosse;
        this.controllerFXPartita = controllerFXPartita;
        UnicastRemoteObject.exportObject(this, 0);
    }

    public static void initGUIView (ControllerFXMosse controllerFXMosse, ControllerFXPartita controllerFXPartita, FXApplication application) throws RemoteException {
        if (instance == null) {
            instance = new GUIView(controllerFXMosse, controllerFXPartita, application);
        }
    }

    public static GUIView getInstance() throws SingletonNonInizializzatoException{
        if (instance == null) throw new SingletonNonInizializzatoException();
        return instance;
    }

    @Override
    public void setIdGiocatore(int idGiocatore) throws RemoteException{
        this.idGiocatore = idGiocatore;
    }

    @Override
    public int getIdGiocatore() throws RemoteException {
        return idGiocatore;
    }

    @Override
    public void scegliMappa()throws RemoteException {
        Platform.runLater(() -> super.setNuovoStep("mappegallery.fxml"));
    }

    @Override
    public void erroreDiConnessione() throws RemoteException {

    }

    @Override
    public void updatePuntiVittoriaGiocatore(int idGiocatore, int punti) throws RemoteException{

    }

    @Override
    public void updateBalcone(String regione, String colore1, String colore2, String colore3, String colore4) throws RemoteException{

    }

    @Override
    public void updateMonete(int idGiocatore, int idMonete) throws RemoteException {

    }

    @Override
    public void updateCartePoliticaAvversari(int idGiocatore, int numCarte) throws RemoteException {

    }

    @Override
    public void updateCartePoliticaProprie(List<String> carte) throws RemoteException {

    }

    @Override
    public void updateCartePermessoRegione(String regione, CartaPermessoCostruzione c1, CartaPermessoCostruzione c2)  throws RemoteException{

    }

    @Override
    public void updateCartePermessoGiocatore(int idGiocatore, List<CartaPermessoCostruzione> mano) throws RemoteException{

    }

    @Override
    public void updateAiutanti(int idGiocatore, int numAiutanti) throws RemoteException{

    }

    @Override
    public void updateRiservaAiutanti(int numAiutanti) throws RemoteException{

    }

    @Override
    public void updateRiservaConsiglieri(List<String> coloriConsiglieri) throws RemoteException{

    }

    @Override
    public void updatePercorsoNobiltà(int idGiocatore, int posizione) throws RemoteException{

    }

    @Override
    public void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori) throws RemoteException{

    }

    @Override
    public void updateEmporiDisponibiliGiocatore(int idGiocatore, int num) throws RemoteException{

    }

    @Override
    public void updateCarteBonusColoreCittàGiocatore(int idGiocatore, HashMap<String, Integer> mapCarte) throws RemoteException{

    }

    @Override
    public void updateCarteBonusColoreCittàTabellone(HashMap<String, Integer> coloriEPunti) throws RemoteException{

    }

    @Override
    public void updateCarteBonusReGiocatore(int idGiocatore, HashMap<String, Integer> mapCarte) throws RemoteException{

    }

    @Override
    public void updateCarteBonusReTabellone(int puntiVIttoriaPrimaCarta) throws RemoteException{

    }

    @Override
    public void updateCarteBonusRegioneGiocatore(int idGiocatore, HashMap<String, Integer> carte) throws RemoteException {

    }

    @Override
    public void updateCarteBonusRegioneTabellone(String nomeRegione, int puntiCarta) throws RemoteException {

    }

    @Override
    public void updatePosizioneRe(String città) throws RemoteException {

    }

    @Override
    public void eseguiTurno() throws RemoteException {

    }

    @Override
    public void fineTurno() throws RemoteException {

    }
}
