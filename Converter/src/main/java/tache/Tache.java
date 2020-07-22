package tache;

import event.tache.event.EventTacheUpdateMessage;
import event.tache.event.EventTacheUpdated;
import javafx.application.Platform;
import javafx.concurrent.Task;

public abstract class Tache<T> extends Task<T> {
	
	@Override
	public void updateMessage(String msg) {
		super.updateMessage(msg);
		fireUpdateEvent(new EventTacheUpdateMessage(msg));
	}
	
	@Override
	public void updateTitle(String title) {
		super.updateMessage(title);
		//fireUpdateEvent(new EventTacheUpdateTitle());
	}
	
	@Override
	public void updateProgress(long workDone, long max) {
		super.updateProgress(workDone, max);
		//fireUpdateEvent(new EventTacheUpdateProgress());
	}
	
	@Override
	public void updateProgress(double workDone, double max) {
		super.updateProgress(workDone, max);
		//fireUpdateEvent(new EventTacheUpdateProgress());
	}
	
	@Override
	public void updateValue(T value) {
		super.updateValue(value);
		//fireUpdateEvent(new EventTacheUpdateValue());
	}
	
	private void fireUpdateEvent(EventTacheUpdated event) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				Tache.this.fireEvent(event);
			}
		});
	}
}
