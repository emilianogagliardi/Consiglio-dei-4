package client.view.GUI;

import classicondivise.VetrinaMarket;
import classicondivise.bonus.Bonus;
import classicondivise.carte.CartaPermessoCostruzione;
import client.view.GUI.customevent.ShowViewGiocoEvent;
import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaView;
import javafx.application.Platform;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/*
    SINGLETON
    GUIView riceve gli ordini da remoto, in socket o in RMI, e a sua volta impone l'update
    a controllerFXPartita, e l'esecuzione delle mosse a controllerFXMosse.
    Quindi estende il tipo GestoreFlussoFinestra.
 */
public class GUIView extends GestoreFlussoFinestra implements InterfacciaView, Remote {
    private static int idGiocatore;
    private static int idMappa;
    private static GUIView instance;
    private static ControllerFXMosse controllerFXMosse; //utilizzato per le mosse
    private static ControllerFXPartita controllerFXPartita; //utilizzato per le update

    private GUIView(FXApplication application) throws RemoteException {
        super.setApplication(application);
        UnicastRemoteObject.exportObject(this, 0);
    }


    protected static void initGUIView (FXApplication application) throws RemoteException {
        if (instance == null) {
            instance = new GUIView(application);
        }
    }

    protected static GUIView getInstance() throws SingletonNonInizializzatoException{
        if (instance == null) throw new SingletonNonInizializzatoException();
        return instance;
    }

    protected static void setControllerFXPartita(ControllerFXPartita controller) {
        controllerFXPartita = controller;
    }

    protected static void setIdMappa(int id){idMappa = id;}

    protected static int getIdMappa(){return idMappa;}

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
        super.showSceltaMappa();
    }

    @Override
    public void iniziaAGiocare(int idMappa) throws RemoteException {
        this.setIdMappa(idMappa);
        controllerFXPartita.getRootPane().fireEvent(new ShowViewGiocoEvent());
        super.showFinestraGioco();
    }

    @Override
    public void erroreDiConnessione() throws RemoteException {

    }

    @Override
    public void updatePuntiVittoriaGiocatore(int id, int punti) throws RemoteException{
        if(id == this.getIdGiocatore()) Platform.runLater(() -> controllerFXPartita.updatePuntiVittoriaGiocatore(punti));
        else Platform.runLater(() -> controllerFXPartita.updatePuntiVittoriaAvversario(id, punti));
    }

    @Override
    public void updateBalcone(String idBalcone, String colore1, String colore2, String colore3, String colore4) throws RemoteException{
        Platform.runLater(() -> controllerFXPartita.updateBalcone(idBalcone, colore1, colore2, colore3, colore4));
    }

    @Override
    public void updateMonete(int id, int monete) throws RemoteException {
        if(id==this.getIdGiocatore()) controllerFXPartita.updateMoneteGiocatore(monete);
        else Platform.runLater(() -> controllerFXPartita.updateMoneteAvversario(id, monete));
    }

    @Override
    public void updateCartePoliticaAvversari(int idGiocatore, int numCarte) throws RemoteException {
        Platform.runLater(() -> controllerFXPartita.updateCartePoliticaAvversari(idGiocatore, numCarte));
    }

    @Override
    public void updateCartePoliticaProprie(List<String> carte) throws RemoteException {
        Platform.runLater(() -> controllerFXPartita.updateCartePoliticaGiocatore(carte));
    }

    @Override
    public void updateCartePermessoRegione(String regione, CartaPermessoCostruzione c1, CartaPermessoCostruzione c2)  throws RemoteException{
        Platform.runLater(() -> controllerFXPartita.updateCartePermessoRegione(regione, c1, c2));
    }

    @Override
    public void updateCartePermessoGiocatore(int idGiocatore, List<CartaPermessoCostruzione> mano) throws RemoteException{
        Platform.runLater(() -> controllerFXPartita.updateCartePermessoGiocatore(idGiocatore, mano));
    }

    @Override
    public void updateAiutanti(int id, int numAiutanti) throws RemoteException{
        if(id == this.getIdGiocatore()) Platform.runLater(() -> controllerFXPartita.updateAiutantiGiocatore(numAiutanti));
        else Platform.runLater(() -> controllerFXPartita.updateAiutantiAvversari(id, numAiutanti));
    }

    @Override
    public void updateRiservaAiutanti(int numAiutanti) throws RemoteException{
        Platform.runLater(() -> controllerFXPartita.updateAiutantiGioco(numAiutanti));
    }

    @Override
    public void updateRiservaConsiglieri(List<String> coloriConsiglieri) throws RemoteException{
        Platform.runLater(() -> controllerFXPartita.updateConsiglieriGioco(coloriConsiglieri));
    }

    @Override
    public void updatePercorsoNobiltà(int idGiocatore, int posizione) throws RemoteException{
        Platform.runLater(() -> controllerFXPartita.updatePosizionePercorsoNobilta(idGiocatore, posizione));
    }

    @Override
    public void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori) throws RemoteException{
        Platform.runLater(() -> controllerFXPartita.updateEmporiCittà(nomeCittà, idGiocatori));
    }

    @Override
    public void updateBonusCittà(String nomeCittà, Bonus bonus) throws RemoteException {
        Platform.runLater(() -> controllerFXPartita.updateBonusCittà(nomeCittà, bonus));
    }


    @Override
    public void updateEmporiDisponibiliGiocatore(int idGiocatore, int num) throws RemoteException{
        if (idGiocatore == this.getIdGiocatore()) Platform.runLater(() ->controllerFXPartita.updateEmporiGiocatore(num));
        else Platform.runLater(() -> controllerFXPartita.updateEmporiAvversario(idGiocatore, num));
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

    @Override
    public void mostraMessaggio(String messaggio) {
        Platform.runLater(() -> controllerFXPartita.nuovoMessaggio(messaggio));
    }

    @Override
    public void updateBonusPercorsoNobiltà(List<Bonus> percorso) throws RemoteException {
        Platform.runLater(() -> controllerFXPartita.updateBonusNobilta(percorso));
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
}
