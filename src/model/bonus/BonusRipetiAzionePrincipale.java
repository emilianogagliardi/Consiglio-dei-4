package model.bonus;

import model.NomeAzionePrincipale;

public class BonusRipetiAzionePrincipale extends RealBonus {
    NomeAzionePrincipale azionePrincipale;
    public BonusRipetiAzionePrincipale(NomeAzionePrincipale azionePrincipale, Bonus decorated){
        super(decorated);
        this.azionePrincipale = azionePrincipale;
    }

    public NomeAzionePrincipale getAzionePrincipale(){return azionePrincipale;}
}
