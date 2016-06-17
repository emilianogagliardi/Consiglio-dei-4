package server.sistema;


import classicondivise.ComunicazioneController;
import server.controller.Controller;
import server.model.carte.CartaPermessoCostruzione;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class SocketPollingController implements Runnable {
    private Socket socket;
    private Controller controller;
    private volatile boolean running = true;

    public SocketPollingController(Socket socket, Controller controller) {
        this.socket = socket;
        this.controller = controller;
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
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());) {
            String inputLine, idBalcone, coloreConsigliereDaRiserva, stringaNomeCittà, regione, coloreConsigliere;
            int numeroCarta;
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
