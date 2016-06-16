package server.model;

import interfaccecondivise.InterfacciaView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

public class Re extends Observable {
    private Città città;

    public Re (Città città, ArrayList<InterfacciaView> views) {
        super(views);
        this.città = Objects.requireNonNull(città);
    }

    public Città getCittà(){return città;}

    public void setPosizione(Città città) {
        this.città = città;
        updateViewPosizione();
    }

    private void updateViewPosizione(){
        super.notifyViews((InterfacciaView v) -> {
            try {
                v.updatePosizioneRe(città.getNome().toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
