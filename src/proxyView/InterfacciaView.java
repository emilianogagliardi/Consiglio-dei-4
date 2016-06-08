package proxyView;

import model.carte.CartaPermessoCostruzione;

import java.util.ArrayList;
import java.util.HashMap;

public interface InterfacciaView {
    void setIdGiocatore(int idGiocatore);
    int getIdGiocatore();
    int scegliMappa();
    void erroreDiConnessione();
    void updatePuntiVittoriaGiocatore(int idGiocatore, int punti);
    void updateBalcone (String regione, String colore1, String colore2, String colore3, String colore4);
    void updateMonete (int idGiocatore, int idMonete);
    void updateCartePoliticaAvversari (int idGiocatore, int numCarte);
    void updateCartePoliticaProprie (ArrayList<String> carte);
    void updateCartePermessoRegione (String regione, CartaPermessoCostruzione c1, CartaPermessoCostruzione c2);
    void updateCartePermessoGiocatore (int idGiocatore, ArrayList<CartaPermessoCostruzione> mano);
    void updateAiutanti (int idGiocatore, int numAiutanti);
    void updateRiservaAiutanti(int numAiutanti);
    void updateRiservaConsiglieri(ArrayList<String> coloriConsiglieri);
    void updatePercorsoNobiltà(int idGiocatore, int posizione);
    void updateEmporiCittà(String nomeCittà, ArrayList<Integer> idGiocatori);
    void updateEmporiDisponibiliGiocatore (int idGiocatore, int num);
    void updateCarteBonusColoreCittàGiocatore(int idGiocatore, HashMap<String, Integer> mapCarte);
    void updateCarteBonusColoreCittàTabellone(String... colori);
    void updateCarteBonusReGiocatore(int idGiocatore, HashMap<String, Integer> mapCarte);
    void updateCarteBonusReTabellone(int puntiVIttoriaPrimaCarta); //solo la prima carta in vista del mazzo
    void updateCarteBonusRegioneGiocatore(int idGiocatore, HashMap<String, Integer> carte);
    void updateCarteBonusRegioneTabellone(HashMap<String, Integer> carte);
    void updatePosizioneRe(String città);
    void eseguiTurno();

}
