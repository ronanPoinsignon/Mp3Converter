package event.action;

import affichage.demande.Selection;
import javafx.event.ActionEvent;

/**
 * Evénement de sauvegarde.
 * @author ronan
 *
 */
public class ActionEventSave extends ActionEventHandler {

	public ActionEventSave(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(ActionEvent event) {
		selection.sauvegarder();
	}

}
