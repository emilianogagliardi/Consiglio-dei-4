package server.sistema;

import interfaccecondivise.InterfacciaSceltaMappa;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SceltaMappaRMI implements InterfacciaSceltaMappa, Remote {
    transient AvviatorePartita avviatorePartita;

    public SceltaMappaRMI(AvviatorePartita avviatorePartita) throws RemoteException{
        this.avviatorePartita = avviatorePartita;
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void mappaScelta(int idMappa) throws RemoteException{
        avviatorePartita.setMappa(idMappa);
        avviatorePartita.mappaSettata();
    }
}
