package event.window;

import affichage.demande.Selection;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public abstract class WindowEventHandler implements EventHandler<WindowEvent>{

	Selection selection;
	
	public WindowEventHandler(Selection selection) {
		this.selection = selection;
	}
}