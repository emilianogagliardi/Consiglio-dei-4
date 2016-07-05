package client.view.CLI;


import classicondivise.Colore;
import classicondivise.IdBalcone;
import classicondivise.NomeCittà;
import classicondivise.carte.CartaPermessoCostruzione;
import client.view.SocketProxyController;
import interfaccecondivise.InterfacciaController;
import server.model.Città;
import server.model.NomeRegione;

import java.rmi.RemoteException;
import java.util.*;

class EseguiTurno implements Runnable {
    private Scanner in;
    private volatile boolean fine;
    private InterfacciaController controller;
    private CLIView cliView;

    EseguiTurno(InterfacciaController controller, CLIView cliView){
        this.controller = controller;
        this.cliView = cliView;
        in = new Scanner(System.in);
    }

    @Override
    public void run() {
        String inputLine;
        fine = false;
        try{
            do {
                System.out.println("\nChe cosa vuoi fare?");
                System.out.println("1: Esegui azione");
                System.out.println("2: Passa turno");
                System.out.println("3: logout");

                inputLine = in.nextLine();
                switch (inputLine) {
                    case "1":
                        System.out.println("Vuoi eseguire un'azione veloce o principale? (V) o (P)");

                        inputLine = in.nextLine();
                        switch (inputLine) {
                            case "V":
                                sceltaAzioneVeloce();
                                break;
                            case "P":
                                sceltaAzionePrincipale();
                                break;
                            default:
                                break;
                        }
                        break;
                    case "2":
                        if (!fine) {
                            controller.passaTurno();
                        }
                        fine = true;
                        break;
                    case "3":
                        in.close();
                        if (!fine) {
                            controller.logout(cliView.getIdGiocatore());
                        }
                        fine = true;
                        break;
                    default:
                        break;
                }
            } while (!fine);
        } catch (RemoteException exc){
            exc.printStackTrace();
        }
    }

    private void sceltaAzioneVeloce(){
        String inputLine;
        System.out.println("Scegli un'azione veloce");
        System.out.println("1: Ingaggiare un aiutante");
        System.out.println("2: Cambiare le tessere permesso di costruzione");
        System.out.println("3: Mandare un aiutante ad eleggere un consigliere");
        System.out.println("4: Compiere un'azione principale aggiuntiva");

        try {
            inputLine = in.nextLine();
            switch (inputLine) {
                case "1":
                    if (!fine)
                        controller.ingaggiareAiutante();
                    break;
                case "2":
                    stampaCartePermessoCostruzione();
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
                    if (!fine) {
                        stampaTuttiBalconi();
                        stampaConsiglieriRiserva();
                        controller.mandareAiutanteEleggereConsigliere(inserimentoIdBalconeEleggereUnConsigliere(), inserimentoConsigliereRiserva());
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
        } catch (RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void stampaConsiglieriRiserva(){
        System.out.println("Riserva consiglieri:");
        for (String colore : cliView.getRiservaConsiglieri()) {
            System.out.print("  " + colore);
        }
        System.out.println();
    }

    private void stampaCartePermessoCostruzione(){
        HashMap<String, List<CartaPermessoCostruzione>> mappa = cliView.getMappaCartePermessoRegione();
        for(Map.Entry<String, List<CartaPermessoCostruzione>> entry : mappa.entrySet()) {
            String key = entry.getKey();
            List<CartaPermessoCostruzione> value = entry.getValue();
            System.out.println(key + ":");
            System.out.print("Carta 1:");
            CartaPermessoCostruzione c1 = value.get(0);
            if (c1 != null) {
                for (NomeCittà città : c1.getCittà()) {
                    System.out.print("  " + città);
                }
                System.out.println();
            } else System.out.println("Carta 1 non disponibile!");
            System.out.print("Carta 2:");
            CartaPermessoCostruzione c2 = value.get(1);
            if (c2 != null) {
                for (NomeCittà città : c2.getCittà()) {
                    System.out.print("  " + città);
                }
                System.out.println();
            } else System.out.println("Carta 2 non disponibile!");
        }
    }

    private void sceltaAzionePrincipale(){
        String inputLine, idBalcone, coloreConsigliereRiserva, cittàCostruzione;
        int numeroCartaPermesso;
        List<String> listaCartePolitica;
        CartaPermessoCostruzione cartaPermessoCostruzione;
        System.out.println("Scegli un'azione principale");
        System.out.println("1: Eleggere un consigliere");
        System.out.println("2: Acquistare una tessera permesso di costruzione");
        System.out.println("3: Costruire un emporio usando una tessera permesso");
        System.out.println("4: Costruire un emporio con l'aiuto del re");

        try {
            inputLine = in.nextLine();
            switch (inputLine) {
                case "1":
                    stampaTuttiBalconi();
                    stampaConsiglieriRiserva();
                    idBalcone = inserimentoIdBalconeEleggereUnConsigliere();
                    coloreConsigliereRiserva = inserimentoConsigliereRiserva();
                    if (!fine){
                        controller.eleggereConsigliere(idBalcone, coloreConsigliereRiserva);
                    }
                    break;
                case "2":
                    stampaBalconiECartePermesso();
                    stampaCartePoliticaGiocatore();
                    idBalcone = inserimentoIdBalconeAcquistareTesseraPermesso();
                    listaCartePolitica = inserimentoCartePolitica();
                    numeroCartaPermesso = inserimentoNumeroCartaPermesso();
                    if (!fine) {
                        controller.acquistareTesseraPermessoCostruzione(idBalcone, listaCartePolitica, numeroCartaPermesso);
                    }
                    break;
                case "3":
                    if (!fine) {
                        cartaPermessoCostruzione = inserimentoCartaPermessoCostruzione();
                        if (cartaPermessoCostruzione == null){
                            break;
                        }
                        controller.costruireEmporioConTesseraPermessoCostruzione(cartaPermessoCostruzione, inserimentoCittà());
                    }
                    break;
                case "4":
                    System.out.println("Balcone del re:");
                    for (String colore : cliView.getMappaBalconi().get("RE")) {
                        System.out.print(" " + colore);
                    }
                    System.out.println();
                    System.out.println("Carte politica:");
                    for (String colore : cliView.getManoCartePolitica()) {
                        System.out.print(" " + colore);
                    }
                    System.out.println();
                    listaCartePolitica = inserimentoCartePolitica();
                    cittàCostruzione = inserimentoCittà();
                    if (!fine) {
                        controller.costruireEmporioConAiutoRe(listaCartePolitica, cittàCostruzione);
                    }
                    break;
                default:
                    break;
            }
        } catch (RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void stampaTuttiBalconi(){
        System.out.println("Balconi:");
        System.out.print("COSTA:");
        List<String> lista = cliView.getMappaBalconi().get(IdBalcone.COSTA.toString());
        for (String colore : lista) {
            System.out.print("  " + colore);
        }
        System.out.println();
        System.out.print("COLLINA:");
        lista = cliView.getMappaBalconi().get(IdBalcone.COLLINA.toString());
        for (String colore : lista) {
            System.out.print("  " + colore);
        }
        System.out.println();
        System.out.print("MONTAGNA:");
        lista = cliView.getMappaBalconi().get(IdBalcone.MONTAGNA.toString());
        for (String colore : lista) {
            System.out.print("  " + colore);
        }
        System.out.println();
        System.out.print("RE:");
        lista = cliView.getMappaBalconi().get(IdBalcone.RE.toString());
        for (String colore : lista) {
            System.out.print("  " + colore);
        }
        System.out.println();
    }


    private void stampaBalconiECartePermesso(){
        System.out.println("Balconi:");
        System.out.print("COSTA:");
        List<String> lista = cliView.getMappaBalconi().get(IdBalcone.COSTA.toString());
        for (String colore : lista) {
            System.out.print("  " + colore);
        }
        CartaPermessoCostruzione c1 = cliView.getMappaCartePermessoRegione().get(IdBalcone.COSTA.toString()).get(0);
        CartaPermessoCostruzione c2 = cliView.getMappaCartePermessoRegione().get(IdBalcone.COSTA.toString()).get(1);
        System.out.print("  Carta 1:");
        for (NomeCittà nomeCittà : c1.getCittà()) {
            System.out.print("  " + nomeCittà);
        }
        System.out.print("  Carta 2:");
        for (NomeCittà nomeCittà : c2.getCittà()) {
            System.out.print("  " + nomeCittà);
        }
        System.out.println();
        System.out.print("COLLINA:");
        lista = cliView.getMappaBalconi().get(IdBalcone.COLLINA.toString());
        for (String colore : lista) {
            System.out.print("  " + colore);
        }
        c1 = cliView.getMappaCartePermessoRegione().get(IdBalcone.COLLINA.toString()).get(0);
        c2 = cliView.getMappaCartePermessoRegione().get(IdBalcone.COLLINA.toString()).get(1);
        System.out.print("  Carta 1:");
        for (NomeCittà nomeCittà : c1.getCittà()) {
            System.out.print("  " + nomeCittà);
        }
        System.out.print("  Carta 2:");
        for (NomeCittà nomeCittà : c2.getCittà()) {
            System.out.print("  " + nomeCittà);
        }
        System.out.println();
        System.out.print("MONTAGNA:");
        lista = cliView.getMappaBalconi().get(IdBalcone.MONTAGNA.toString());
        for (String colore : lista) {
            System.out.print("  " + colore);
        }
        c1 = cliView.getMappaCartePermessoRegione().get(IdBalcone.MONTAGNA.toString()).get(0);
        c2 = cliView.getMappaCartePermessoRegione().get(IdBalcone.MONTAGNA.toString()).get(1);
        System.out.print("  Carta 1:");
        for (NomeCittà nomeCittà : c1.getCittà()) {
            System.out.print("  " + nomeCittà);
        }
        System.out.print("  Carta 2:");
        for (NomeCittà nomeCittà : c2.getCittà()) {
            System.out.print("  " + nomeCittà);
        }
        System.out.println();
    }

    private void stampaCartePoliticaGiocatore(){
        System.out.println("Carte politica:");
        List<String> lista = cliView.getManoCartePolitica();
        for (String colore : lista) {
            System.out.print("  " + colore);
        }
        System.out.println();
    }

    private CartaPermessoCostruzione inserimentoCartaPermessoCostruzione(){
        int i = 0, input;
        String inputLine;
        List<CartaPermessoCostruzione> lista;
        try{
            lista = cliView.getMappaCartePermessoCostruzioneGiocatori().get(cliView.getIdGiocatore());
            if (lista.size() == 0) {
                System.out.println("Non hai carte permesso di costruzione!");
                return null;
            }
            System.out.println("Scegli la carta permesso che vuoi utilizzare oppure scrivi un numero non presente in lista per annullare la mossa\n");
            for (CartaPermessoCostruzione cartaPermessoCostruzione : lista){
                i++;
                System.out.print(i + ":");
                for (NomeCittà nomeCittà : cartaPermessoCostruzione.getCittà()){
                    System.out.print(" " + nomeCittà);
                }
                System.out.print("\n");
            }
            inputLine = in.nextLine();
            try{
                input =  Integer.parseInt(inputLine) - 1;
            } catch (NumberFormatException exc){
                return null;
            }
            if (input >= 0 && input <= (lista.size() - 1)) {
                return lista.get(input);
            }
        } catch (RemoteException exc){
            exc.printStackTrace();
        }
        return null;
    }

    private String inserimentoCittà(){
        String inputLine, città;
        System.out.println("Inserisci l'iniziale maiuscola del nome della città: \n    ARKON,\n" +
                "    BURGEN,\n" +
                "    CASTRUM,\n" +
                "    DORFUL,\n" +
                "    ESTI,\n" +
                "    FRAMEK,\n" +
                "    GRADEN,\n" +
                "    HELLAR,\n" +
                "    INDUR,\n" +
                "    JUVELAR,\n" +
                "    KULTOS,\n" +
                "    LYRAM,\n" +
                "    MERKATIM,\n" +
                "    NARIS,\n" +
                "    OSIUM.");
        inputLine = in.nextLine();
        switch (inputLine) {
            case "A":
                città = NomeCittà.ARKON.toString();
                break;
            case "B":
                città = NomeCittà.BURGEN.toString();
                break;
            case "C":
                città = NomeCittà.CASTRUM.toString();
                break;
            case "D":
                città = NomeCittà.DORFUL.toString();
                break;
            case "E":
                città = NomeCittà.ESTI.toString();
                break;
            case "F":
                città = NomeCittà.FRAMEK.toString();
                break;
            case "G":
                città = NomeCittà.GRADEN.toString();
                break;
            case "H":
                città = NomeCittà.HELLAR.toString();
                break;
            case "I":
                città = NomeCittà.INDUR.toString();
                break;
            case "J":
                città = NomeCittà.JUVELAR.toString();
                break;
            case "K":
                città = NomeCittà.KULTOS.toString();
                break;
            case "L":
                città = NomeCittà.LYRAM.toString();
                break;
            case "M":
                città = NomeCittà.MERKATIM.toString();
                break;
            case "N":
                città = NomeCittà.NARIS.toString();
                break;
            case "O":
                città = NomeCittà.OSIUM.toString();
                break;
            default:
                città = "";
                break;
        }
        return città;
    }

    private String inserimentoIdBalconeEleggereUnConsigliere(){
        String inputLine, idBalcone;
        System.out.println("Inserisci il nome della regione del balcone (CT, CL o M) oppure RE per il balcone del re dove desideri inserire il consigliere");
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
        return idBalcone;
    }

    private String inserimentoIdBalconeAcquistareTesseraPermesso(){
        String inputLine, idBalcone;
        System.out.println("Inserisci il nome della regione del balcone (CT, CL o M) che vuoi soddisfare");
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
            default:
                idBalcone = "";
                break;
        }
        return idBalcone;
    }

    private String inserimentoConsigliereRiserva() {
        System.out.println("Inserisci il colore del consigliere che vuoi prelevare dalla riserva: viola (V), azzurro (AZ), nero (N), rosa (R), arancione (AR) o bianco (B)");
        String inputLine, coloreConsigliere;
        inputLine = in.nextLine();
        switch (inputLine) {
            case "V":
                coloreConsigliere = Colore.VIOLA.toString();
                break;
            case "AZ":
                coloreConsigliere = Colore.AZZURRO.toString();
                break;
            case "N":
                coloreConsigliere = Colore.NERO.toString();
                break;
            case "R":
                coloreConsigliere = Colore.ROSA.toString();
                break;
            case "AR":
                coloreConsigliere = Colore.ARANCIONE.toString();
                break;
            case "B":
                coloreConsigliere = Colore.BIANCO.toString();
                break;
            default:
                coloreConsigliere = "";
                break;
        }
        return coloreConsigliere;
    }

    private List<String> inserimentoCartePolitica(){
        System.out.println("Inserisci i colori delle carte politica separati da un INVIO e poi scrivi \"fine\": viola (V), azzurro (AZ), nero (N), rosa (R), arancione (AR), bianco (B) o jolly (J)");
        String inputLine;
        ArrayList<String> lista = new ArrayList<>();
        do{
            inputLine = in.nextLine();
            switch (inputLine) {
                case "V":
                    lista.add(Colore.VIOLA.toString());
                    break;
                case "AZ":
                    lista.add(Colore.AZZURRO.toString());
                    break;
                case "N":
                    lista.add(Colore.NERO.toString());
                    break;
                case "R":
                    lista.add(Colore.ROSA.toString());
                    break;
                case "AR":
                    lista.add(Colore.ARANCIONE.toString());
                    break;
                case "B":
                    lista.add(Colore.BIANCO.toString());
                    break;
                case "J":
                    lista.add(Colore.JOLLY.toString());
                    break;
                default:
                    break;
            }
        } while (!inputLine.equals("fine"));
        return lista;
    }

    private int inserimentoNumeroCartaPermesso(){
        String inputLine;
        System.out.println("Inserisci il numero di carta permesso: 1 o 2");
        inputLine = in.nextLine();
        try{
            return Integer.parseInt(inputLine);
        } catch (NumberFormatException exc){
            return -1;
        }
    }

    void stop(){
        fine = true;
    }


}
