package client;

import client.view.eccezioni.SingletonNonInizializzatoException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by emilianogagliardi on 15/06/16.
 */
//singleton
public class ComunicazioneSceltaMappaSocket implements ComunicazioneSceltaMappa{
    private static ComunicazioneSceltaMappaSocket instance;
    private static PrintWriter out;

    private ComunicazioneSceltaMappaSocket(Socket socket) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("impossibile comunicare la scelta della mappa in socket");
            e.printStackTrace();
        }
    }

    public static void init(Socket socket) {
        instance = new ComunicazioneSceltaMappaSocket(socket);
    }

    public static ComunicazioneSceltaMappa getInstance() throws SingletonNonInizializzatoException{
        if (instance == null) throw new SingletonNonInizializzatoException();
        else return instance;
    }

    @Override
    public void comunicaSceltaMappa(int id) {
        out.println(id);
        out.flush();
    }
}
