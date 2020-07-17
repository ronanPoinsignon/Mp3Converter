package event.window;

import affichage.demande.Selection;
import javafx.stage.WindowEvent;

public class WindowEventQuitter extends WindowEventHandler {

	public WindowEventQuitter(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(WindowEvent event) {
		selection.quitter();
	}

}
