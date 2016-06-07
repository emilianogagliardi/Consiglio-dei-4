package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * L'instaurazione delle connessioni viene fatta tramite socket. Quando un client si connette comunica immediatamente
 * il suo nickname e in che modo vorrà comunicare, se tramite socket (inviando la stringa "socket") o RMI (inviando la
 * stringa "RMI")
 */

/*TODO bisogna passare ad avviaPartita i socket
    avvia partita non può fare operazioni bloccanti, perchè terrebbe il server in stop,
    è necessario comunicare con un client per fargli scegliere la mappa, quindi sarà necessario
    che questa operazione avvenga in un nuovo thread. è opportuno che sia il controller a generare la partita??
*/
public class Server {
    private HashMap<Integer, String> giocatori = new HashMap<>();
    private int idCorrente = 0;
    private ExecutorService executors = Executors.newCachedThreadPool();

    public void startServer() {
        ServerSocket serverSocket = null;
        ScheduledExecutorService timeoutExecutor = Executors.newSingleThreadScheduledExecutor();
        Thread timeoutThread = new ThreadTimeout(this);
        try {
            serverSocket = new ServerSocket(CostantiSistema.PORT);
        }catch (IOException e) {
            System.out.println("Impossibile inizializzare server socket");
        }
        while (true) {
            try{
                Socket socket = serverSocket.accept();
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                String comunicazione = in.nextLine();
                if (!comunicazione.equals("socket") || !comunicazione.equals("RMI")){
                    out.println("false"); //rifiuta la connessione se comunicazione non è valida
                }else{
                    out.println("true"); //conferma connessione accettata
                }
                giocatori.put(idCorrente, comunicazione);
                idCorrente++;
                if (giocatori.size() == CostantiSistema.NUM_GIOCATORI_TIMEOUT) { //start thread di timeout
                    timeoutExecutor.schedule(timeoutThread, CostantiSistema.TIMEOUT, TimeUnit.SECONDS);
                }else if (giocatori.size() == CostantiSistema.NUM_GOCATORI_MAX) {
                    timeoutThread.interrupt(); //killa il thread di timeout
                    fineGiocatoriAccettati();
                }
            }catch (IOException e){
                System.out.println("impossibile creare socket da server socket");
            }catch (NullPointerException e) {
                System.out.println("server socket è null, impossibile eseguire accept");
                break;
            }
        }
    }

    public void fineGiocatoriAccettati(){
        AvviaPartita();
        creaNuovaMappaGiocatori();
        idCorrente = 0;
    }

    private void creaNuovaMappaGiocatori() {
        giocatori = new HashMap<>();
    }

    private void AvviaPartita() {
        //executors.submit(new Controller());//anche parametro proxyView.InterfacciaView
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}
