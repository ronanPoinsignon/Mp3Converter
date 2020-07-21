package event.tache.event;

import event.tache.handler.EventHandlerTacheUpdateProgress;
import javafx.event.EventType;

public class EventTacheUpdateProgress extends EventTacheUpdated {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final EventType<EventTacheUpdateProgress> EVENT_UPDATE_PROGRESS = 
			new EventType<EventTacheUpdateProgress>(EVENT_UPDATE, "UPDATE_MESSAGE");

	private long workDone, max;
	
	public EventTacheUpdateProgress(long workDone, long max) {
		super(EVENT_UPDATE_PROGRESS);
		this.workDone = workDone;
		this.max = max;
	}

	public void invokeHandler(EventHandlerTacheUpdateProgress handler) {
		handler.onUpdateProgress(workDone, max);
	}
}
