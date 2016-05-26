package model;

import model.eccezioni.NumeroConsiglieriNonValidoException;
import model.eccezioni.NumeroRegioniNonValidoException;
import model.eccezioni.ReNonInizializzatoException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static model.Costanti.NUM_REGIONI;

public class Partita {
    private Re re;
    private BalconeDelConsiglio balconeDelConsiglioRe;
    private Collection<Regione> regioni;


    public void setRe(Città cittàRe) throws ReNonInizializzatoException{
        Re.init(cittàRe);
        re = Re.getInstance();
    }

    public void setBalconeDelConsiglioRe(BalconeDelConsiglio balconeDelConsiglioRe){
        this.balconeDelConsiglioRe = balconeDelConsiglioRe;
    }

    public void setRegioni(Collection<Regione> regioni) throws NumeroRegioniNonValidoException{
        if (regioni.size() != 3){
            throw new NumeroRegioniNonValidoException();
        }
        this.regioni = regioni;
    }

    
}
