package instauraconnessione;

/**
 * Thread timeout viene fatto partire dall'accettore di connessioni, prendendo come parametro
 * l'accettore stesso. Dopo 20 secondi impone la fine dei giocatori per partita in creazione,
 * che comporterà l'inizializzazione e lo start della partita.
 */

public class ThreadTimeout extends Thread{
    InstauraConnessioni attesaConnessioni;

    public ThreadTimeout(InstauraConnessioni a){
        attesaConnessioni = a;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                Thread.sleep(500); //controlla se è interrotto o no ogni mezzo secondo
            } catch (InterruptedException e) {
                System.out.println("errore nel timeout");
            }
        }
        if(!this.isInterrupted())
            attesaConnessioni.fineGiocatoriAccettati();
    }
}
