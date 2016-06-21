package server.proxyView;

import classicondivise.ComunicazioneView;
import classicondivise.VetrinaMarket;
import classicondivise.bonus.Bonus;
import interfaccecondivise.InterfacciaView;
import classicondivise.carte.CartaPermessoCostruzione;
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
        try {
            oos.writeObject(ComunicazioneView.UPDATE_PUNTI_VITTORIA_GIOCATORE.toString());
            oos.flush();
            oos.writeInt(idGiocatore);
            oos.flush();
            oos.writeInt(punti);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateBalcone(String idBalcone, String colore1, String colore2, String colore3, String colore4) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_BALCONE.toString());
            oos.flush();
            oos.writeObject(idBalcone);
            oos.flush();
            oos.writeObject(colore1);
            oos.flush();
            oos.writeObject(colore2);
            oos.flush();
            oos.writeObject(colore3);
            oos.flush();
            oos.writeObject(colore4);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateMonete(int idGiocatore, int monete) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_MONETE.toString());
            oos.flush();
            oos.writeInt(idGiocatore);
            oos.flush();
            oos.writeInt(monete);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateCartePoliticaAvversari(int idGiocatore, int numCarte) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_CARTE_POLITICA_AVVERSARI.toString());
            oos.flush();
            oos.writeInt(idGiocatore);
            oos.flush();
            oos.writeInt(numCarte);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateCartePoliticaProprie(List<String> carte) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_CARTE_POLITICA_PROPRIE.toString());
            oos.flush();
            oos.writeObject(carte);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateCartePermessoRegione(String regione, CartaPermessoCostruzione c1, CartaPermessoCostruzione c2) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_CARTE_PERMESSO_REGIONE.toString());
            oos.flush();
            oos.writeObject(regione);
            oos.flush();
            oos.writeObject(c1);
            oos.flush();
            oos.writeObject(c2);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateCartePermessoGiocatore(int idGiocatore, List<CartaPermessoCostruzione> manoCartePermessoCostruzione) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_CARTE_PERMESSO_GIOCATORE.toString());
            oos.flush();
            oos.writeInt(idGiocatore);
            oos.flush();
            oos.writeObject(manoCartePermessoCostruzione);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateAiutanti(int idGiocatore, int numAiutanti) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_AIUTANTI.toString());
            oos.flush();
            oos.writeInt(idGiocatore);
            oos.flush();
            oos.writeInt(numAiutanti);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateRiservaAiutanti(int numAiutanti) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_RISERVA_AIUTANTI.toString());
            oos.flush();
            oos.writeInt(numAiutanti);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateRiservaConsiglieri(List<String> coloriConsiglieri) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_RISERVA_CONSIGLIERI.toString());
            oos.flush();
            oos.writeObject(coloriConsiglieri);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updatePercorsoNobiltà(int idGiocatore, int posizione) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_PERCORSO_NOBILTA.toString());
            oos.flush();
            oos.writeInt(idGiocatore);
            oos.flush();
            oos.writeInt(posizione);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_EMPORI_CITTA.toString());
            oos.flush();
            oos.writeObject(nomeCittà);
            oos.flush();
            oos.writeObject(idGiocatori);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateBonusCittà(String nomeCittà, Bonus bonus) throws RemoteException {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_BONUS_CITTA.toString());
            oos.flush();
            oos.writeObject(bonus);
            oos.flush();
            oos.writeObject(nomeCittà);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void updateEmporiDisponibiliGiocatore(int idGiocatore, int numeroEmporiDisponibili) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_EMPORI_DISPONIBILI_GIOCATORE.toString());
            oos.flush();
            oos.writeInt(idGiocatore);
            oos.flush();
            oos.writeInt(numeroEmporiDisponibili);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
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
    public void updatePosizioneRe(String nomeCittà) {
        try {
            oos.writeObject(ComunicazioneView.UPDATE_POSIZIONE_RE.toString());
            oos.flush();
            oos.writeObject(nomeCittà);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void eseguiTurno() {
        try {
            oos.writeObject(ComunicazioneView.ESEGUI_TURNO.toString());
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void fineTurno() throws RemoteException {
        try {
            oos.writeObject(ComunicazioneView.FINE_TURNO.toString());
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public void mostraMessaggio(String messaggio) {
        try {
            oos.writeObject(ComunicazioneView.MOSTRA_MESSAGGIO.toString());
            oos.flush();
            oos.writeObject(messaggio);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
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
