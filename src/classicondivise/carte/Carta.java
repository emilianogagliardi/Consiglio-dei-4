package classicondivise.carte;

import java.io.Serializable;

public abstract class Carta implements Serializable{
    private boolean visibile = true; //una carta con visibile = true significa che è scoperta


    public Carta(){
        //do nothing
    } //per la serializzazione di carta permesso

    public boolean isVisibile() {
        return visibile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }
}
