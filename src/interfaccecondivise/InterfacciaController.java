package interfaccecondivise;

import classicondivise.CartaPermessoCostruzione;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface InterfacciaController extends Remote, Serializable{ //serializable è per fare il bind su rmiregistry

    boolean passaTurno() throws RemoteException; //la View chiama passaTurno per passare il turno al giocatore successivo


    //azioni principali
    boolean eleggereConsigliere(String idBalcone, String coloreConsigliere) throws RemoteException;

    boolean acquistareTesseraPermessoCostruzione(String idBalcone, List<String> cartePolitica, int carta) throws RemoteException; //carta indica quale delle due carte permesso costruzione di regione ha scelto il giocatore (1 o 2)

    boolean costruireEmporioConTesseraPermessoCostruzione(CartaPermessoCostruzione cartaPermessoCostruzione, String città)  throws RemoteException;

    boolean costruireEmporioConAiutoRe(List<String> nomiColoriCartePolitica, String nomeCittàCostruzione)  throws RemoteException;

    //azioni veloci
    boolean ingaggiareAiutante()  throws RemoteException;

    boolean cambiareTesserePermessoCostruzione(String regione)  throws RemoteException;

    boolean mandareAiutanteEleggereConsigliere(String idBalcone, String coloreConsigliere)  throws RemoteException;

    boolean compiereAzionePrincipaleAggiuntiva()  throws RemoteException;

    //market
    boolean vendiCartePermesso(List<CartaPermessoCostruzione> cartePermesso, int prezzo) throws RemoteException;

    boolean vendiCartePolitica(List<String> cartePolitica, int prezzo) throws RemoteException;

    boolean vendiAiutanti(int numeroAiutanti, int prezzo) throws RemoteException;

    boolean compraCartePermesso(int idGiocatore,  List<CartaPermessoCostruzione> cartePermesso) throws RemoteException;

    boolean compraCartePolitica(int idGiocatore, List<String> cartePolitica) throws RemoteException;

    boolean compraAiutanti(int idGiocatore, int numeroAiutanti) throws RemoteException;


    void logout() throws RemoteException;
}
