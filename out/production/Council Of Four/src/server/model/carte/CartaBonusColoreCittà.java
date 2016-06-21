package server.model.carte;
import classicondivise.carte.CartaConBonus;
import server.model.ColoreCittà;
import classicondivise.bonus.BonusPuntiVittoria;
import classicondivise.bonus.NullBonus;

import java.util.Objects;

public class CartaBonusColoreCittà extends CartaConBonus {
    private ColoreCittà colore;

    public CartaBonusColoreCittà(int puntiVittoria, ColoreCittà colore) {
        super(new BonusPuntiVittoria(puntiVittoria, NullBonus.getInstance()));
        this.colore = Objects.requireNonNull(colore);
    }

    public ColoreCittà getColore(){
        return colore;
    }

    @Override
    public boolean equals(Object obj) throws IllegalArgumentException{
        if(!(obj instanceof CartaBonusColoreCittà)) {
            return false;
        }
        CartaBonusColoreCittà altraCartaBonusColoreCittà = (CartaBonusColoreCittà) obj;
        return this.colore.equals(altraCartaBonusColoreCittà.getColore());
    }
}