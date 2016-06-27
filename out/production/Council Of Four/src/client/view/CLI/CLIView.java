package client.view.CLI;


import classicondivise.VetrinaMarket;
import classicondivise.bonus.Bonus;
import classicondivise.carte.Carta;
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
import java.util.*;

class CLIView implements InterfacciaView, Remote {
    private String connectionType;
    private InterfacciaLoggerRMI loggerRMI;
    private ObjectOutputStream oos;
    private Scanner in;
    private boolean fineTurno;
    private int idGiocatore;
    private EseguiTurno istanza;
    private InterfacciaController controller;
    private HashMap<Integer, Integer> mappaPuntiVittoria;
    private HashMap<String, List<String>> mappaBalconi;
    private HashMap<Integer, Integer> mappaMonete;
    private HashMap<Integer, Integer> mappaCartePoliticaAvversari;
    private List<String> manoCartePolitica;
    private HashMap<String, List<CartaPermessoCostruzione>> mappaCartePermessoRegione;
    private HashMap<Integer, List<CartaPermessoCostruzione>> mappaCartePermessoCostruzioneGiocatori;
    private HashMap<Integer, Integer> mappaAiutantiGiocatori;
    private int riservaAiutanti;
    private List<String> riservaConsiglieri;
    private HashMap<Integer, Integer> mappaPosizioniPercorsoNobiltà;
    private HashMap<String, List<Integer>> mappaEmporiCittà;
    private HashMap<String, Bonus> mappaBonusCittà;
    private HashMap<Integer, Integer> mappaEmporiDisponibiliGiocatori;
    private String posizioneRe;
    private List<Bonus> percorsoNobiltà;

    CLIView(String connectionType){
        try {
            in = new Scanner(System.in);
            this.connectionType = connectionType;
            fineTurno = true;
            UnicastRemoteObject.exportObject(this, 0);
            mappaPuntiVittoria = new HashMap<>();
            mappaBalconi = new HashMap<>();
            mappaMonete = new HashMap<>();
            mappaCartePoliticaAvversari = new HashMap<>();
            manoCartePolitica = new ArrayList<>();
            mappaCartePermessoRegione = new HashMap<>();
            mappaCartePermessoCostruzioneGiocatori = new HashMap<>();
            mappaAiutantiGiocatori = new HashMap<>();
            riservaConsiglieri = new ArrayList<>();
            mappaPosizioniPercorsoNobiltà = new HashMap<>();
            mappaEmporiCittà = new HashMap<>();
            mappaBonusCittà = new HashMap<>();
            mappaEmporiDisponibiliGiocatori = new HashMap<>();
            percorsoNobiltà = new ArrayList<>();
        } catch (RemoteException exc) {
            exc.printStackTrace();
        }
    }

    public HashMap<String, List<String>> getMappaBalconi() {
        return mappaBalconi;
    }

    public HashMap<Integer, Integer> getMappaMonete() {
        return mappaMonete;
    }

    public HashMap<Integer, Integer> getMappaCartePoliticaAvversari() {
        return mappaCartePoliticaAvversari;
    }

    public List<String> getManoCartePolitica() {
        return manoCartePolitica;
    }

    public HashMap<String, List<CartaPermessoCostruzione>> getMappaCartePermessoRegione() {
        return mappaCartePermessoRegione;
    }

    public HashMap<Integer, List<CartaPermessoCostruzione>> getMappaCartePermessoCostruzioneGiocatori() {
        return mappaCartePermessoCostruzioneGiocatori;
    }

    public HashMap<Integer, Integer> getMappaAiutantiGiocatori() {
        return mappaAiutantiGiocatori;
    }

    public int getRiservaAiutanti() {
        return riservaAiutanti;
    }

    public List<String> getRiservaConsiglieri() {
        return riservaConsiglieri;
    }

    public HashMap<Integer, Integer> getMappaPosizioniPercorsoNobiltà() {
        return mappaPosizioniPercorsoNobiltà;
    }

    public HashMap<String, List<Integer>> getMappaEmporiCittà() {
        return mappaEmporiCittà;
    }

    public HashMap<String, Bonus> getMappaBonusCittà() {
        return mappaBonusCittà;
    }

    public HashMap<Integer, Integer> getMappaEmporiDisponibiliGiocatori() {
        return mappaEmporiDisponibiliGiocatori;
    }

    public String getPosizioneRe() {
        return posizioneRe;
    }

    public List<Bonus> getPercorsoNobiltà() {
        return percorsoNobiltà;
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
        mappaPuntiVittoria.put(idGiocatore, punti);
    }

    public HashMap<Integer, Integer> getMappaPuntiVittoria() {
        return mappaPuntiVittoria;
    }

    @Override
    public void updateBalcone(String idBalcone, String colore1, String colore2, String colore3, String colore4) throws RemoteException {
        ArrayList<String> lista = new ArrayList<>();
        lista.add(colore1);
        lista.add(colore2);
        lista.add(colore3);
        lista.add(colore4);
        mappaBalconi.put(idBalcone, lista);
    }

    @Override
    public void updateMonete(int idGiocatore, int monete) throws RemoteException {
        mappaMonete.put(idGiocatore, monete);
        if (idGiocatore == this.idGiocatore) {
            System.out.println("Monete: " + monete);
        } else {
            System.out.println("Monete giocatore " + idGiocatore + ": " + monete);
        }
    }

    @Override
    public void updateCartePoliticaAvversari(int idGiocatore, int numCarte) throws RemoteException {
        mappaCartePoliticaAvversari.put(idGiocatore, numCarte);
    }

    @Override
    public void updateCartePoliticaProprie(List<String> carte) throws RemoteException {
        manoCartePolitica.clear();
        manoCartePolitica.addAll(carte);
    }

    @Override
    public void updateCartePermessoRegione(String regione, CartaPermessoCostruzione c1, CartaPermessoCostruzione c2) throws RemoteException {
        ArrayList<CartaPermessoCostruzione> lista = new ArrayList<>();
        lista.add(c1);
        lista.add(c2);
        mappaCartePermessoRegione.put(regione, lista);
    }

    @Override
    public void updateCartePermessoGiocatore(int idGiocatore, List<CartaPermessoCostruzione> manoCartePermessoCostruzione) throws RemoteException {
        mappaCartePermessoCostruzioneGiocatori.put(idGiocatore, manoCartePermessoCostruzione);
    }

    @Override
    public void updateAiutanti(int idGiocatore, int numAiutanti) throws RemoteException {
        mappaAiutantiGiocatori.put(idGiocatore, numAiutanti);
        if (idGiocatore == this.idGiocatore) {
            System.out.println("Aiutanti: " + numAiutanti);
        } else {
            System.out.println("Aiutanti giocatore " + idGiocatore + ": " + numAiutanti);
        }
    }

    @Override
    public void updateRiservaAiutanti(int numAiutanti) throws RemoteException {
        riservaAiutanti = numAiutanti;
    }

    @Override
    public void updateRiservaConsiglieri(List<String> coloriConsiglieri) throws RemoteException {
        riservaConsiglieri = coloriConsiglieri;
    }

    @Override
    public void updatePercorsoNobiltà(int idGiocatore, int posizione) throws RemoteException {
        mappaPosizioniPercorsoNobiltà.put(idGiocatore, posizione);
    }

    @Override
    public void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori) throws RemoteException {
        mappaEmporiCittà.put(nomeCittà, idGiocatori);
    }

    @Override
    public void updateBonusCittà(String nomeCittà, Bonus bonus) throws RemoteException {
        mappaBonusCittà.put(nomeCittà, bonus);
    }

    @Override
    public void updateEmporiDisponibiliGiocatore(int idGiocatore, int numeroEmporiDisponibili) throws RemoteException {
        mappaEmporiDisponibiliGiocatori.put(idGiocatore, numeroEmporiDisponibili);
    }

    @Override
    public void updatePosizioneRe(String nomeCittà) throws RemoteException {
        posizioneRe = nomeCittà;
    }

    @Override
    public void eseguiTurno() throws RemoteException {
        istanza = EseguiTurno.getIstanza();
        istanza.setController(controller);
        istanza.setCLIView(this);
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
        this.percorsoNobiltà = percorsoNobiltà;
    }

}
