package event.tache.handler;

import event.tache.event.EventTacheUpdateMessage;
import javafx.event.EventHandler;

/**
 * Handler abstrait servant à la réception de {@link EventTacheUpdateMessage}.
 * @author ronan
 *
 */
public abstract class EventHandlerTacheUpdateMessage implements EventHandler<EventTacheUpdateMessage> {

	@Override
	public void handle(EventTacheUpdateMessage event) {
		event.invokeHandler(this);
	}
	
	public abstract void onUpdateMessage(String message);

}
