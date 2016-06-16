package client.view.GUI;

import java.io.IOException;

/*
classe che contiene un riferimento alla javafx application (FlussoView), in grado di assegnare una nuova finestra principale
 */
public abstract class GestoreFlussoFinestra{
    transient private FXApplication application;

    public void setApplication(FXApplication application) {
        this.application = application;
    }

    public FXApplication getApplication(){return application;}

    public void setNuovoStep(String nomeFileFXML){
        try {
            application.setFinestraDaFXML(nomeFileFXML);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
