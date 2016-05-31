package model.carte;


import model.Colore;

import java.util.Objects;

public enum ColoreCartaPolitica {
    JOLLY, VIOLA, AZZURRO, NERO, ROSA, ARANCIONE, BIANCO;
    public Colore toColore(){
        return Colore.valueOf(this.toString());
    }
}
