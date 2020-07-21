package event.tache.event;

import javafx.event.Event;
import javafx.event.EventType;

public abstract class EventAbstrait extends Event {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public EventAbstrait(EventType<? extends Event> eventType) {
        super(eventType);
    }
}