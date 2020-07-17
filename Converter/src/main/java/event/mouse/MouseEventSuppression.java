package event.mouse;

import affichage.demande.Selection;
import javafx.scene.input.MouseEvent;

/**
 * Evénement de suppression d'une vidéo de la liste de vidéo.
 * @author ronan
 *
 */
public class MouseEventSuppression extends MouseEventHandler {

	public MouseEventSuppression(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(MouseEvent event) {
		selection.removeLine();
	}

}
