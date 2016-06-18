package interfaccecondivise;

import server.model.carte.CartaPermessoCostruzione;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface InterfacciaView extends Remote {
    void setIdGiocatore(int idGiocatore) throws RemoteException;
    int getIdGiocatore() throws RemoteException;
    void scegliMappa() throws RemoteException;
    void iniziaAGiocare()throws RemoteException;
    void erroreDiConnessione() throws RemoteException;
    void updatePuntiVittoriaGiocatore(int idGiocatore, int punti) throws RemoteException;
    void updateBalcone(String regione, String colore1, String colore2, String colore3, String colore4) throws RemoteException;
    void updateMonete(int idGiocatore, int idMonete) throws RemoteException;
    void updateCartePoliticaAvversari(int idGiocatore, int numCarte) throws RemoteException;
    void updateCartePoliticaProprie(List<String> carte) throws RemoteException;
    void updateCartePermessoRegione(String regione, CartaPermessoCostruzione c1, CartaPermessoCostruzione c2) throws RemoteException;
    void updateCartePermessoGiocatore(int idGiocatore, List<CartaPermessoCostruzione> mano) throws RemoteException;
    void updateAiutanti(int idGiocatore, int numAiutanti) throws RemoteException;
    void updateRiservaAiutanti(int numAiutanti) throws RemoteException;
    void updateRiservaConsiglieri(List<String> coloriConsiglieri) throws RemoteException;
    void updatePercorsoNobiltà(int idGiocatore, int posizione) throws RemoteException;
    void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori) throws RemoteException;
    void updateEmporiDisponibiliGiocatore(int idGiocatore, int num) throws RemoteException;
    void updateCarteBonusColoreCittàGiocatore(int idGiocatore, HashMap<String, Integer> mapCarte) throws RemoteException;
    void updateCarteBonusColoreCittàTabellone(HashMap<String, Integer> coloriEPunti) throws RemoteException;
    void updateCarteBonusReGiocatore(int idGiocatore, HashMap<String, Integer> mapCarte) throws RemoteException;
    void updateCarteBonusReTabellone(int puntiVIttoriaPrimaCarta) throws RemoteException; //solo la prima carta in vista del mazzo
    void updateCarteBonusRegioneGiocatore(int idGiocatore, HashMap<String, Integer> carte) throws RemoteException;
    void updateCarteBonusRegioneTabellone(String nomeRegione, int puntiCarta) throws RemoteException;
    void updatePosizioneRe(String città) throws RemoteException;
    void eseguiTurno() throws RemoteException;
    void fineTurno() throws RemoteException;
    void mostraMessaggio(String messaggio) throws RemoteException;
}
