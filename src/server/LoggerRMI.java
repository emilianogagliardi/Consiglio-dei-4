package server;

import proxyview.InterfacciaView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class LoggerRMI extends UnicastRemoteObject implements InterfacciaLoggerRMI {
    Server server;

    public LoggerRMI(Server s) throws RemoteException{
        server = s;
    }

    @Override
    public void login(InterfacciaView view) {
        server.addView(view);
    }
}