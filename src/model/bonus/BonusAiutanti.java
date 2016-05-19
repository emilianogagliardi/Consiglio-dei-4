public class BonusAiutanti extends RealBonus {
    private int numeroAiutanti;

    public BonusAiutanti(int a, RealBonus decorated) throws IllegalArgumentException{
        super(decorated);
        if (a <= 0) throw new IllegalArgumentException("Non è possibile creare un bonus aiutanti con un numero negativo o nullo di aiutanti");
        numeroAiutanti = a;
    }
    public void ottieni(Giocatore giocatore) {
        giocatore.guadagnaAiutanti(numeroAiutanti);
        super.ottieniDecoratedBonus(giocatore);
    }
}