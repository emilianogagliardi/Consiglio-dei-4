package model.bonus;

public class BonusMoneta extends RealBonus {
    private int numeroMonete;

    public BonusMoneta (int m, RealBonus decorated) throws IllegalArgumentException{
        super(decorated);
        if (m <= 0) throw new IllegalArgumentException("Non è possibile creare un bonus guadagna monete con un numero di monete negativo o nullo");
        numeroMonete = m;
    }

    public void ottieni(Giocatore giocatore) {
        giocatore.guadagnaMonete(numeroMonete);
        super.ottieniDecoratedBonus(giocatore);
    }

}