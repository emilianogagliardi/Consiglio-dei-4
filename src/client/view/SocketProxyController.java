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
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)){ //autoFlush enabled
            writer.println(ComunicazioneController.PASSA_TURNO.toString());
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean eleggereConsigliere(String idBalcone, String coloreConsigliere) {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)){ //autoFlush enabled
            writer.println(ComunicazioneController.ELEGGERE_CONSIGLIERE.toString());
            writer.println(idBalcone);
            writer.println(coloreConsigliere);
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean acquistareTesseraPermessoCostruzione(String idBalcone, List<String> cartePolitica, int carta) {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.ser"))){ //autoFlush enabled
            writer.println(ComunicazioneController.ACQUISTARE_TESSERA_PERMESSO_COSTRUZIONE.toString());
            writer.println(idBalcone);
            oos.writeObject(cartePolitica);
            writer.println(carta);
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean costruireEmporioConTesseraPermessoCostruzione(CartaPermessoCostruzione cartaPermessoCostruzione, String città) {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.ser"))){ //autoFlush enabled
            writer.println(ComunicazioneController.COSTRUIRE_EMPORIO_CON_TESSERA_PERMESSO_COSTRUZIONE.toString());
            oos.writeObject(cartaPermessoCostruzione);
            writer.println(città);
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean costruireEmporioConAiutoRe(List<String> nomiColoriCartePolitica, String nomeCittàCostruzione) {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.ser"))){ //autoFlush enabled
            writer.println(ComunicazioneController.COSTRUIRE_EMPORIO_CON_AIUTO_RE.toString());
            oos.writeObject(nomiColoriCartePolitica);
            writer.println(nomeCittàCostruzione);
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean ingaggiareAiutante() {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)){ //autoFlush enabled
            writer.println(ComunicazioneController.INGAGGIARE_AIUTANTE.toString());
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean cambiareTesserePermessoCostruzione(String regione) {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)){ //autoFlush enabled
            writer.println(ComunicazioneController.CAMBIARE_TESSERE_PERMESSO_COSTRUZIONE.toString());
            writer.println(regione);
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean mandareAiutanteEleggereConsigliere(String idBalcone, String coloreConsigliere) {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)){ //autoFlush enabled
            writer.println(ComunicazioneController.MANDARE_AIUTANTE_ELEGGERE_CONSIGLIERE.toString());
            writer.println(idBalcone);
            writer.println(coloreConsigliere);
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean compiereAzionePrincipaleAggiuntiva() {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)){ //autoFlush enabled
            writer.println(ComunicazioneController.COMPIERE_AZIONE_PRINCIPALE_AGGIUNTIVA.toString());
        } catch (IOException exc){
            return false;
        }
        return true;
    }
}
