package server;

import proxyView.InterfacciaView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class LoggerRMI extends UnicastRemoteObject implements InterfacciaLoggerRMI {
    Server server;

    public LoggerRMI(Server s) throws RemoteException{
        server = s;
    }

    @Override
    public int login(InterfacciaView view) {
        int idAggiunto = server.getIdCorrente();
        server.addView(view);
        return idAggiunto;
    }
}