public class BonusAvanzaPercorsoNobiltà extends RealBonus {
    private int numeroPosti;

    public BonusAvanzaPercorsoNobiltà(int n, RealBonus decorated) throws IllegalArgumentException{
        super (decorated);
        if (n <= 0) throw new IllegalArgumentException("impossibile creare un bonus avanzamento percorso nobiltà con un numero negativo o nullo di poszioni");
        numeroPosti = n;
    }

    public void ottieni(Giocatore giocatore) {
        giocatore.avanzaPercorsoNobiltà(numeroPosti);
        super.ottieniDecoratedBonus(giocatore);
    }
}