package server.model.bonus;

public abstract class RealBonus extends Bonus {
    private Bonus decoratedBonus;

    public RealBonus (Bonus decorated) throws IllegalArgumentException{
        if (decorated == null) throw new IllegalArgumentException("Non è possibile passare un bonus nullo come decorato, usare NullBonus.getInstance()");
        this.decoratedBonus = decorated;
    }

    public Bonus getDecoratedBonus(){
        return decoratedBonus;
    }

}