package client.view.CLI;


import classicondivise.IdVendibile;
import classicondivise.NomeCittà;
import classicondivise.Vendibile;
import classicondivise.carte.CartaPermessoCostruzione;
import interfaccecondivise.InterfacciaController;
import server.model.carte.CartaPolitica;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Vendi implements Runnable {
    private Scanner in;
    private volatile boolean fine;
    private InterfacciaController controller;
    private CLIView cliView;

    public Vendi(InterfacciaController controller, CLIView cliView){
        this.controller = controller;
        this.cliView = cliView;
        in = new Scanner(System.in);
    }

    @Override
    public void run() {
        String inputLine;
        fine = false;
        List<Vendibile> vendibili;
        try{
            do {
                System.out.println("\nChe cosa vuoi fare?");
                System.out.println("1: Vendi");
                System.out.println("2: Passa turno");
                System.out.println("3: logout");

                inputLine = in.nextLine();
                switch (inputLine) {
                    case "1":
                        System.out.println("Vuoi vendere carte permesso di costruzione (CPC), carte politica (CPO) oppure aiutanti (A)?");

                        inputLine = in.nextLine();
                        switch (inputLine) {
                            case "CPC":
                                vendibili = sceltaCartePermesso();
                                if (vendibili != null) {
                                    if (!fine) {
                                        controller.vendi(vendibili);
                                    }
                                } else System.out.println("Hai già messo in vendita carte permesso di costruzione!");
                                break;
                            case "CPO":
                                vendibili = sceltaCartePolitica();
                                if (vendibili != null) {
                                    if (!fine) {
                                        controller.vendi(vendibili);
                                    }
                                } else System.out.println("Hai già messo in vendita carte politica!");
                                break;
                            case "A":
                                vendibili = sceltaAiutanti();
                                if (vendibili != null) {
                                    if (!fine) {
                                        controller.vendi(vendibili);
                                    }
                                } else System.out.println("Hai già messo in vendita aiutanti!");
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

   private List<Vendibile> sceltaCartePermesso(){
           int i = 0, input, prezzo;
           String inputLine;
           List<CartaPermessoCostruzione> lista;
           try{
               lista = cliView.getMappaCartePermessoCostruzioneGiocatori().get(cliView.getIdGiocatore());
               if (lista.size() == 0) {
                   System.out.println("Non hai carte permesso di costruzione!");
                   return null;
               }
               List<Vendibile> vendibili = new ArrayList<>();
               List<CartaPermessoCostruzione> cartePermessoCostruzioneDaVendere = new ArrayList<>();
               System.out.println("Scegli le carte permesso che vuoi vendere e poi scrivi \"fine\"\n");
               for (CartaPermessoCostruzione cartaPermessoCostruzione : lista){
                   i++;
                   System.out.print(i + ":");
                   for (NomeCittà nomeCittà : cartaPermessoCostruzione.getCittà()){
                       System.out.print(" " + nomeCittà);
                   }
                   System.out.println();
               }
               inputLine = in.nextLine();
               while (!inputLine.equals("fine")) {
                   try{
                       input =  Integer.parseInt(inputLine) - 1;
                   } catch (NumberFormatException exc){
                       break;
                   }
                   if (input >= 0 && input <= (lista.size() - 1)) {
                       cartePermessoCostruzioneDaVendere.add(lista.get(input));
                   }
                   inputLine = in.nextLine();
               }
               if (cartePermessoCostruzioneDaVendere.size() != 0) {
                   System.out.print("Scegli il prezzo di vendita: ");
                   inputLine = in.nextLine();
                   try{
                       prezzo =  Integer.parseInt(inputLine);
                   } catch (NumberFormatException exc){
                       return null;
                   }
                   vendibili.add(new Vendibile(cartePermessoCostruzioneDaVendere, prezzo, cliView.getIdGiocatore(), IdVendibile.CARTE_PERMESSO_COSTRUZIONE));
                   return vendibili;
               } else return null;
           } catch (RemoteException exc){
               exc.printStackTrace();
           }
           return null;
   }

    private List<Vendibile> sceltaCartePolitica(){
        int i = 0, input, prezzo;
        String inputLine;
        List<String> lista;
        try{
            lista = cliView.getManoCartePolitica();
            if (lista.size() == 0) {
                System.out.println("Non hai carte politica!");
                return null;
            }
            List<Vendibile> vendibili = new ArrayList<>();
            List<String> cartePoliticaDaVendere = new ArrayList<>();
            System.out.println("Scegli le carte politica che vuoi vendere e poi scrivi \"fine\"\n");
            for (String cartaPolitica : lista){
                i++;
                System.out.println(i + ": " + cartaPolitica);
            }
            inputLine = in.nextLine();
            while (!inputLine.equals("fine")) {
                try{
                    input =  Integer.parseInt(inputLine) - 1;
                } catch (NumberFormatException exc){
                    break;
                }
                if (input >= 0 && input <= (lista.size() - 1)) {
                    cartePoliticaDaVendere.add(lista.get(input));
                }
                inputLine = in.nextLine();
            }
            if (cartePoliticaDaVendere.size() != 0) {
                System.out.print("Scegli il prezzo di vendita: ");
                inputLine = in.nextLine();
                try{
                    prezzo =  Integer.parseInt(inputLine);
                } catch (NumberFormatException exc){
                    return null;
                }
                vendibili.add(new Vendibile(cartePoliticaDaVendere, prezzo, cliView.getIdGiocatore(), IdVendibile.CARTE_POLITICA));
                return vendibili;
            } else return null;
        } catch (RemoteException exc){
            exc.printStackTrace();
        }
        return null;
    }

    private List<Vendibile> sceltaAiutanti(){
        int prezzo, aiutanti, aiutantiDaVendere;
        String inputLine;
        try{
            aiutanti = cliView.getMappaAiutantiGiocatori().get(cliView.getIdGiocatore());
            if (aiutanti == 0) {
                System.out.println("Non hai aiutanti!");
                return null;
            }
            List<Vendibile> vendibili = new ArrayList<>();
            System.out.println("Scrivi il numero di aiutanti che vuoi vendere:");
            inputLine = in.nextLine();
            try{
                aiutantiDaVendere =  Integer.parseInt(inputLine);
            } catch (NumberFormatException exc){
                return null;
            }
            if (aiutantiDaVendere != 0) {
                System.out.print("Scegli il prezzo di vendita: ");
                inputLine = in.nextLine();
                try{
                    prezzo =  Integer.parseInt(inputLine);
                } catch (NumberFormatException exc){
                    return null;
                }
                vendibili.add(new Vendibile(aiutantiDaVendere, prezzo, cliView.getIdGiocatore(), IdVendibile.AIUTANTI));
                return vendibili;
            } else return null;
        } catch (RemoteException exc){
            exc.printStackTrace();
        }
        return null;
    }


    void stop(){
        fine = true;
    }

}

