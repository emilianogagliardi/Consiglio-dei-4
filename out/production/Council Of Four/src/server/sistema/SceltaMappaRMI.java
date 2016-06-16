package server.sistema;

import interfaccecondivise.InterfacciaSceltaMappa;

import java.io.Serializable;

/**
 * Created by emilianogagliardi on 14/06/16.
 */
public class SceltaMappaRMI implements InterfacciaSceltaMappa, Serializable{
    transient AvviatorePartita avviatorePartita;

    public SceltaMappaRMI(AvviatorePartita avviatorePartita) {
        this.avviatorePartita = avviatorePartita;
    }

    @Override
    public void mappaScelta(int idMappa) {
       avviatorePartita.setMappa(idMappa);
    }
}
