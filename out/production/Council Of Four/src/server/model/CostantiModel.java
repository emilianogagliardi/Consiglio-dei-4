package server.model;

import server.model.carte.ColoreCartaPolitica;

public class CostantiModel {
    //caratteristiche della partita
    public static final int NUM_REGIONI = 3;
    public static final int NUM_CITTA = 15;
    public static final int NUM_CITTA_PER_REGIONE = NUM_CITTA/NUM_REGIONI;
    public static final int NUM_CARTE_POLITICA = 90;
    public final static int NUM_CARTE_POLITICA_PER_COLORE = NUM_CARTE_POLITICA / ColoreCartaPolitica.values().length;
    public static final int NUM_CARTE_PREMIO_RE = 5;
    public static final int[] PUNTI_CARTA_PREMIO_RE = {25, 18, 12, 7, 3};
    public static final int NUM_CARTE_BONUS_REGIONE = NUM_REGIONI;
    public static final int NUM_CARTE_PERMESSO = 45;
    public static final int NUM_CARTE_PERMESSO_REGIONE = NUM_CARTE_PERMESSO/NUM_REGIONI; //il funzionamento è garantito solo per valori >= 15
    public static final int NUM_AIUTANTI = 30;
    public static final int NUM_CONSIGLIERI = 24;
    public static final int NUM_CONSIGLIERI_RISERVA = 8;
    public final static int NUM_CONSIGLIERI_PER_COLORE = NUM_CONSIGLIERI / ColoreConsigliere.values().length;
    public static final int NUM_CONSIGLIERI_BALCONE = 4;
    public static final int NUM_CARTE_BONUS_COLORE_CITTA = 4;
    public static final int MAX_NUM_SOTTOBONUS_PER_BONUS = 3;
    public static final int MIN_NUM_SOTTOBONUS_PER_BONUS = 1;
    public static final int MAX_VALORE_SOTTOBONUS = 3;
    public static final int MIN_VALORE_SOTTOBONUS = 1;
    public static final int PUNTI_VITTORIA_BONUS_REGIONE = 10;
    public static final String CITTÀ_RE = "JUVELAR";
    public static final double PERCENTUALE_BONUS_PERCORSO_NOBILTA = 0.6;
    public static final int PUNTI_BONUS_COLORE_CITTA_ORO = 20;
    public static final int PUNTI_BONUS_COLORE_CITTA_ARGENTO = 12;
    public static final int PUNTI_BONUS_COLORE_CITTA_BRONZO = 8;
    public static final int PUNTI_BONUS_COLORE_CITTA_FERRO = 5;
    public static final int MONETE_PER_STRADA = 2; //monete da pagare per strada percorsa nella mossa acquistare una tessera permesso con l'aiuto del re
    //azione veloce ingaggiare un aiutante
    public static final int MONETE_INGAGGIARE_AIUTANTE = 3; //monete da pagare per eseguire la mossa veloce "ingaggiare un aiutante"
    public static final int AIUTANTI_GUADAGNATI_INGAGG_AIUTANTE = 1;

    public static final int AIUTANTI_PAGARE_CAMBIO_TESSERE_PERMESSO = 1; //aiutanti da pagare per effettuare l'azione veloce del cambio delle tessere permesso di una regione
    public static final int AIUTANTI_PAGARE_MANDA_AIUTANTE_ELEGG_CONS = 1; //aiutanti da pagare per effettuare l'azione veloce mandare un aiutante ad eleggere un consigliere
    public static final int AIUTANTI_PAGARE_AZIONE_PRINCIPALE_AGGIUNTIVA = 3; //aiutanti da pagare per compiere un'azione principale aggiuntiva
    //monete che si guadagnano nella mossa eleggereConsigliere
    public static final int MONETE_GUADAGNATE_ELEGGERE_CONSIGLIERE = 4;
    //monete da pagare nella mossa acquistarePermessoCostruzione per soddisfare un consiglio
    public static final int MONETE_1_CARTA_POLITICA = 10;
    public static final int MONETE_2_CARTE_POLITICA = 7;
    public static final int MONETE_3_CARTE_POLITICA = 4;
    public static final int MONETE_PER_CARTA_JOLLY = 1;
    //numero aiutanti da pagare per emporio già costruito in una città da altri giocatori
    public static final int NUMERO_AIUTANTI_PAGARE_EMPORIO = 1;
    //caratteristiche del giocatore
    public static final int MAX_MONETE = 30;
    public static final int MAX_POS_NOBILTA = 20;
    public static final int NUM_EMPORI_GIOCATORE = 10;
    public static final int MAX_PUNTI_VITTORIA = 99;
    public static final int NUM_CARTE_POLITICA_INIZIALI_GIOCATORE = 6;
    public final static int[] MONETE_INIZIALI_GIOCATORI = {10, 11, 12, 13};
    public final static int[] AIUTANTI_INIZIALI_GIIOCATORI = {4, 3, 2, 1};
}
