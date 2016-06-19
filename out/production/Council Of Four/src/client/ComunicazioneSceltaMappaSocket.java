package client;

import client.view.eccezioni.SingletonNonInizializzatoException;

import java.io.IOException;
import java.io.ObjectOutputStream;


//singleton
public class ComunicazioneSceltaMappaSocket implements ComunicazioneSceltaMappa{
    private static ComunicazioneSceltaMappaSocket instance;
    private static ObjectOutputStream oos;

    private ComunicazioneSceltaMappaSocket(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public static void init(ObjectOutputStream oos) {
        instance = new ComunicazioneSceltaMappaSocket(oos);
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
