package model.carte;


public class CartaPolitica extends Carta {
    private ColoreCartaPolitica colore;

    public CartaPolitica(ColoreCartaPolitica colore){
        this.colore = colore;
    }
    public ColoreCartaPolitica getColore(){
        return colore;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CartaPolitica) {
            CartaPolitica c = (CartaPolitica) o;
            if (c.getColore() == colore)
                return true;
        }
        return false;
    }
}
