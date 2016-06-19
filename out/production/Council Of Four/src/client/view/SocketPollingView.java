package client.view;

import classicondivise.ComunicazioneView;
import client.view.GUI.GUIView;
import interfaccecondivise.InterfacciaView;

import java.io.IOException;
import java.io.ObjectInputStream;

/*
    codice di un thread che fa polling sul socket. Riceve oridini e chiama metodi sulla view.
    Rende trasparente nel caso di comunicazone socket la ricezione di ordini dal server remoto.
 */
public class SocketPollingView implements Runnable {
    private InterfacciaView view;
    ObjectInputStream ois;
    private volatile boolean running = true;

    public SocketPollingView(GUIView view, ObjectInputStream ois) {
        this.view = view;
        this.ois = ois;
    }
/*
    public void termina(){ //TODO: il chiamante deve chiamare questo metodo per far terminare il thread
        try {
            this.socket.close(); //probabilmente questa istruzione non serve perchè il socket viene chiuso automaticamente nel try with resources
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        running = false;
    }
*/
    @Override
    public void run() {
        try {
            String inputLine;
            int idMappa, idGiocatore;
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
                            //riceve l'id della mappa scelta dal server
                            idMappa = ois.readInt();
                            view.iniziaAGiocare(idMappa);
                            break;
                        case SET_ID_GIOCATORE:
                            idGiocatore = ois.readInt();
                            view.setIdGiocatore(idGiocatore);
                            break;
                        case GET_ID_GIOCATORE:
                           //ci pensa SocketProxyView a restituire l'IDGiocatore
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
