package client.view;

import classicondivise.ComunicazioneView;
import client.view.GUI.GUIView;
import interfaccecondivise.InterfacciaView;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

/*
    codice di un thread che fa polling sul socket. Riceve oridini e chiama metodi sulla view.
    Rende trasparente nel caso di comunicazone socket la ricezione di ordini dal server remoto.
 */
public class SocketPollingView implements Runnable {
    InterfacciaView view;
    private Socket socket;
    private volatile boolean running = true;

    public SocketPollingView(GUIView view, Socket socket) {
        this.view = view;
        this.socket = socket;
    }

    public void termina(){ //TODO: il chiamante deve chiamare questo metodo per far terminare il thread
        try {
            this.socket.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        running = false;
    }

    @Override
    public void run() {
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            String inputLine;
            ComunicazioneView comunicazioneView;
            while (running) {
                try{
                    inputLine = (String) ois.readObject();
                    comunicazioneView = ComunicazioneView.valueOf(inputLine);
                    switch (comunicazioneView) {
                        case SCEGLI_MAPPA:
                            view.scegliMappa();
                            break;
                        case INIZIA_A_GIOCARE:
                            view.iniziaAGiocare();
                            break;
                        default:
                            break;
                    }
                } catch (ClassNotFoundException exc){
                    exc.printStackTrace();
                }
            }
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }
}
