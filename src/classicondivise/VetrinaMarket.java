package classicondivise;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VetrinaMarket implements Serializable {
    List<Vendibile> vendibili;

    public VetrinaMarket(){
        vendibili = new ArrayList<>();
    }

    public void aggiungiVendibile(Vendibile oggetto){
        vendibili.add(oggetto);
    }

    public boolean rimuoviVendibile(Vendibile oggetto){
        for (Vendibile vendibile : vendibili) {
            if (vendibile.equals(oggetto)) {
                vendibili.remove(vendibile);
                return true;
            }
        }
        return false;
    }

}
