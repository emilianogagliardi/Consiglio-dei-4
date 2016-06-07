package model;

import proxyview.InterfacciaView;

@FunctionalInterface
public interface ViewUpdater {
    void update(InterfacciaView view);
}
