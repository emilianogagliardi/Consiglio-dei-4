package model;


public enum ColoreConsigliere {
    VIOLA, AZZURRO, NERO, ROSA, ARANCIONE, BIANCO;
    public Colore toColore(){
        return Colore.valueOf(this.toString());
    }
}
