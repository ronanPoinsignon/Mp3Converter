package event.action;

import affichage.demande.Selection;
import javafx.event.ActionEvent;
import log.Logger;

public class ActionEventRaccourcis extends ActionEventHandler {

	public ActionEventRaccourcis(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(ActionEvent arg0) {
		Logger.getInstance().showRaccourcisInformation();
	}

}