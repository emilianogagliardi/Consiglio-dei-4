package client.view.GUI;

/*
classe che contiene un riferimento alla javafx application (FlussoView), in grado di assegnare una nuova finestra principale
 */
public abstract class GestoreFlussoFinestra{
    transient private FXApplication application;

    public void setApplication(FXApplication application) {
        this.application = application;
    }

    public FXApplication getApplication(){return application;}
/*
    public void showSceltaMappa(){
        Platform.runLater(() -> {
            try {
                application.showSceltaMappa();
            }catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void showFinestraGioco(){
        Platform.runLater(() -> {
            try {
                application.showFinestraGioco();
            }catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    */
}
