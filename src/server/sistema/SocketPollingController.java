package server.sistema;


import classicondivise.ComunicazioneController;
import server.controller.Controller;

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
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.ser"));) {
            String inputLine, idBalcone, coloreConsigliereDaRiserva;
            int numeroCarta;
            List<String> nomiColoriCartePolitica;
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
                    default:
                        break;
                }
            }
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }
}
