package model.carte;


import model.Colore;

import java.util.ArrayList;
import java.util.Objects;

public enum ColoreCartaPolitica {
    JOLLY, VIOLA, AZZURRO, NERO, ROSA, ARANCIONE, BIANCO;
    public Colore toColore(){
        return Colore.valueOf(this.toString());
    }
    public static ArrayList<Colore> toColore(ArrayList<ColoreCartaPolitica> coloriCartaPolitica){
        ArrayList<Colore> arrayList = new ArrayList<>();
        for(ColoreCartaPolitica coloreCartaPolitica : coloriCartaPolitica)
            arrayList.add(coloreCartaPolitica.toColore());
        return arrayList;
    }
}
