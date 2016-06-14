package client.view;

import controller.InterfacciaController;

import java.net.Socket;

/**
 * Created by emilianogagliardi on 13/06/16.
 */

/*
questa classe serve ad essere passato come se fosse un controller quando il client apre una comunicazione
di tipo socket. Implementa la cominciazione necessaria all'esecuzione delle mosse nel server, rendendo
trasparente a controllerFXMosse il fatto che il controller sia in remoto, come nel caso di RMI
 */
public class SocketProxyController implements InterfacciaController {
    Socket socket;
    public SocketProxyController(Socket socket) {
        this.socket = socket;
    }
}
