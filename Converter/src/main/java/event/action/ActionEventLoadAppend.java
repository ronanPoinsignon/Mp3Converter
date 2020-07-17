package event.action;

import affichage.demande.Selection;
import javafx.event.ActionEvent;

/**
 * Evénement de chargement d'un fichier.
 * @author ronan
 *
 */
public class ActionEventLoadAppend extends ActionEventHandler {

	public ActionEventLoadAppend(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(ActionEvent event) {
		selection.loadAppend();
	}

}
