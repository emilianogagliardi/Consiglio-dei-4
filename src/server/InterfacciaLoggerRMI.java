package server;

import proxyView.InterfacciaView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfacciaLoggerRMI extends Remote {
    int login(InterfacciaView view) throws RemoteException;
}
