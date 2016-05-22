package model;


import model.carte.CartaBonusRegione;
import model.carte.CartaPermessoCostruzione;

import java.util.Collection;
import java.util.HashSet;

public class Regione {
    private BalconeDelConsiglio balconeDelConsiglio;
    private Collection<Città> città = new HashSet<Città>(5); //HashSet è un'implementazione dell'interfaccia Set, una Collection che non permette duplicati. 5 è la capacità iniziale
    private CartaBonusRegione cartaBonusRegione;
    private Mazzo<CartaPermessoCostruzione> mazzoCartePermessoCostruzione;
    CartaPermessoCostruzione cartaPermessoCostruzione1;
    CartaPermessoCostruzione cartaPermessoCostruzione2;
}
