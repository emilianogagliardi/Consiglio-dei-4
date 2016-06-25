package client.view.CLI;


import java.util.Scanner;

class EseguiTurno implements Runnable {
    private Scanner in;
    private static EseguiTurno istanza;
    private boolean fine;

    private EseguiTurno(){
        in = new Scanner(System.in);
    }

    public static EseguiTurno getIstanza(){
        if (istanza == null){
            return  new EseguiTurno();
        } else return istanza;
    }

    @Override
    public void run() {
        String inputLine;
        fine = false;
        do{
            System.out.println("Che cosa vuoi fare?");
            System.out.println("1: Vedere informazioni partita");
            System.out.println("2: Esegui azione");
            System.out.println("logut");

            inputLine = in.nextLine();
            switch (inputLine){
                case "1":
                    break;
                case "2":
                    break;
                case "logout":
                    fine = true;
                    break;
            }

        } while(!fine);
    }
}
