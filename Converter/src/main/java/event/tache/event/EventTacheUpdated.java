package event.tache.event;

import javafx.event.Event;
import javafx.event.EventType;
import tache.Tache;

/**
 * Evénement abstrait utilisé dans les différentes {@link Tache} de l'application.
 * @author ronan
 *
 */
public abstract class EventTacheUpdated extends EventAbstrait {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final EventType<EventTacheUpdated> EVENT_UPDATE = new EventType<>(Event.ANY);

	protected EventTacheUpdated(EventType<? extends Event> eventType) {
		super(eventType);
	}

}
