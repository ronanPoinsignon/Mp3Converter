package event.action;

import affichage.demande.Selection;
import javafx.event.ActionEvent;
import log.Logger;

/**
 * Evénement permettant l'affichage d'une bulle d'information correspondant à la liste des racourcis disponibles.
 * @author ronan
 *
 */
public class ActionEventRaccourcis extends ActionEventHandler {

	public ActionEventRaccourcis(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(ActionEvent arg0) {
		Logger.getInstance().showRaccourcisInformation();
	}

}