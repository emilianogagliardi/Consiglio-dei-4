package client.view.GUI.controllerFX;

import view.GUI.FlussoView;

import java.io.IOException;

/**
 * Created by emilianogagliardi on 13/06/16.
 */
/*
classe che contiene un riferimento alla javafx application (FlussoView), in grado di assegnare una nuova finestra principale
 */
public abstract class GestoreFlussoFinestra {
    private FlussoView flusso;
    public void setFlusso(FlussoView flusso) {
        this.flusso = flusso;
    }
    public void setNuovoStep(String nomeFileFXML) throws IOException{
        flusso.setFinestraDaFXML(nomeFileFXML);
    }
}
