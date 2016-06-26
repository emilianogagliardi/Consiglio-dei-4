package client.view.CLI;


import classicondivise.IdBalcone;
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

    void setController(InterfacciaController controller){
        this.controller = controller;
    }

    @Override
    public void run() {
        String inputLine, idBalcone, coloreConsigliere;
        fine = false;
        try {
            do {
                System.out.println("Che cosa vuoi fare?");
                System.out.println("1: Vedere informazioni partita");
                System.out.println("2: Esegui azione");
                System.out.println("3: Abbandona la partita");

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
                                    case "3":
                                        System.out.println("Inserisci il nome della regione del balcone (CT, CL o M) oppure RE per il balcone del re");
                                        System.out.println("Poi inserisci il colore del consigliere che vuoi prelevare dalla riserva: viola (V), azzurro (AZ), nero (N), rosa (R), arancione (AR) o bianco (B)");
                                        inputLine = in.nextLine();
                                        switch (inputLine) {
                                            case "CT":
                                                idBalcone = IdBalcone.COSTA.toString();
                                                break;
                                            case "CL":
                                                idBalcone = IdBalcone.COLLINA.toString();
                                                break;
                                            case "M":
                                                idBalcone = IdBalcone.MONTAGNA.toString();
                                                break;
                                            case "RE":
                                                idBalcone = IdBalcone.RE.toString();
                                                break;
                                            default:
                                                idBalcone = "";
                                                break;
                                        }
                                        inputLine = in.nextLine();
                                        switch (inputLine) {
                                            case "V":
                                               coloreConsigliere = "VIOLA";
                                                break;
                                            case "AZ":
                                                coloreConsigliere = "AZZURRO";
                                                break;
                                            case "N":
                                                coloreConsigliere = "NERO";
                                                break;
                                            case "R":
                                                coloreConsigliere = "ROSA";
                                                break;
                                            case "AR":
                                                coloreConsigliere = "ARANCIONE";
                                                break;
                                            case "B":
                                                coloreConsigliere = "BIANCO";
                                                break;
                                            default:
                                                coloreConsigliere = "";
                                                break;
                                        }
                                        if (!fine) {
                                            controller.mandareAiutanteEleggereConsigliere(idBalcone, coloreConsigliere);
                                        }
                                        break;
                                    case "4":
                                        if (!fine) {
                                            controller.compiereAzionePrincipaleAggiuntiva();
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
                    case "3":
                        //TODO: logout
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
