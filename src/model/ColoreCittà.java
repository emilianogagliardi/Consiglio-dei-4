package model;


public enum ColoreCittà {
    ORO, ARGENTO, BRONZO, FERRO;
    public Colore toColore(){
        return Colore.valueOf(this.toString());
    }
}
