package model;

public class Costanti {
    //caratteristiche della partita
    public static final int NUM_REGIONI = 3;
    public static final int NUM_CITTA = 15;
    public static final int NUM_CITTA_PER_REGIONE = NUM_CITTA/NUM_REGIONI;
    public static final int NUM_CARTE_POLITICA = 90;
    public static final int NUM_CARTE_PREMIO_RE = 5;
    public static final int NUM_CARTE_BONUS_REGIONE = NUM_REGIONI;
    public static final int NUM_CARTE_PERMESSO = 45;
    public static final int NUM_CARTE_PERMESSO_REGIONE = NUM_CARTE_BONUS_REGIONE/NUM_REGIONI;
    public static final int NUM_AIUTANTI = 30;
    public static final int NUM_CONSIGLIERI = 24;
    public static final int NUM_CONSIGLIERI_RISERVA = 8;
    public static final int NUM_CONSIGLIERI_BALCONE = 4;
    public static final NomeCittà CITTA_RE = NomeCittà.JUVELAR;
    public static final int MAX_GIOCATORI = 4;
    public static final int NUM_CARTE_BONUS_COLORE_CITTA = 4;
    //caratteristiche del giocatore
    public static final int MAX_MONETE = 30;
    public static final int MAX_POS_NOBILTA = 20;
    public static final int NUM_EMPORI_GIOCATORE = 10;
    public static final int NUM_CARTE_POLITICA_INIZIALI = 6;
    public static final int MAX_PUNTI_VITTORIA = 99;
}
