package server.sistema;

import java.util.ArrayList;

/**
 * Created by emilianogagliardi on 15/06/16.
 */
/*
    SINGLETON
    NomiChiaveRMI serve esclusivamente ad ottenere una chiave
    su RMI registry diversa per ogni partita in atto (che viene comunicata ai client)
 */
public class NumeroNomeChiaveRMI {
    private static NumeroNomeChiaveRMI instance;
    private static ArrayList<Boolean> isNumeroInUso;
    private static int ultimoOttenuto;

    private NumeroNomeChiaveRMI() {
        isNumeroInUso = new ArrayList<>();
        isNumeroInUso.add(false);
    }

    public synchronized static void init(){
        instance = new NumeroNomeChiaveRMI();
    }

    public static int ottieniNuovoNumero() {
        for (int i = 0; i < isNumeroInUso.size(); i++){
            if (!isNumeroInUso.get(i)){ //il numero è già stato utilizzato in precedenza, ma ora non è in uso
                isNumeroInUso.set(i, true);
                ultimoOttenuto = i;
                return i;
            }
        }
        //tutti i numeri già utilizzati in precedenza sono ancora in uso
        isNumeroInUso.add(true);
        ultimoOttenuto = isNumeroInUso.size() - 1;
        return ultimoOttenuto;
    }

    public static int ottieniUltimoNumero(){
        System.out.println(ultimoOttenuto);
        return ultimoOttenuto;
    }
}
