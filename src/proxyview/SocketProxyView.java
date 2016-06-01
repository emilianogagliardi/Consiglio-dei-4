package proxyview;

import model.carte.CartaPermessoCostruzione;

import java.util.ArrayList;
import java.util.HashMap;

public class SocketProxyView implements InterfacciaView {

    @Override
    public void updateBalcone(String balcone, String colore1, String colore2, String colore3, String colore4) {

    }

    @Override
    public void updateMonete(int idGiocatore, int idMonete) {

    }

    @Override
    public void updateCartePoliticaAvversari(int idGiocatore, int numCarte) {

    }

    @Override
    public void updateCartePoliticaProprie(String... carte) {

    }

    @Override
    public void updateCartePermessoRegione(String regione, CartaPermessoCostruzione c1, CartaPermessoCostruzione c2) {

    }

    @Override
    public void updateCartePermessoGiocatore(int idGiocatore, ArrayList<CartaPermessoCostruzione> mano) {

    }

    @Override
    public void updateAiutanti(int idGiocatore, int numAiutanti) {

    }

    @Override
    public void updateRiservaAiutanti(int numAiutanti) {

    }

    @Override
    public void updateRiservaConsiglieri(String... colori) {

    }

    @Override
    public void updatePercorsoNobiltà(int idGiocatore, int posizione) {

    }

    @Override
    public void updateEmporiCittà(String nomeCittà, int... idGiocatori) {

    }

    @Override
    public void updateCarteBonusColoreCittàGiocatore(int idGiocatore, String colore) {

    }

    @Override
    public void updateCarteBonusColoreCittàTabellone(String... colori) {

    }

    @Override
    public void updateCarteBonusReGiocatore(int idGiocatore, int... puntiVittoria) {

    }

    @Override
    public void updateCarteBonusReTabellone(int puntiVIttoriaPrimaCarta) {

    }

    @Override
    public void updateCarteBonusRegioneGiocatore(int idGiocatore, HashMap<String, Integer> carte) {

    }

    @Override
    public void updateCarteBonusRegioneTabellone(HashMap<String, Integer> carte) {

    }

    @Override
    public void updatePosizioneRe(String città) {

    }
}
