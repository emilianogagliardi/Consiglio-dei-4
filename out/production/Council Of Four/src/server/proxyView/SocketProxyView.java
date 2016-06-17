package server.proxyView;

import interfaccecondivise.InterfacciaView;
import server.model.carte.CartaPermessoCostruzione;
import server.sistema.AvviatorePartita;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class SocketProxyView implements InterfacciaView {
    private AvviatorePartita avviatore;
    private int idGiocatore;
    private PrintWriter out;
    private Scanner in;
    private Socket socket;

    public SocketProxyView(Socket socket){
        try {
            out = new PrintWriter(socket.getOutputStream(), true); //non è necessario fare flush
            in = new Scanner(socket.getInputStream());
        }catch (IOException e){
            erroreDiConnessione();
            System.out.println("impossibile aprire output stream socket");
        }
    }

    public void setAvviatore(AvviatorePartita avviatore) {
        this.avviatore = avviatore;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void setIdGiocatore(int idGiocatore) {
        this.idGiocatore = idGiocatore;
    }

    @Override
    public int getIdGiocatore(){
        return idGiocatore;
    }

    @Override
    public void scegliMappa() {
        out.println("sceglimappa");
        System.out.println("in attesa della scelta da client");
        avviatore.setMappa(Integer.parseInt(in.nextLine()));
        System.out.println("ricevuta la scelta");
    }

    @Override
    public void iniziaAGiocare() throws RemoteException {
        out.println("start");
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