package server.sistema;

/**
 * Created by emilianogagliardi on 15/06/16.
 */
/*
    SINGLETON
    NomiChiaveRMI conta il numero di partite in esecuzione. Serve esclusivamente ad ottenere una chiave
    su RMI registry diversa per ogni partita in atto (che viene comunicata ai client)
 */
public class NomeChiaveRMI {
    private static NomeChiaveRMI instance;
    private static int numero;

    private NomeChiaveRMI() {
        numero = 0;
    }

    public synchronized static int init(){
        return numero;
    }

    public static String getChiaveController() {
        return "controller" + numero;
    }

    public static String getChiaveSceltaMappa() {
        return "sceltamappa" + numero;
    }

    public synchronized static void incrementa() {
        numero ++;
    }
}
