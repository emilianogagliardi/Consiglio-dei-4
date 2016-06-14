package server.model.bonus;

public class BonusMonete extends RealBonus {
    private int numeroMonete;

    public BonusMonete (int m, Bonus decorated) throws IllegalArgumentException{
        super(decorated);
        if (m <= 0) throw new IllegalArgumentException("Non è possibile creare un bonus guadagna monete con un numero di monete negativo o nullo");
        numeroMonete = m;
    }

    public int getNumeroMonete() {
        return numeroMonete;
    }
}