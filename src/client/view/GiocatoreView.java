package client.view;

import classicondivise.carte.CartaPermessoCostruzione;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emilianogagliardi on 26/06/16.
 */
/*
classe che contiene alcune caratteristiche del giocatore, senza metodi di logica o di manipolazione dei dati,
serve a tenere traccia delle risorse del giocatore in modo da mostrare nella scelta delle mosse i suoi elementi
 */
public class GiocatoreView {
    private  static GiocatoreView instance;
    private List<String> cartePolitica;
    private List<CartaPermessoCostruzione> cartePermesso;
    private int monete;
    private int aiutanti;

    private GiocatoreView(){
        cartePermesso = new ArrayList<>();
        cartePolitica = new ArrayList<>();
    }

    public static GiocatoreView getInstance(){
        if (instance == null) {
            instance = new GiocatoreView();
        }
        return instance;
    }

    public void setAiutanti(int n){aiutanti = n;}
    public int getAiutanti(){return aiutanti;}

    public void setMonete(int n){monete = n;}
    public  int getMonete(int n){return monete;}

    public void setCartePolitica(List<String> cartePolitica){
        this.cartePolitica = cartePolitica;
    }

    public void setCartePermesso(List<CartaPermessoCostruzione> cartePermesso){
        this.cartePermesso = cartePermesso;
    }

    public List<String> getCartePolitica(){
        return cartePolitica;
    }

    public List<CartaPermessoCostruzione> getCartePermesso(){
        return cartePermesso;
    }
}
