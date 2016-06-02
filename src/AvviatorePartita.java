
import controller.Controller;
import model.Partita;
import proxyview.InterfacciaView;
import proxyview.RMIProxyView;
import proxyview.SocketProxyView;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AvviatorePartita implements Runnable {
    private HashMap<Integer, Socket> sockets;
    private HashMap<Integer, String> modiComunicazione;
    private static ExecutorService executors = Executors.newCachedThreadPool();

    public AvviatorePartita(HashMap<Integer, Socket> sockets, HashMap<Integer, String> modiComunicazione) {
        this.modiComunicazione = modiComunicazione;
        this.sockets = sockets;
    }

    @Override
    public void run() {
        HashMap<Integer, InterfacciaView> views = new HashMap<>();
        //crea la mappa di proxy view
        modiComunicazione.forEach((k, s) -> {
            if (s.equals("RMI")) views.put(k, new RMIProxyView());
            else views.put(k, new SocketProxyView());
        });
        Partita partita = new Partita();
        executors.submit(new Controller(partita, views));
    }
}
