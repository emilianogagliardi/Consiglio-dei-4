package instauraconnessione;

/**
 * Thread timeout viene fatto partire dall'accettore di connessioni, prendendo come parametro
 * l'accettore stesso. Dopo 20 secondi impone la fine dei giocatori per partita in creazione,
 * che comporterà l'inizializzazione e lo start della partita.
 */

public class ThreadTimeout implements Runnable{
    AttesaConnessioni attesaConnessioni;

    public ThreadTimeout(AttesaConnessioni a){
        attesaConnessioni = a;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(CostantiSistema.TIMEOUT*1000);
        }catch (InterruptedException e) {
            System.out.println("errore nel timeout");
        }
        attesaConnessioni.fineGiocatoriAccettati();
    }
}
