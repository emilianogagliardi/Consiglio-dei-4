package client.view;

import classicondivise.carte.CartaPermessoCostruzione;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emilianogagliardi on 26/06/16.
 */
/*
classe che serve a memorizzare il numero di elementi disponibili eni pool di
partita in seguito alle update dal server
 */
public class RiservaPartitaView {
    private static RiservaPartitaView instance;
    private int aiutanti;
    private List<String> consiglieri;
    private CartaPermessoCostruzione[] carteCosta, carteCollina, carteMontagna;

    private RiservaPartitaView() {
        consiglieri = new ArrayList<>();
    }

    public static RiservaPartitaView getInstance(){
        if(instance == null) instance = new RiservaPartitaView();
        return instance;
    }

    public void setConsiglieri(List<String> consiglieri){this.consiglieri = consiglieri;}

    public List<String> getConsiglieri(){return consiglieri;}

    public void setAiutanti(int n) {aiutanti=n;}

    public int getAiutanti(){return aiutanti;}

    public CartaPermessoCostruzione[] getCartePermessoRegione(String regione) throws IllegalArgumentException{
        switch (regione){
            case "COSTA":
                return carteCosta;
            case "COLLINA":
                return carteCollina;
            case "MONTAGNA":
                return carteMontagna;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setCartePermessoCostruzione(String regione, CartaPermessoCostruzione[] carte) throws IllegalArgumentException{
        if(carte.length!=2) throw new IllegalArgumentException();
        CartaPermessoCostruzione[] carteprecedenti = getCartePermessoRegione(regione);
        carteprecedenti = carte;
    }
}
