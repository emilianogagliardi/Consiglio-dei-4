package model.carte;


public class NullCarta extends Carta {
    private static NullCarta uniqueInstance;

    private NullCarta(){
        //do nothing
    }

    public static synchronized NullCarta getInstance() {
        if(uniqueInstance == null) {
            uniqueInstance = new NullCarta();
        }
        return uniqueInstance;
    }
}
