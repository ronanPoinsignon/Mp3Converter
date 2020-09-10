package event.mouse;

import affichage.demande.Selection;
import exception.PasDeResultatException;
import javafx.scene.input.MouseEvent;
import prog.Utils;

public class MouseEventAjoutPlaylist extends MouseEventHandler {
	
	public MouseEventAjoutPlaylist(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(MouseEvent arg0) {
		String url = null;
		try {
			url = Utils.showInputDIalogAjout();
		} catch (PasDeResultatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		selection.ajouterPlaylist(url);
	}
}
