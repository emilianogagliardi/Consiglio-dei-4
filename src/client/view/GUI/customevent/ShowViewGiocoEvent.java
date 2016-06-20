package client.view.GUI.customevent;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created by emilianogagliardi on 20/06/16.
 */
public class ShowViewGiocoEvent extends Event {
    public static final EventType<ShowViewGiocoEvent> SHOW_IMAGE = new EventType<>("SHOW_IMAGE");
    public ShowViewGiocoEvent() {
        super(SHOW_IMAGE);
    }
}
