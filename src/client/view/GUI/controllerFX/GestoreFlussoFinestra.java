package client.view.GUI.controllerFX;

import client.view.GUI.FlussoView;

import java.io.IOException;

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
