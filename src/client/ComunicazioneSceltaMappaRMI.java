package client;

import client.view.CostantiClient;
import client.view.eccezioni.SingletonNonInizializzatoException;
import interfaccecondivise.InterfacciaSceltaMappa;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by emilianogagliardi on 15/06/16.
 */
//singleton
public class ComunicazioneSceltaMappaRMI implements ComunicazioneSceltaMappa{
    private InterfacciaSceltaMappa setterMappa;
    private static ComunicazioneSceltaMappaRMI instance;

    private ComunicazioneSceltaMappaRMI(String chiaveSceltaMappaRMI) {
        try {
            Registry registry = LocateRegistry.getRegistry(CostantiClient.IP_SERVER, CostantiClient.REGISTRY_PORT);
            setterMappa = (InterfacciaSceltaMappa) registry.lookup(chiaveSceltaMappaRMI);
        } catch (RemoteException | NotBoundException e) {
            System.out.println("impossibile comunicare scelta mappa in rmi");
            e.printStackTrace();
        }
    }

    public static void init(String chiaveSceltaMappaRMI){
        if (instance == null){
            instance = new ComunicazioneSceltaMappaRMI(chiaveSceltaMappaRMI);
        }
    }

    public static ComunicazioneSceltaMappaRMI getInstance() throws SingletonNonInizializzatoException{
        if (instance == null) throw new SingletonNonInizializzatoException();
        return instance;
    }

    @Override
    public void comunicaSceltaMappa(int id) {
        try {
            setterMappa.mappaScelta(id);
        } catch (RemoteException e) {
            System.out.println("impossibile comunicare scelta mappa in rmi");
            e.printStackTrace();
        }
    }
}
