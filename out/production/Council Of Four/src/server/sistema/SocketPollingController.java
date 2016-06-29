package server.sistema;


import classicondivise.ComunicazioneController;
import classicondivise.Vendibile;
import server.controller.Controller;
import classicondivise.carte.CartaPermessoCostruzione;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class SocketPollingController implements Runnable {
    private Controller controller;
    private volatile boolean running = true;
    private ObjectInputStream ois;

    SocketPollingController(Controller controller, ObjectInputStream ois) {
        this.controller = controller;
        this.ois = ois;
    }

    @Override
    public void run() {
        try {
            String inputLine, idBalcone, coloreConsigliereDaRiserva, stringaNomeCittà, regione, coloreConsigliere;
            int numeroCarta, idGiocatore;
            List<String> nomiColoriCartePolitica;
            CartaPermessoCostruzione cartaPermessoCostruzione;
            List<Vendibile> vendibili;
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
                        case VENDI:
                            vendibili = (List<Vendibile>) ois.readObject();
                            controller.vendi(vendibili);
                            break;
                        case COMPRA:
                            vendibili = (List<Vendibile>) ois.readObject();
                            controller.compra(vendibili);
                        case LOGOUT:
                            idGiocatore = ois.readInt();
                            controller.logout(idGiocatore);
                            ois.close();
                            running = false;
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
