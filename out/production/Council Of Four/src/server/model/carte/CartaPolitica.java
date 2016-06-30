package server.model.carte;


import classicondivise.CostantiCondivise;
import classicondivise.carte.Carta;

import java.util.Objects;

public class CartaPolitica extends Carta {
    private ColoreCartaPolitica colore;

    public CartaPolitica(ColoreCartaPolitica colore){
        this.colore = Objects.requireNonNull(colore);
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

    @Override
    public int hashCode(){
        return CostantiCondivise.HASH_CODE_CARTA_POLITICA;
    }
}
