package model.carte;

public abstract class Carta {
    private boolean visibile = true;

    public boolean getVisibile() {
        return visibile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }
}
