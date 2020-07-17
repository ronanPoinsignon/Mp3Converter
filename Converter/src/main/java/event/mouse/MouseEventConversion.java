package event.mouse;

import affichage.demande.Selection;
import javafx.scene.input.MouseEvent;

/**
 * Evénement de conversion de la liste de vidéos.
 * @author ronan
 *
 */
public class MouseEventConversion extends MouseEventHandler {

	public MouseEventConversion(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(MouseEvent event) {
		selection.convertir();
	}

}
