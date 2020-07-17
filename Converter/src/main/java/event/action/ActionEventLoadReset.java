package event.action;

import affichage.demande.Selection;
import javafx.event.ActionEvent;

/**
 * Evénement de chargement d'un fichier.
 * @author ronan
 *
 */
public class ActionEventLoadReset extends ActionEventHandler {

	public ActionEventLoadReset(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(ActionEvent event) {
		selection.loadReset();
	}

}
