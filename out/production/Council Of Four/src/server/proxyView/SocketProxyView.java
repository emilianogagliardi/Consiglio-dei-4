package server.proxyView;

import classicondivise.ComunicazioneView;
import interfaccecondivise.InterfacciaView;
import server.model.carte.CartaPermessoCostruzione;
import server.sistema.AvviatorePartita;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

;

public class SocketProxyView implements InterfacciaView {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private AvviatorePartita avviatorePartita;
    private int idGiocatore;

    public SocketProxyView(Socket socket){
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void setIdGiocatore(int idGiocatore) {
        try{
            oos.writeObject(ComunicazioneView.SET_ID_GIOCATORE.toString());
            oos.flush();
            oos.writeInt(idGiocatore);
            oos.flush();
            this.idGiocatore = idGiocatore;
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public int getIdGiocatore(){
        return idGiocatore;
    }

    public void setAvviatore(AvviatorePartita avviatorePartita) {
        this.avviatorePartita = avviatorePartita;
    }

    @Override
    public void scegliMappa() {
        try{
            oos.writeObject(ComunicazioneView.SCEGLI_MAPPA.toString());
            oos.flush();
            avviatorePartita.setMappa(ois.readInt());
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void iniziaAGiocare(int idMappa) throws RemoteException {
        try {
            oos.writeObject(ComunicazioneView.INIZIA_A_GIOCARE.toString());
            oos.flush();
            //comunica l'id della mappa che è stata scelta
            oos.writeInt(idMappa);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
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

    @Override
    public void fineTurno() throws RemoteException {

    }

    @Override
    public void mostraMessaggio(String messaggio) {

    }
}
