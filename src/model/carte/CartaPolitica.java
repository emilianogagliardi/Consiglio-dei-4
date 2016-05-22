package model.carte;

public class CartaPolitica extends Carta {
    private ColoreCartaPolitica colore;

    public CartaPolitica(ColoreCartaPolitica colore){
        this.colore = colore;
    }
    public ColoreCartaPolitica getColore(){
        return colore;
    }
}
