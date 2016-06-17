package client.view;

import classicondivise.ComunicazioneController;
import interfaccecondivise.InterfacciaController;
import server.model.carte.CartaPermessoCostruzione;

import java.io.*;
import java.net.Socket;
import java.util.List;

/*
questa classe serve ad essere passato come se fosse un controller quando il client apre una comunicazione
di tipo socket. Implementa la cominciazione necessaria all'esecuzione delle mosse nel server, rendendo
trasparente a controllerFXMosse il fatto che il controller sia in remoto, come nel caso di RMI
 */
public class SocketProxyController implements InterfacciaController {
    Socket socket;

    public SocketProxyController(Socket socket) throws IOException {
        this.socket = socket; //TODO: il chiamante deve chiudere il socket
    }

    @Override
    public boolean passaTurno() {
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
            oos.writeObject(ComunicazioneController.PASSA_TURNO.toString());
            oos.flush();
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean eleggereConsigliere(String idBalcone, String coloreConsigliere) {
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
            oos.writeObject(ComunicazioneController.ELEGGERE_CONSIGLIERE.toString());
            oos.flush();
            oos.writeObject(idBalcone);
            oos.flush();
            oos.writeObject(coloreConsigliere);
            oos.flush();
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean acquistareTesseraPermessoCostruzione(String idBalcone, List<String> cartePolitica, int carta) {
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
            oos.writeObject(ComunicazioneController.ACQUISTARE_TESSERA_PERMESSO_COSTRUZIONE.toString());
            oos.flush();
            oos.writeObject(idBalcone);
            oos.flush();
            oos.writeObject(cartePolitica);
            oos.flush();
            oos.writeObject(carta);
            oos.flush();
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean costruireEmporioConTesseraPermessoCostruzione(CartaPermessoCostruzione cartaPermessoCostruzione, String città) {
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
            oos.writeObject(ComunicazioneController.COSTRUIRE_EMPORIO_CON_TESSERA_PERMESSO_COSTRUZIONE.toString());
            oos.flush();
            oos.writeObject(cartaPermessoCostruzione);
            oos.flush();
            oos.writeObject(città);
            oos.flush();
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean costruireEmporioConAiutoRe(List<String> nomiColoriCartePolitica, String nomeCittàCostruzione) {
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
            oos.writeObject(ComunicazioneController.COSTRUIRE_EMPORIO_CON_AIUTO_RE.toString());
            oos.flush();
            oos.writeObject(nomiColoriCartePolitica);
            oos.flush();
            oos.writeObject(nomeCittàCostruzione);
            oos.flush();
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean ingaggiareAiutante() {
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
            oos.writeObject(ComunicazioneController.INGAGGIARE_AIUTANTE.toString());
            oos.flush();
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean cambiareTesserePermessoCostruzione(String regione) {
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
            oos.writeObject(ComunicazioneController.CAMBIARE_TESSERE_PERMESSO_COSTRUZIONE.toString());
            oos.flush();
            oos.writeObject(regione);
            oos.flush();
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean mandareAiutanteEleggereConsigliere(String idBalcone, String coloreConsigliere) {
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
            oos.writeObject(ComunicazioneController.MANDARE_AIUTANTE_ELEGGERE_CONSIGLIERE.toString());
            oos.flush();
            oos.writeObject(idBalcone);
            oos.flush();
            oos.writeObject(coloreConsigliere);
            oos.flush();
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean compiereAzionePrincipaleAggiuntiva() {
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
            oos.writeObject(ComunicazioneController.COMPIERE_AZIONE_PRINCIPALE_AGGIUNTIVA.toString());
            oos.flush();
        } catch (IOException exc){
            return false;
        }
        return true;
    }
}
