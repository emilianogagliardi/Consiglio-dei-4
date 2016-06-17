package client;

import client.view.eccezioni.SingletonNonInizializzatoException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;


//singleton
public class ComunicazioneSceltaMappaSocket implements ComunicazioneSceltaMappa{
    private static ComunicazioneSceltaMappaSocket instance;
    private static ObjectOutputStream oos;

    private ComunicazioneSceltaMappaSocket(Socket socket) {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
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
        try{
            oos.writeInt(id);
            oos.flush();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }
}
