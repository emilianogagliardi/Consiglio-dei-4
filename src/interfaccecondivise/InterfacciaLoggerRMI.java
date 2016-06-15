package interfaccecondivise;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfacciaLoggerRMI extends Remote {
    int login(InterfacciaView view) throws RemoteException;
    String getChiaveController() throws RemoteException;
    String getChiaveSceltaMappa() throws RemoteException;
}
