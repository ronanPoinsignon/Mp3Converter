package event.action;

import affichage.demande.Selection;
import javafx.event.ActionEvent;

/**
 * Evénement refaisant l'action annulée.
 * @author ronan
 *
 */
public class ActionEventRedo extends ActionEventHandler {

	public ActionEventRedo(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(ActionEvent event) {
		selection.reexecuter();
	}

}
