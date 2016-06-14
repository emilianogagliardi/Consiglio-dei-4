package server.model;

import interfaccecondivise.InterfacciaView;

@FunctionalInterface
public interface ViewUpdater {
    void update(InterfacciaView view);
}
