package client.view;

import interfaccecondivise.InterfacciaController;
import server.model.carte.CartaPermessoCostruzione;

import java.net.Socket;
import java.util.List;

/*
questa classe serve ad essere passato come se fosse un controller quando il client apre una comunicazione
di tipo socket. Implementa la cominciazione necessaria all'esecuzione delle mosse nel server, rendendo
trasparente a controllerFXMosse il fatto che il controller sia in remoto, come nel caso di RMI
 */
public class SocketProxyController implements InterfacciaController {
    Socket socket;
    public SocketProxyController(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void passaTurno() {

    }

    @Override
    public boolean eleggereConsigliere(String idBalcone, String coloreConsigliere) {
        return false;
    }

    @Override
    public boolean acquistareTesseraPermessoCostruzione(String idBalcone, List<String> cartePolitica, int carta) {
        return false;
    }

    @Override
    public boolean costruireEmporioConTesseraPermessoCostruzione(CartaPermessoCostruzione cartaPermessoCostruzione, String città) {
        return false;
    }

    @Override
    public boolean costruireEmporioConAiutoRe(List<String> nomiColoriCartePolitica, String nomeCittàCostruzione) {
        return false;
    }

    @Override
    public boolean ingaggiareAiutante() {
        return false;
    }

    @Override
    public boolean cambiareTesserePermessoCostruzione(String regione) {
        return false;
    }

    @Override
    public boolean mandareAiutanteEleggereConsigliere(String idBalcone, String coloreConsigliere) {
        return false;
    }

    @Override
    public boolean compiereAzionePrincipaleAggiuntiva() {
        return false;
    }
}
