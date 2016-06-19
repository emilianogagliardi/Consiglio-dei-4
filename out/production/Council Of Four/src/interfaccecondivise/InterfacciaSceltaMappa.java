package interfaccecondivise;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface InterfacciaSceltaMappa extends Remote {
    void mappaScelta(int idMappa) throws RemoteException;
}
