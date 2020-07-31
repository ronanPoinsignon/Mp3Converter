package event.tache.event;

import javafx.event.Event;
import javafx.event.EventType;

public abstract class EventTacheUpdated extends EventAbstrait {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final EventType<EventTacheUpdated> EVENT_UPDATE = new EventType<>(ANY);

    public EventTacheUpdated(EventType<? extends Event> eventType) {
		super(eventType);
	}

}
