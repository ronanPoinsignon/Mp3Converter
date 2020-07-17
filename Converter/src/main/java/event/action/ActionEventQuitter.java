package event.action;

import affichage.demande.Selection;
import javafx.event.ActionEvent;

/**
 * Evénement fermant l'application.
 * @author ronan
 *
 */
public class ActionEventQuitter extends ActionEventHandler {

	public ActionEventQuitter(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(ActionEvent event) {
		selection.quitter();
	}

}
