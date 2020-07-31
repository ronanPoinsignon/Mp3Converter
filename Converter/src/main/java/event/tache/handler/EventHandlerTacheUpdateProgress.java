package event.tache.handler;

import event.tache.event.EventTacheUpdateProgress;
import javafx.event.EventHandler;

/**
 * Handler abstrait servant à la réception de {@link EventTacheUpdateProgress}.
 * @author ronan
 *
 */
public abstract class EventHandlerTacheUpdateProgress implements EventHandler<EventTacheUpdateProgress> {

	@Override
	public void handle(EventTacheUpdateProgress event) {
		event.invokeHandler(this);
	}
	
	public abstract void onUpdateProgress(long workDone, long max);

}
