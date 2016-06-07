package server;

/**
 * Thread timeout viene fatto partire dall'accettore di connessioni, prendendo come parametro
 * l'accettore stesso. Dopo 20 secondi impone la fine dei giocatori per partita in creazione,
 * che comporterà l'inizializzazione e lo start della partita.
 */

public class ThreadTimeout extends Thread{
    Server server;

    public ThreadTimeout(Server server){
        this.server = server;
    }

    @Override
    public void run() {
        if(!this.isInterrupted())
            server.fineGiocatoriAccettati();
    }
}
