package server;

import proxyView.InterfacciaView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfacciaLoggerRMI extends Remote {
    void login(InterfacciaView view) throws RemoteException;
}
