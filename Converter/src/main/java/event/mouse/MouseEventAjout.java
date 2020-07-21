package event.mouse;

import affichage.demande.Selection;
import exception.PasDeResultatException;
import javafx.scene.input.MouseEvent;
import prog.Utils;

/**
 * Evénement d'ajout d'une vidéo à la liste de vidéos.
 * @author ronan
 *
 */
public class MouseEventAjout extends MouseEventHandler {

	public MouseEventAjout(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(MouseEvent event) {
		String url;
		try {
			url = Utils.showInputDIalogAjout();
			selection.addVideoToTable(url);
		} catch (PasDeResultatException e) {
			
		} 
	}

}
