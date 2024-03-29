package tache;

import event.tache.event.EventTacheUpdateMessage;
import event.tache.event.EventTacheUpdated;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Tache abstraite utilisée le maniement d'événements.
 * @author ronan
 *
 * @param <T>
 */
public abstract class Tache<T> extends Task<T> {

	protected Tache() {
		exceptionProperty().addListener((observable, oldValue, newValue) ->  {
			if(newValue != null) {
				Exception ex = (Exception) newValue;
				ex.printStackTrace();
				this.cancel();
			}
		});
	}

	@Override
	public void updateMessage(String msg) {
		super.updateMessage(msg);
		fireUpdateEvent(new EventTacheUpdateMessage(msg));
	}

	/*@Override
	public void updateTitle(String title) {
		super.updateMessage(title);
		fireUpdateEvent(new EventTacheUpdateTitle(title));
	}*/

	@Override
	public void updateProgress(long workDone, long max) {
		super.updateProgress(workDone, max);
		//fireUpdateEvent(new EventTacheUpdateProgress(workDone, max));
	}

	/*@Override
	public void updateProgress(double workDone, double max) {
		super.updateProgress(workDone, max);
		fireUpdateEvent(new EventTacheUpdateProgress(workDone, max));
	}*/

	@Override
	public void updateValue(T value) {
		super.updateValue(value);
		//fireUpdateEvent(new EventTacheUpdateValue());
	}

	/**
	 * Active un événement donné.
	 * @param event
	 */
	private void fireUpdateEvent(final EventTacheUpdated event) {
		Platform.runLater(() -> Tache.this.fireEvent(event));
	}
}
