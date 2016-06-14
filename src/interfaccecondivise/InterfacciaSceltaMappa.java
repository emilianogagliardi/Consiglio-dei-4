package interfaccecondivise;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by emilianogagliardi on 14/06/16.
 */
public interface InterfacciaSceltaMappa extends Remote {
    void mappaScelta(int idMappa) throws RemoteException;
}
