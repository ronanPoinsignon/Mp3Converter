package event.window;

import affichage.demande.Selection;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

/**
 * Event abstrait correspondant aux fenêtre windows (généralement le stage de la fenêtre).
 * @author ronan
 *
 */
public abstract class WindowEventHandler implements EventHandler<WindowEvent>{

	Selection selection;

	protected WindowEventHandler(Selection selection) {
		this.selection = selection;
	}
}