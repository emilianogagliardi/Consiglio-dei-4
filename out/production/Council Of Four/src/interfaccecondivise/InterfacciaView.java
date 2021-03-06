package interfaccecondivise;

import classicondivise.Vendibile;
import classicondivise.bonus.Bonus;
import classicondivise.carte.CartaPermessoCostruzione;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfacciaView extends Remote {
    void setIdGiocatore(int idGiocatore) throws RemoteException;
    int getIdGiocatore() throws RemoteException;
    void scegliMappa() throws RemoteException;
    void iniziaAGiocare(int idMappa)throws RemoteException;
    void erroreDiConnessione() throws RemoteException;
    void updatePuntiVittoriaGiocatore(int idGiocatore, int punti) throws RemoteException;
    void updateBalcone(String idBalcone, String colore1, String colore2, String colore3, String colore4) throws RemoteException;
    void updateMonete(int idGiocatore, int monete) throws RemoteException;
    void updateCartePoliticaAvversari(int idGiocatore, int numCarte) throws RemoteException;
    void updateCartePoliticaProprie(List<String> carte) throws RemoteException;
    void updateCartePermessoRegione(String regione, CartaPermessoCostruzione c1, CartaPermessoCostruzione c2) throws RemoteException;
    void updateCartePermessoGiocatore(int idGiocatore, List<CartaPermessoCostruzione> manoCartePermessoCostruzione) throws RemoteException;
    void updateAiutanti(int idGiocatore, int numAiutanti) throws RemoteException;
    void updateRiservaAiutanti(int numAiutanti) throws RemoteException;
    void updateRiservaConsiglieri(List<String> coloriConsiglieri) throws RemoteException;
    void updatePercorsoNobiltà(int idGiocatore, int posizione) throws RemoteException;
    void updateEmporiCittà(String nomeCittà, List<Integer> idGiocatori) throws RemoteException;
    void updateBonusCittà(String nomeCittà, Bonus bonus) throws RemoteException;
    void updateEmporiDisponibiliGiocatore(int idGiocatore, int numeroEmporiDisponibili) throws RemoteException;
    void updatePosizioneRe(String nomeCittà) throws RemoteException;
    void eseguiTurno() throws RemoteException;
    void fineTurno() throws RemoteException; //per indicare alla view che è finito il suo turno di gioco o il suo turno di market
    void mostraMessaggio(String messaggio) throws RemoteException;
    void updateBonusPercorsoNobiltà(List<Bonus> percorso)throws RemoteException;
    void vendi() throws RemoteException;
    void fineVendi() throws RemoteException;
    void fineCompra() throws RemoteException;
    void compra(List<Vendibile> vendibili) throws RemoteException;
    void logOut() throws RemoteException;
}
