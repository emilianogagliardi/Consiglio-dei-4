package model.carte;

public abstract class Carta {
    private boolean visibile = true; //una carta con visibile = true significa che è scoperta

    public boolean getVisibile() {
        return visibile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }
}
