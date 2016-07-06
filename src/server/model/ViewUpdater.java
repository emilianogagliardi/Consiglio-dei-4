package server.model;

import interfaccecondivise.InterfacciaView;

/*
Observable prende in input nel metodo notify un updater. I sottotipo di
Observable ridefiiscono update per passarlo al padre che effettua la chiamata.
 */
@FunctionalInterface
public interface ViewUpdater {
    void update(InterfacciaView view);
}
