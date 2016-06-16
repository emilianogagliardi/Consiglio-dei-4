package server.sistema;


import classicondivise.ComunicazioneController;
import server.controller.Controller;
import server.model.carte.CartaPermessoCostruzione;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class SocketPollingController implements Runnable{
    private Socket socket; //TODO: il chiamante deve chiudere il socket
    private Controller controller;
    private volatile boolean running = true;

    public SocketPollingController(Socket socket, Controller controller) {
        this.socket = socket;
        this.controller = controller;
    }

    public void termina(){
        running = false;
    } //TODO: ricordarsi di chiamare questo metodo

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.ser"));) {
            String inputLine, idBalcone, coloreConsigliereDaRiserva, stringaNomeCittà, regione, coloreConsigliere;
            int numeroCarta;
            List<String> nomiColoriCartePolitica;
            CartaPermessoCostruzione cartaPermessoCostruzione;
            while (running) {
                inputLine = in.readLine();
                ComunicazioneController comunicazioneController = ComunicazioneController.valueOf(inputLine);
                switch (comunicazioneController) {
                    case PASSA_TURNO:
                        controller.passaTurno();
                        break;
                    case ELEGGERE_CONSIGLIERE:
                        idBalcone = in.readLine();
                        coloreConsigliereDaRiserva  = in.readLine();
                        controller.eleggereConsigliere(idBalcone, coloreConsigliereDaRiserva);
                        break;
                    case ACQUISTARE_TESSERA_PERMESSO_COSTRUZIONE:
                        idBalcone = in.readLine();
                        try {
                            nomiColoriCartePolitica = (List<String>) ois.readObject();
                        } catch (ClassNotFoundException exc){
                            exc.printStackTrace();
                            break;
                        }
                        numeroCarta = Integer.valueOf(in.readLine());
                        controller.acquistareTesseraPermessoCostruzione(idBalcone, nomiColoriCartePolitica, numeroCarta);
                        break;
                    case COSTRUIRE_EMPORIO_CON_TESSERA_PERMESSO_COSTRUZIONE:
                        try {
                            cartaPermessoCostruzione = (CartaPermessoCostruzione) ois.readObject();
                        } catch (ClassNotFoundException exc){
                            exc.printStackTrace();
                            break;
                        }
                        stringaNomeCittà = in.readLine();
                        controller.costruireEmporioConTesseraPermessoCostruzione(cartaPermessoCostruzione, stringaNomeCittà);
                        break;
                    case COSTRUIRE_EMPORIO_CON_AIUTO_RE:
                        try {
                            nomiColoriCartePolitica = (List<String>) ois.readObject();
                        } catch (ClassNotFoundException exc){
                            exc.printStackTrace();
                            break;
                        }
                        stringaNomeCittà = in.readLine();
                        controller.costruireEmporioConAiutoRe(nomiColoriCartePolitica, stringaNomeCittà);
                        break;
                    case INGAGGIARE_AIUTANTE:
                        controller.ingaggiareAiutante();
                        break;
                    case CAMBIARE_TESSERE_PERMESSO_COSTRUZIONE:
                        regione = in.readLine();
                        controller.cambiareTesserePermessoCostruzione(regione);
                        break;
                    case MANDARE_AIUTANTE_ELEGGERE_CONSIGLIERE:
                        idBalcone = in.readLine();
                        coloreConsigliere = in.readLine();
                        controller.mandareAiutanteEleggereConsigliere(idBalcone, coloreConsigliere);
                        break;
                    case COMPIERE_AZIONE_PRINCIPALE_AGGIUNTIVA:
                        controller.compiereAzionePrincipaleAggiuntiva();
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }
}
