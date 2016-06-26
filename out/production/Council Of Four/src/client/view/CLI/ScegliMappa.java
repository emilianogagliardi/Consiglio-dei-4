package client.view.CLI;


import client.ComunicazioneSceltaMappa;
import client.ComunicazioneSceltaMappaRMI;
import client.ComunicazioneSceltaMappaSocket;
import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaLoggerRMI;

import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ScegliMappa implements Runnable {
    private String connectionType;
    private InterfacciaLoggerRMI loggerRMI;
    private ObjectOutputStream oos;

    public ScegliMappa(String  connectionType, InterfacciaLoggerRMI loggerRMI, ObjectOutputStream oos){
        this.connectionType = connectionType;
        this.loggerRMI = loggerRMI;
        this.oos = oos;
    }


    @Override
    public void run() {
        ComunicazioneSceltaMappa setterMappa;
        Scanner in = new Scanner(System.in);
        try {
            if (connectionType.equals("R")) {
                ComunicazioneSceltaMappaRMI.init(loggerRMI.getChiaveSceltaMappa());
                setterMappa = ComunicazioneSceltaMappaRMI.getInstance();
                System.out.println("Inserisci il numero di mappa che vuoi utilizzare");
                int id = in.nextInt();
                setterMappa.comunicaSceltaMappa(id);
            } else if (connectionType.equals("S")) {
                ComunicazioneSceltaMappaSocket.init(oos);
                setterMappa = ComunicazioneSceltaMappaSocket.getInstance();
                System.out.println("Inserisci il numero di mappa che vuoi utilizzare");
                int id = in.nextInt();
                setterMappa.comunicaSceltaMappa(id);
            }
        } catch (SingletonNonInizializzatoException | RemoteException exc) {
            exc.printStackTrace();
        }
    }
}
