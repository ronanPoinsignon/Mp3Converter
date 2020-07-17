package event.action;

import affichage.demande.Selection;
import javafx.event.ActionEvent;

/**
 * Evénement d'annulation d'une action.
 * @author ronan
 *
 */
public class ActionEventAnnuler extends ActionEventHandler {

	public ActionEventAnnuler(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(ActionEvent event) {
		selection.annuler();
	}

}
