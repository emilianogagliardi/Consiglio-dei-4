package client.view;

import client.view.GUI.GUIView;
import interfaccecondivise.InterfacciaView;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

/*
    codice di un thread che fa polling sul socket. Riceve oridini e chiama metodi sulla view.
    Rende trasparente nel caso di comunicazone socket la ricezione di ordini dal server remoto.
 */
public class SocketPolling implements Runnable{
    InterfacciaView view;
    Scanner in;

    public SocketPolling(GUIView view, Socket socket) {
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
                    try { //non è possibile che venga lanciata questa eccezzione, non è utilizzato rmi ma socket
                        view.scegliMappa();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
