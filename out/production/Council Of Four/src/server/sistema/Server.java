package server.sistema;

import interfaccecondivise.InterfacciaView;
import server.proxyView.SocketProxyView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Server {
    private ArrayList<InterfacciaView> proxyViews;
    private int idCorrente;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> timeout;
    private int numeroChiaviCorrente;
    private ObjectInputStream ois;

    public Server() {
        proxyViews = new ArrayList<>();
        idCorrente = 0;
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startServer() {
        NumeroNomeChiaveRMI.init();
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
            e.printStackTrace();
        }
        while (true) {
            try{
                Socket socket = serverSocket.accept();
                //comunica al client qual è il suo id e aggiunge la proxy view associata
                synchronized ((Object) idCorrente) {
                    ois = new ObjectInputStream(socket.getInputStream());
                    addView(new SocketProxyView(socket, ois));
                }
            }catch (IOException e){
                System.out.println("impossibile creare socket da server socket");
            }catch (NullPointerException e) {
                System.out.println("server socket è null, impossibile eseguire accept");
                e.printStackTrace();
                break;
            }
        }
    }

    public synchronized void addView(InterfacciaView view){
        //TODO togliere questa riga
        System.out.println("Giocatore"+idCorrente+ " accettato");
        //se si sta aggiungendo il primo giocatore di una partita si richiede un nuovo numero per le chiavi rmi
        if(idCorrente == 0) numeroChiaviCorrente = NumeroNomeChiaveRMI.ottieniNuovoNumero();
        try {
            view.setIdGiocatore(idCorrente);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        proxyViews.add(view);
        idCorrente++;
        if (proxyViews.size() == CostantiSistema.NUM_GIOCATORI_TIMEOUT) { //start thread di timeout
            timeout = scheduler.schedule(() -> {fineGiocatoriAccettati();}, CostantiSistema.TIMEOUT_2_GIOCATORI, SECONDS);
        } else if (proxyViews.size() == CostantiSistema.NUM_GOCATORI_MAX) {
            timeout.cancel(true);
            fineGiocatoriAccettati();
        }
    }

    public void fineGiocatoriAccettati() {
        //TODO togliere questa riga
        System.out.println("Fine giocatori in partita");
        System.out.println();
        idCorrente = 0;
        AvviatorePartita avviatorePartita = new AvviatorePartita(proxyViews, numeroChiaviCorrente);
        new Thread(avviatorePartita).start();
        proxyViews = new ArrayList<>();
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
