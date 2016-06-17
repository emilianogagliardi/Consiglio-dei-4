package server.sistema;

import interfaccecondivise.InterfacciaSceltaMappa;

import java.io.Serializable;


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
