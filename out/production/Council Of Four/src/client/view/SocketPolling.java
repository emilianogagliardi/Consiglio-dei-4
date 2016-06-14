package client.view;

import interfaccecondivise.InterfacciaView;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
    codice di un thread che fa polling sul socket. Riceve oridini e chiama metodi sulla view.
    Rende trasparente nel caso di comunicazone socket la ricezione di ordini dal server remoto.
 */
public class SocketPolling implements Runnable{
    InterfacciaView view;
    Scanner in;

    public SocketPolling(InterfacciaView view, Socket socket) {
        this.view = view;
        try {
            in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            String daServer = in.nextLine();
            switch (daServer){
                //TODO ricezione e servizio degli ordini da server, chiamando i metodi sulla view
                case "sceglimappa":
                    Platform.runLater(() -> view.scegliMappa()); //le modifiche sulla voiew possono essere fatte solo nel thread principale
                    break;
                default:
                    break;
            }
        }
    }
}
