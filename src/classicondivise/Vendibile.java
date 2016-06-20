package classicondivise;


import java.io.Serializable;

public class Vendibile<T> implements Serializable {
    private T oggetto;
    private int prezzo, idGiocatore;
    private IdVendibile idVendibile;

    public Vendibile(T oggetto, int prezzo, int idGiocatore, IdVendibile idVendibile) {
        if (oggetto == null || prezzo < 0)
            throw new IllegalArgumentException("L'oggetto vendibile deve esistere ed avere un prezzo superiore o uguale a 0");
        this.oggetto = oggetto;
        this.prezzo = prezzo;
        this.idGiocatore = idGiocatore;
        this.idVendibile = idVendibile;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public int getIdGiocatore() {
        return idGiocatore;
    }

    public IdVendibile getIdVendibile() {
        return idVendibile;
    }

    public T getOggetto() {
        return oggetto;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Vendibile)) return false;
        Vendibile otherVendibile = (Vendibile) other;
        return otherVendibile.getOggetto().equals(this.oggetto);
    }
}
