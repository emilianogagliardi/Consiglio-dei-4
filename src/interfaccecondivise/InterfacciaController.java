package interfaccecondivise;

import server.model.carte.CartaPermessoCostruzione;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.List;


public interface InterfacciaController extends Remote, Serializable{ //serializable è per fare il bind su rmiregistry
    void passaTurno(); //la View chiama passaTurno per passare il turno al giocatore successivo


    //azioni principali
    boolean eleggereConsigliere(String idBalcone, String coloreConsigliere);

    boolean acquistareTesseraPermessoCostruzione(String idBalcone, List<String> cartePolitica, int carta); //carta indica quale delle due carte permesso costruzione di regione ha scelto il giocatore (1 o 2)

    boolean costruireEmporioConTesseraPermessoCostruzione(CartaPermessoCostruzione cartaPermessoCostruzione, String città);

    boolean costruireEmporioConAiutoRe(List<String> nomiColoriCartePolitica, String nomeCittàCostruzione);

    //azioni veloci
    boolean ingaggiareAiutante();

    boolean cambiareTesserePermessoCostruzione(String regione);

    boolean mandareAiutanteEleggereConsigliere(String idBalcone, String coloreConsigliere);

    boolean compiereAzionePrincipaleAggiuntiva();
}
