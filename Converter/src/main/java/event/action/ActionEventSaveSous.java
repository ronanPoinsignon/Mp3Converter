package event.action;

import affichage.demande.Selection;
import fichier.DirectoryChooserManager;
import javafx.event.ActionEvent;
import prog.Utils;

public class ActionEventSaveSous extends ActionEventHandler {

	public ActionEventSaveSous(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(ActionEvent event) {
		DirectoryChooserManager.getInstance(Utils.DIRECTORY_CHOOSER_SAVE).setInitialDirectory(null);
		selection.sauvegarder();
	}
}
