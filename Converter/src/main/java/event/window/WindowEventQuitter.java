package event.window;

import affichage.demande.Selection;
import javafx.stage.WindowEvent;

/**
 * Evénement gérant la fin de l'application lorsque l'utilisateur souhaite la fermer.
 * @author ronan
 *
 */
public class WindowEventQuitter extends WindowEventHandler {

	public WindowEventQuitter(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(WindowEvent event) {
		selection.quitter();
	}

}
