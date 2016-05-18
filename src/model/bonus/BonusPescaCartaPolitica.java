package model.bonus;

public class BonusPescaCartaPolitica extends RealBonus {
    private int numeroCarte;

    public BonusPescaCartaPolitica(int n, RealBonus decorated) throws IllegalArgumentException{
        super(decorated);
        if (n <= 0) throw new IllegalArgumentException("Non è possibile generare un bonus pesca carte politica con un numero nullo o negativo di carte");
        numeroCarte = n;
    }

    public void ottieni(Giocatore giocatore) {
        giocatore.pescaCartePolitica(numeroCarte);
        super.ottieniDecoratedBonus(giocatore);
    }

}