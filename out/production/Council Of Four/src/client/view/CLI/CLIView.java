package client.view.CLI;


import classicondivise.VetrinaMarket;
import classicondivise.bonus.Bonus;
import classicondivise.carte.CartaPermessoCostruzione;
import client.ComunicazioneSceltaMappa;
import client.ComunicazioneSceltaMappaRMI;
import client.ComunicazioneSceltaMappaSocket;
import client.view.CostantiClient;
import client.view.SocketProxyController;
import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaController;
import interfaccecondivise.InterfacciaLoggerRMI;
import interfaccecondivise.InterfacciaView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

class CLIView implements InterfacciaView, Remote {
    private String connectionType;
    private InterfacciaLoggerRMI loggerRMI;
    private ObjectOutputStream oos;
    private Scanner in;
    private boolean fineTurno;
    private int idGiocatore;
    private EseguiTurno istanza;
    private InterfacciaController controller;

    CLIView(String connectionType){
        try {
            in = new Scanner(System.in);
            this.connectionType = connectionType;
            fineTurno = true;
            UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void setIdGiocatore(int idGiocatore) throws RemoteException {
        this.idGiocatore = idGiocatore;
    }

    @Override
    public int getIdGiocatore() throws RemoteException {
        return idGiocatore;
    }

    void setLoggerRMI(InterfacciaLoggerRMI loggerRMI) {
        this.loggerRMI = loggerRMI;
    }

    void setObjectStream(ObjectOutputStream oos) {
        this.oos = oos;
    }

    @Override
    public void scegliMappa() throws RemoteException {
        new Thread(new ScegliMappa(connectionType, loggerRMI, oos)).start();
    }

    @Override
    public void iniziaAGiocare(int idMappa) throws RemoteException {
        if (connectionType.equals("S")) {
            try {
                controller = new SocketProxyController(oos);
            } catch (IOException exc){
                exc.printStackTrace();
            }
        } else if (connectionType.equals("R")) {
            try {
                Registry registry = LocateRegistry.getRegistry(CostantiClient.IP_SERVER, CostantiClient.REGISTRY_PORT);
                controller = (InterfacciaController) registry.lookup(loggerRMI.getChiaveController());
            } catch (RemoteException | NotBoundException exc) {
                exc.printStackTrace();
            }
        }
    }

    @Override
    public void erroreDiConnessione() throws RemoteException {

    }

    @Override
    public void updatePuntiVittoriaGiocatore(int idGiocatore, int punti) throws RemoteException {

    }

    @Override
    public void updateBalcone(String idBalcone, String colore1, String colore2, String colore3, String colore4) throws RemoteException {

    }

    @Override
    public void updateMonete(int idGiocatore, int monete) throws RemoteException {

    }

    @Override
    public void updateCartePoliticaAvversari(int idGiocatore, int numCarte) throws RemoteException {

    }

    @Override
    public void updateCartePoliticaProprie(List<String> carte) throws RemoteException {

    }

    @Override
    public void updateCartePermessoRegione(String regione, CartaPermessoCostruzione c1, CartaPermessoCostruzione c2) throws RemoteException {

    }

    @Override
    public void updateCartePermessoGiocatore(int idGiocatore, List<CartaPermessoCostruzione> manoCartePermessoCostruzione) throws RemoteException {

    }

    @Override
    public void updateAiutanti(int idGiocatore, int numAiutanti) throws RemoteException {

    }

    @Override
    public void updateRiservaAiutanti(int numAiutanti) throws RemoteException {

    }

    @Override
    public void updateRiservaConsiglieri(List<String> coloriConsiglieri) throws RemoteException {

    }

    @Override
    public void updatePercorsoNobiltà(int idGiocatore, int posizione) throws RemoteException {

    }

    @Override
    public void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori) throws RemoteException {

    }

    @Override
    public void updateBonusCittà(String nomeCittà, Bonus bonus) throws RemoteException {

    }

    @Override
    public void updateEmporiDisponibiliGiocatore(int idGiocatore, int numeroEmporiDisponibili) throws RemoteException {

    }

    @Override
    public void updatePosizioneRe(String nomeCittà) throws RemoteException {

    }

    @Override
    public void eseguiTurno() throws RemoteException {
        istanza = EseguiTurno.getIstanza();
        istanza.setController(controller);
        new Thread(istanza).start();
    }

    @Override
    public void fineTurno() throws RemoteException {
        istanza.stop();
    }

    @Override
    public void mostraMessaggio(String messaggio) throws RemoteException {
        System.out.println(messaggio);
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

    @Override
    public void updateBonusPercorsoNobiltà(List<Bonus> percorsoNobiltà) throws RemoteException {

    }

}
