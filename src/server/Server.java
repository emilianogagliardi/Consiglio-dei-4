package server;

import proxyView.InterfacciaView;
import proxyView.SocketProxyView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
    private ArrayList<InterfacciaView> proxyViews;
    private int idCorrente;
    private ScheduledExecutorService timeoutExecutor;
    private Thread timeoutThread;

    public Server() {
        proxyViews = new ArrayList<>();
        idCorrente = 0;
        timeoutExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void startServer() {
        try{
            LoggerRMI loggerRMI = new LoggerRMI(this);
            Registry registry = LocateRegistry.createRegistry(CostantiSistema.RMI_PORT);
            registry.bind(CostantiSistema.NOME_LOGGER_REG, loggerRMI);
        }catch (RemoteException e) {
            System.out.println("Impossibile creare e registrare loggerRMI");
            e.printStackTrace();
        }catch (AlreadyBoundException e) {
            System.out.println("impossibile fare bind di logger RMI");
            e.printStackTrace();
        }
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(CostantiSistema.SOCKET_PORT);
        }catch (IOException e) {
            System.out.println("Impossibile inizializzare server socket");
        }
        while (true) {
            try{
                Socket socket = serverSocket.accept();
                //comunica al client qual è il suo id e aggiunge la proxy view associata
                synchronized ((Object) idCorrente) {
                    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                    pw.println(idCorrente);
                    addView(new SocketProxyView(socket));
                }
            }catch (IOException e){
                System.out.println("impossibile creare socket da server socket");
            }catch (NullPointerException e) {
                System.out.println("server socket è null, impossibile eseguire accept");
                break;
            }
        }
    }

    public synchronized void addView(InterfacciaView view){
        view.setIdGiocatore(idCorrente);
        proxyViews.add(view);
        idCorrente++;
        if (proxyViews.size() == CostantiSistema.NUM_GIOCATORI_TIMEOUT) { //start thread di timeout
            timeoutThread = new ThreadTimeout(this);
            timeoutExecutor.schedule(timeoutThread, CostantiSistema.TIMEOUT_2_GIOCATORI, TimeUnit.SECONDS);
        }else if (proxyViews.size() == CostantiSistema.NUM_GOCATORI_MAX) {
            timeoutThread.interrupt(); //killa il thread di timeout
            fineGiocatoriAccettati();
        }
    }

    public void fineGiocatoriAccettati() {
        idCorrente = 0;
        AvviatorePartita avviatorePartita = new AvviatorePartita(proxyViews);
        new Thread(avviatorePartita).start();
    }

    public int getIdCorrente(){
        synchronized ((Object) idCorrente) {
            return idCorrente;
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}
