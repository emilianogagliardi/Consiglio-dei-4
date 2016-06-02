package model;

import proxyview.InterfacciaView;

@FunctionalInterface
public interface ViewUpdater {
    void updater(InterfacciaView view);
}
