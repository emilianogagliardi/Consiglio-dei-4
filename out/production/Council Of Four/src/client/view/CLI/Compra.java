package client.view.CLI;


import classicondivise.IdVendibile;
import classicondivise.NomeCittà;
import classicondivise.Vendibile;
import classicondivise.carte.CartaPermessoCostruzione;
import interfaccecondivise.InterfacciaController;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Compra implements Runnable {
    private Scanner in;
    private volatile boolean fine;
    private InterfacciaController controller;
    private CLIView cliView;
    private List<Vendibile> vendibili;

    public Compra(InterfacciaController controller, CLIView cliView, List<Vendibile> vendibili){
        this.controller = controller;
        this.cliView = cliView;
        this.vendibili = vendibili;
        in = new Scanner(System.in);
    }

    @Override
    public void run() {
        String inputLine;
        fine = false;
        List<Vendibile> vendibiliDaComprare = new ArrayList<>();
        int input;
        try{
            do {
                System.out.println("\nChe cosa vuoi fare?");
                System.out.println("1: Compra");
                System.out.println("2: Passa turno");

                inputLine = in.nextLine();
                switch (inputLine) {
                    case "1":
                        int i = 0;
                        System.out.println("Inserisci il numero identificativo dei vendibili che vuoi comprare e poi scrivi \"fine\"");
                        for (Vendibile vendibile : vendibili) {
                            i++;
                            if (vendibile.getIdGiocatore() == cliView.getIdGiocatore()) {
                                continue;
                            }
                            switch (vendibile.getIdVendibile()){
                                case CARTE_PERMESSO_COSTRUZIONE:
                                    System.out.println(i + " :Carte permesso di costruzione; prezzo " + vendibile.getPrezzo() + "; venduto da giocatore " + vendibile.getIdGiocatore());
                                    List<CartaPermessoCostruzione> cartePermessoCostruzione = (List<CartaPermessoCostruzione>) vendibile.getOggetto();
                                    for (CartaPermessoCostruzione cartaPermessoCostruzione : cartePermessoCostruzione) {
                                        for (NomeCittà nomeCittà : cartaPermessoCostruzione.getCittà()){
                                            System.out.print(" " + nomeCittà);
                                        }
                                        System.out.println();
                                    }
                                    System.out.println();
                                    break;
                                case CARTE_POLITICA:
                                    System.out.println(i + " :Carte politica; prezzo " + vendibile.getPrezzo() + "; venduto da giocatore " + vendibile.getIdGiocatore());
                                    List<String> cartePolitica = (List<String>) vendibile.getOggetto();
                                    for (String colore : cartePolitica){
                                        System.out.println(colore);
                                    }
                                    break;
                                case AIUTANTI:
                                    System.out.println(i + " :Aiutanti; prezzo " + vendibile.getPrezzo() + "; venduto da giocatore " + vendibile.getIdGiocatore());
                                    System.out.println("Numero aiutanti: " + vendibile.getOggetto());
                                    break;
                                default:
                                    break;
                            }
                        }
                        inputLine = in.nextLine();
                        while (!inputLine.equalsIgnoreCase("fine")) {
                            try{
                                input =  Integer.parseInt(inputLine) - 1;
                            } catch (NumberFormatException exc){
                                break;
                            }
                            if (input >= 0 && input <= (vendibili.size() - 1)) {
                                vendibiliDaComprare.add(vendibili.get(input));
                            }
                            inputLine = in.nextLine();
                        }
                        if (vendibiliDaComprare.size() > 0) {
                            if (!fine) {
                                controller.compra(vendibiliDaComprare);
                            }
                        }
                        break;
                    case "2":
                        if (!fine) {
                            controller.passaTurno();
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

    void stop(){
        fine = true;
    }

}
