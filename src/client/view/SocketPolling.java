package client.view;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by emilianogagliardi on 13/06/16.
 */

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
            String daControllerServer = in.nextLine();
            switch (daControllerServer){
                //TODO ricezione e servizio degli ordini da server, chiamando i metodi sulla view
            }
        }
    }
}
