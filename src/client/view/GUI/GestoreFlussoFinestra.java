package client.view.GUI;

import java.io.IOException;

/*
classe che contiene un riferimento alla javafx application (FlussoView), in grado di assegnare una nuova finestra principale
 */
public abstract class GestoreFlussoFinestra {
    private FlussoView flusso;
    public void setFlusso(FlussoView flusso) {
        this.flusso = flusso;
    }
    public FlussoView getFlusso(){return flusso;}
    public void setNuovoStep(String nomeFileFXML){
        try {
            flusso.setFinestraDaFXML(nomeFileFXML);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
