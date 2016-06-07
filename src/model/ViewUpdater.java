package model;

import proxyView.InterfacciaView;

@FunctionalInterface
public interface ViewUpdater {
    void update(InterfacciaView view);
}
