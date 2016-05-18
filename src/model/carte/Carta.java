package model.carte;

/**
 * Created by riccardo on 18/05/16.
 */
public abstract class Carta {
    private boolean visibile = true;

    public boolean getVisibile() {
        return visibile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }
}
