package model;

import java.util.Objects;

public class Re {
    private Città città;

    public Re (Città città) {
        this.città = Objects.requireNonNull(città);
    }

    public Città getCittà(){return città;}

    public void setPosizione(Città città) {
        this.città = città;
        //TODO: updatePosizioneRe()
    }
}
