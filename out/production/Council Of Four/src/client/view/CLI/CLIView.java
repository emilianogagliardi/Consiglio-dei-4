package client.view.CLI;


import classicondivise.VetrinaMarket;
import classicondivise.bonus.Bonus;
import classicondivise.carte.CartaPermessoCostruzione;
import client.ComunicazioneSceltaMappa;
import client.ComunicazioneSceltaMappaRMI;
import client.ComunicazioneSceltaMappaSocket;
import client.view.SocketProxyController;
import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaController;
import interfaccecondivise.InterfacciaLoggerRMI;
import interfaccecondivise.InterfacciaView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

class CLIView implements InterfacciaView, Remote {
    String connectionType;
    ComunicazioneSceltaMappa setterMappa;
    InterfacciaLoggerRMI loggerRMI;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Scanner in;
    boolean fineTurno;
    int idGiocatore;
    EseguiTurno istanza;
    InterfacciaController controller;

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

    void setObjectStream(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
    }

    void initController(){
        //TODO: Se socket fai una cosa, se RMI fanne un'altra
        try {
            controller = new SocketProxyController(oos);
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void scegliMappa() throws RemoteException {
        try {
            if (connectionType.equals("R")) {
                ComunicazioneSceltaMappaRMI.init(loggerRMI.getChiaveSceltaMappa());
                setterMappa = ComunicazioneSceltaMappaRMI.getInstance();
                System.out.println("Inserisci il numero di mappa che vuoi utilizzare");
                int id = in.nextInt();
                setterMappa.comunicaSceltaMappa(id);
            } else if (connectionType.equals("S")) {
                ComunicazioneSceltaMappaSocket.init(oos);
                setterMappa = ComunicazioneSceltaMappaSocket.getInstance();
                System.out.println("Inserisci il numero di mappa che vuoi utilizzare");
                int id = in.nextInt();
                setterMappa.comunicaSceltaMappa(id);

            }
        } catch (SingletonNonInizializzatoException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void iniziaAGiocare(int idMappa) throws RemoteException {

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
        istanza.setSocketProxyController(controller);
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
