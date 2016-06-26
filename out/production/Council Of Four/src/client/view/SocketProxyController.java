package client.view;

import classicondivise.ComunicazioneController;
import classicondivise.Vendibile;
import interfaccecondivise.InterfacciaController;
import classicondivise.carte.CartaPermessoCostruzione;

import java.io.*;
import java.rmi.RemoteException;
import java.util.List;

/*
questa classe serve ad essere passato come se fosse un controller quando il client apre una comunicazione
di tipo socket. Implementa la cominciazione necessaria all'esecuzione delle mosse nel server, rendendo
trasparente a controllerFXMosse il fatto che il controller sia in remoto, come nel caso di RMI
 */
public class SocketProxyController implements InterfacciaController {
    private ObjectOutputStream oos;

    public SocketProxyController(ObjectOutputStream oos) throws IOException {
        this.oos = oos;
    }

    @Override
    public boolean passaTurno() {
        try {
            oos.writeObject(ComunicazioneController.PASSA_TURNO.toString());
            oos.flush();
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean eleggereConsigliere(String idBalcone, String coloreConsigliere) {
        try {
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
        try {
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
        try {
            oos.writeObject(ComunicazioneController.COSTRUIRE_EMPORIO_CON_TESSERA_PERMESSO_COSTRUZIONE.toString());
            oos.flush();
            oos.writeObject(cartaPermessoCostruzione);
            oos.flush();
            oos.writeObject(città);
            oos.flush();
        } catch (IOException exc) {
            return false;
        }
        return true;
    }

    @Override
    public boolean costruireEmporioConAiutoRe(List<String> nomiColoriCartePolitica, String nomeCittàCostruzione) {
        try {
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
        try {
            oos.writeObject(ComunicazioneController.INGAGGIARE_AIUTANTE.toString());
            oos.flush();
        } catch (IOException exc) {
            return false;
        }
        return true;
    }

    @Override
    public boolean cambiareTesserePermessoCostruzione(String regione) {
        try {
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
        try {
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
        try {
            oos.writeObject(ComunicazioneController.COMPIERE_AZIONE_PRINCIPALE_AGGIUNTIVA.toString());
            oos.flush();
        } catch (IOException exc){
            return false;
        }
        return true;
    }

    @Override
    public boolean vendiCartePermesso(List<CartaPermessoCostruzione> cartePermesso, int prezzo) throws RemoteException {
        return false;
    }

    @Override
    public boolean vendiCartePolitica(List<String> cartePolitica, int prezzo) throws RemoteException {
        return false;
    }

    @Override
    public boolean vendiAiutanti(int numeroAiutanti, int prezzo) throws RemoteException {
        return false;
    }

    @Override
    public boolean compraVendibili(List<Vendibile> vendibili) throws RemoteException {
        return false;
    }

    @Override
    public void logout() throws RemoteException {

    }
}
