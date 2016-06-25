package client.view.CLI;


import client.view.SocketProxyController;
import interfaccecondivise.InterfacciaController;
import server.model.NomeRegione;

import java.rmi.RemoteException;
import java.util.Scanner;

class EseguiTurno implements Runnable {
    private Scanner in;
    private static EseguiTurno istanza;
    private volatile boolean fine;
    private InterfacciaController controller;

    private EseguiTurno(){
        in = new Scanner(System.in);
    }

    static EseguiTurno getIstanza(){
        if (istanza == null){
            return  new EseguiTurno();
        } else return istanza;
    }

    void setSocketProxyController(InterfacciaController controller){
        this.controller = controller;
    }

    @Override
    public void run() {
        String inputLine, regione;
        fine = false;
        try {
            do {
                System.out.println("Che cosa vuoi fare?");
                System.out.println("1: Vedere informazioni partita");
                System.out.println("2: Esegui azione");
                System.out.println("logut");

                inputLine = in.nextLine();
                switch (inputLine) {
                    case "1":
                        break;
                    case "2":
                        System.out.println("Vuoi eseguire un'azione veloce o principale? (V) o (P)");

                        inputLine = in.nextLine();
                        switch (inputLine) {
                            case "V":
                                System.out.println("Scegli un'azione veloce");
                                System.out.println("1: Ingaggiare un aiutante");
                                System.out.println("2: Cambiare le tessere permesso di costruzione");
                                System.out.println("3: Mandare un aiutante ad eleggere un consigliere");
                                System.out.println("4: Compiere un'azione principale aggiuntiva");

                                inputLine = in.nextLine();
                                switch (inputLine) {
                                    case "1":
                                        if (!fine)
                                            controller.ingaggiareAiutante();
                                        break;
                                    case "2":
                                        System.out.println("Inserisci il nome della regione di cui vuoi cambiare le carte permesso di costruzione: COSTA(CT) o COLLINA(CL) o MONTAGNA(M)");
                                        inputLine = in.nextLine();
                                        switch (inputLine) {
                                            case "CT":
                                                if (!fine)
                                                    controller.cambiareTesserePermessoCostruzione(NomeRegione.COSTA.toString());
                                                break;
                                            case "CL":
                                                if (!fine) {
                                                    controller.cambiareTesserePermessoCostruzione(NomeRegione.COLLINA.toString());
                                                }
                                                break;
                                            case "M":
                                                if (!fine)
                                                    controller.cambiareTesserePermessoCostruzione(NomeRegione.MONTAGNA.toString());
                                                break;
                                            default:
                                                break;
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "P":
                                System.out.println("Scegli un'azione principale");
                                System.out.println("1: ...");
                                break;
                            default:
                                break;
                        }
                        break;
                    case "logout":
                        fine = true;
                        break;
                }
            } while (!fine);
        } catch (RemoteException exc) {
            exc.printStackTrace();
        }
    }

    public void stop(){
        fine = true;
    }
}
