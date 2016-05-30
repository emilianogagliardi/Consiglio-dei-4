package instauraconnessione;

import model.Partita;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * L'instaurazione delle connessioni viene fatta tramite socket. Quando un client si connette comunica immediatamente
 * il suo nickname e in che modo vorrà comunicare, se tramite socket (inviando la stringa "socket") o RMI (inviando la
 * stringa "RMI")
 */

//TODO sincronizzazione????

public class InstauraConnessioni {
    private HashMap<String, String> giocatori = new HashMap<>();
    private ExecutorService executors = Executors.newCachedThreadPool();

    public void startServer() {
        ServerSocket serverSocket = null;
        ThreadTimeout timeout = new ThreadTimeout(this);
        try {
            serverSocket = new ServerSocket(CostantiSistema.PORT);
        }catch (IOException e) {
            System.out.println("Impossibile inizializzare server socket");
        }
        while (true) {
            try{
                Socket socket = serverSocket.accept();
                Scanner in = new Scanner(socket.getInputStream());
                String nickname = in.nextLine();
                String comunicazione = in.nextLine();
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                if (!comunicazione.equals("socket") || !comunicazione.equals("RMI")){
                    out.println("false"); //rifiuta la connessione se comunicazione non è valida
                }else{
                    out.println("true"); //conferma connessione accettata
                }
                giocatori.put(nickname, comunicazione);
                if (giocatori.size() == CostantiSistema.NUM_GIOCATORI_TIMEOUT) { //start thread di timeout
                    timeout.start();
                }else if (giocatori.size() == CostantiSistema.NUM_GOCATORI_MAX) {
                    fineGiocatoriAccettati();
                    timeout.interrupt(); //killa il thread di timeout
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
        flushaMappa();
        creaEAvviaPartita();
    }

    private void flushaMappa() {
        giocatori = new HashMap<>();
    }

    private void creaEAvviaPartita() {
        Partita nuovaPartita = new Partita();
        //TODO costruisci il model
        //TODO costruisci le proxyview
        executors.submit(new HandlerPartita(nuovaPartita));//anche parametro InterfacciaView
    }

    public static void main(String[] args) {
        InstauraConnessioni a = new InstauraConnessioni();
        a.startServer();
    }
}
