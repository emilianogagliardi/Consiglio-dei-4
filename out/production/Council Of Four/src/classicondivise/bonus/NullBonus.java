package classicondivise.bonus;

import java.io.Serializable;

public class NullBonus extends Bonus {
    private static NullBonus instance;

    private NullBonus(){
        //do nothing
    }

    public static synchronized NullBonus getInstance(){
        if (instance == null){
            instance = new NullBonus();
        }
        return instance;
    }
}