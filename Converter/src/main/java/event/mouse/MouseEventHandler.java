package event.mouse;

import affichage.demande.Selection;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Classe permettant la gestion des événement de la souris lors d'un clic sur un item.
 * @author ronan
 *
 */
public abstract class MouseEventHandler implements EventHandler<MouseEvent> {

	Selection selection;
	
	public MouseEventHandler(Selection selection) {
		this.selection = selection;
	}
}
