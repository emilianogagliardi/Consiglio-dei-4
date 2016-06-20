package server.model;


import classicondivise.Colore;

import java.util.ArrayList;

public enum ColoreCittà {
    ORO, ARGENTO, BRONZO, FERRO, CITTA_RE;
    public Colore toColore(){
        return Colore.valueOf(this.toString());
    }
    public static ArrayList<Colore> toColore(ArrayList<ColoreCittà> coloriCittà){
        ArrayList<Colore> arrayList = new ArrayList<>();
        for(ColoreCittà coloreCittà : coloriCittà)
            arrayList.add(coloreCittà.toColore());
        return arrayList;
    }
}
