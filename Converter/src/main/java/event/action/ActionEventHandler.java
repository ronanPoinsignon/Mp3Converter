package event.action;

import affichage.demande.Selection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Classe permettant la gestion des événement de la souris lors d'un clic sur un bouton.
 * @author ronan
 *
 */
public abstract class ActionEventHandler implements EventHandler<ActionEvent> {

	protected Selection selection;

	protected ActionEventHandler(Selection selection) {
		this.selection = selection;
	}

}
