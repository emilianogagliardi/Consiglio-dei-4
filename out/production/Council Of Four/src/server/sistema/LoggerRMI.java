package server.sistema;

import interfaccecondivise.InterfacciaLoggerRMI;
import interfaccecondivise.InterfacciaView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class LoggerRMI extends UnicastRemoteObject implements InterfacciaLoggerRMI {
    Server server;

    public LoggerRMI(Server s) throws RemoteException{
        server = s;
    }

    @Override
    public void login(InterfacciaView view) {
            //int idAggiunto = server.getIdCorrente();
            server.addView(view);
    }

    @Override
    public String getChiaveController() throws RemoteException {
        return PrefissiChiaviRMI.PREFISSO_CHIAVE_CONTROLLER + NumeroNomeChiaveRMI.ottieniUltimoNumero();
    }

    @Override
    public String getChiaveSceltaMappa() throws RemoteException {
            return PrefissiChiaviRMI.PREFISSO_CHIAVE_SCELTA_MAPPA + NumeroNomeChiaveRMI.ottieniUltimoNumero();
    }

}