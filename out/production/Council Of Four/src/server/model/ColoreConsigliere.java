package server.model;


import java.util.ArrayList;

public enum ColoreConsigliere {
    VIOLA, AZZURRO, NERO, ROSA, ARANCIONE, BIANCO;
    public Colore toColore(){
        return Colore.valueOf(this.toString());
    }
    public static ArrayList<Colore> toColore(ArrayList<ColoreConsigliere> coloriConsigliere){
        ArrayList<Colore> arrayList = new ArrayList<>();
        for(ColoreConsigliere coloreConsigliere: coloriConsigliere)
            arrayList.add(coloreConsigliere.toColore());
        return arrayList;
    }
}
