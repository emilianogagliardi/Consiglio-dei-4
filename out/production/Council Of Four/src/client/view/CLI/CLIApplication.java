package client.view.CLI;


import client.view.CostantiClient;
import client.view.SocketPollingView;
import interfaccecondivise.InterfacciaLoggerRMI;
import server.sistema.CostantiSistema;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class CLIApplication {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input;
        Socket socket;

        System.out.println("Specifica il tipo di connessione che vuoi utilizzare. Socket (S) o RMI (R)");
        input = in.nextLine();
        if (input.equalsIgnoreCase("S")){
            try {
                socket = new Socket(CostantiClient.IP_SERVER, CostantiSistema.SOCKET_PORT);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                CLIView cliView = new CLIView("S");
                cliView.setObjectStream(oos);
                SocketPollingView socketPollingView = new SocketPollingView(cliView, ois); //crea SocketPollingView che riceverà richieste da SocketProxyView nel server
                new Thread(socketPollingView).start(); //necessario alla comunicazione server -> client

            } catch (IOException exc) {
                exc.printStackTrace();
            }
        } else if (input.equalsIgnoreCase("R")) {
            try {
                Registry registry = LocateRegistry.getRegistry(CostantiClient.IP_SERVER, CostantiClient.REGISTRY_PORT);
                InterfacciaLoggerRMI loggerRMI = (InterfacciaLoggerRMI) registry.lookup(CostantiClient.CHIAVE_LOGGER);
                CLIView cliView = new CLIView("R");
                cliView.setLoggerRMI(loggerRMI);
                loggerRMI.login(cliView); //passa la view per rendere possibile la comunicazione server -> client
            } catch (RemoteException | NotBoundException exc) {
                exc.printStackTrace();
            }
        }
    }
}
