package server.sistema;


import classicondivise.ComunicazioneController;
import classicondivise.Vendibile;
import server.controller.Controller;
import classicondivise.carte.CartaPermessoCostruzione;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class SocketPollingController implements Runnable {
    private Socket socket;
    private Controller controller;
    private volatile boolean running = true;
    private ObjectInputStream ois;

    public SocketPollingController(Socket socket, Controller controller, ObjectInputStream ois) {
        this.socket = socket;
        this.controller = controller;
        this.ois = ois;
    }

    public void termina(){
        try {
            this.socket.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        running = false;
    }

    @Override
    public void run() {
        try {
            String inputLine, idBalcone, coloreConsigliereDaRiserva, stringaNomeCittà, regione, coloreConsigliere;
            int numeroCarta, prezzo;
            List<String> nomiColoriCartePolitica;
            CartaPermessoCostruzione cartaPermessoCostruzione;
            while (running) {
                try {
                    inputLine = (String) ois.readObject();
                    ComunicazioneController comunicazioneController = ComunicazioneController.valueOf(inputLine);
                    switch (comunicazioneController) {
                        case PASSA_TURNO:
                            controller.passaTurno();
                            break;
                        case ELEGGERE_CONSIGLIERE:
                            idBalcone = (String) ois.readObject();
                            coloreConsigliereDaRiserva  = (String) ois.readObject();
                            controller.eleggereConsigliere(idBalcone, coloreConsigliereDaRiserva);
                            break;
                        case ACQUISTARE_TESSERA_PERMESSO_COSTRUZIONE:
                            idBalcone = (String) ois.readObject();
                            try {
                                nomiColoriCartePolitica = (List<String>) ois.readObject();
                            } catch (ClassNotFoundException exc){
                                exc.printStackTrace();
                                break;
                            }
                            numeroCarta = Integer.valueOf((Integer) ois.readObject());
                            controller.acquistareTesseraPermessoCostruzione(idBalcone, nomiColoriCartePolitica, numeroCarta);
                            break;
                        case COSTRUIRE_EMPORIO_CON_TESSERA_PERMESSO_COSTRUZIONE:
                            try {
                                cartaPermessoCostruzione = (CartaPermessoCostruzione) ois.readObject();
                            } catch (ClassNotFoundException exc){
                                exc.printStackTrace();
                                break;
                            }
                            stringaNomeCittà = (String) ois.readObject();
                            controller.costruireEmporioConTesseraPermessoCostruzione(cartaPermessoCostruzione, stringaNomeCittà);
                            break;
                        case COSTRUIRE_EMPORIO_CON_AIUTO_RE:
                            try {
                                nomiColoriCartePolitica = (List<String>) ois.readObject();
                            } catch (ClassNotFoundException exc){
                                exc.printStackTrace();
                                break;
                            }
                            stringaNomeCittà = (String) ois.readObject();
                            controller.costruireEmporioConAiutoRe(nomiColoriCartePolitica, stringaNomeCittà);
                            break;
                        case INGAGGIARE_AIUTANTE:
                            controller.ingaggiareAiutante();
                            break;
                        case CAMBIARE_TESSERE_PERMESSO_COSTRUZIONE:
                            regione = (String) ois.readObject();
                            controller.cambiareTesserePermessoCostruzione(regione);
                            break;
                        case MANDARE_AIUTANTE_ELEGGERE_CONSIGLIERE:
                            idBalcone = (String) ois.readObject();
                            coloreConsigliere = (String) ois.readObject();
                            controller.mandareAiutanteEleggereConsigliere(idBalcone, coloreConsigliere);
                            break;
                        case COMPIERE_AZIONE_PRINCIPALE_AGGIUNTIVA:
                            controller.compiereAzionePrincipaleAggiuntiva();
                            break;
                        case VENDI_AIUTANTI:
                            break;
                        case VENDI_CARTE_PERMESSO:
                            prezzo = Integer.valueOf((Integer) ois.readObject());
                            List<CartaPermessoCostruzione> cartePermesso = (List<CartaPermessoCostruzione>) ois.readObject();
                            controller.vendiCartePermesso(cartePermesso, prezzo);
                            break;
                        case VENDI_CARTE_POLITICA:
                            prezzo = Integer.valueOf((Integer) ois.readObject());
                            List<String> cartePolitica = (List<String>) ois.readObject();
                            controller.vendiCartePolitica(cartePolitica, prezzo);
                            break;
                        case COMPRA_VENDIBILI:
                            List<Vendibile> vendibili = (List<Vendibile>) ois.readObject();
                            controller.compraVendibili(vendibili);
                            break;
                        default:
                            break;
                    }

                } catch (ClassNotFoundException exc) {
                    exc.printStackTrace();
                }
            }
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }
}
