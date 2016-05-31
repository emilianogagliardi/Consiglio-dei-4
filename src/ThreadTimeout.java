
/**
 * Thread timeout viene fatto partire dall'accettore di connessioni, prendendo come parametro
 * l'accettore stesso. Dopo 20 secondi impone la fine dei giocatori per partita in creazione,
 * che comporterà l'inizializzazione e lo start della partita.
 */

public class ThreadTimeout extends Thread{
    Server attesaConnessioni;

    public ThreadTimeout(Server a){
        attesaConnessioni = a;
    }

    @Override
    public void run() {
        if(!this.isInterrupted())
            attesaConnessioni.fineGiocatoriAccettati();
    }
}
