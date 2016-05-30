import instauraconnessione.InstauraConnessioni;
import instauraconnessione.CostantiSistema;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class InstauraConnessioniTest {

    @Test
    public void aggiungiMaxGiocatoriTest() {
        InstauraConnessioni accettore = new InstauraConnessioni();
        new Thread(() -> accettore.startServer()).start(); //lambda expression, start server thread
        new ClientForTest("client1", "socket").startClient();
        new ClientForTest("client2", "RMI").startClient();
        new ClientForTest("client3", "socket").startClient();
        new ClientForTest("client4", "RMI").startClient();
        //TODO assert qualcosa
    }
}

class ClientForTest {
    private String nickname;
    private String connessione;
    private Socket s;
    private PrintWriter out;
    private Scanner in;

    public ClientForTest(String nickname, String connessione) {
        try {
            s = new Socket("127.0.0.1", CostantiSistema.PORT);
            out = new PrintWriter(s.getOutputStream(), true); //effettua flush automaticamente
            in = new Scanner(s.getInputStream());
        } catch (IOException e) {
            System.out.println("impossibile connettere al server");
        }
    }

    public void startClient(){
        do {
            out.println(nickname);
            out.println(connessione);
        }while (!in.nextLine().equals("true"));
    }
}
