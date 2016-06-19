package client.view;

import classicondivise.ComunicazioneView;
import client.view.GUI.GUIView;
import interfaccecondivise.InterfacciaView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

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
                    //TODO viene lanciata un eccezione
                    /*
                    java.io.EOFException
	                at java.io.ObjectInputStream$BlockDataInputStream.peekByte(ObjectInputStream.java:2608)
	                at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1319)
	                at java.io.ObjectInputStream.readObject(ObjectInputStream.java:371)
	                at client.view.SocketPollingView.run(SocketPollingView.java:42)
	                at java.lang.Thread.run(Thread.java:745)
                     */
                    inputLine = (String) ois.readObject();
                    comunicazioneView = ComunicazioneView.valueOf(inputLine);
                    switch (comunicazioneView) {
                        case SCEGLI_MAPPA:
                            view.scegliMappa();
                            break;
                        case INIZIA_A_GIOCARE:
                            //riceve l'id della mappa scelta dal server
                            int idMappa = ois.readInt();
                            view.iniziaAGiocare(idMappa);
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
