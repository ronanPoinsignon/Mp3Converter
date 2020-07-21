package event.mouse;


import affichage.demande.Selection;
import exception.PasDeResultatException;
import javafx.scene.input.MouseEvent;
import prog.Utils;

public class MouseEventConversionInstantannee extends MouseEventHandler {

	public MouseEventConversionInstantannee(Selection selection) {
		super(selection);
	}

	@Override
	public void handle(MouseEvent arg0) {
		String url;
		try {
			url = Utils.showInputDIalogAjout();
			selection.convertirFromUrl(url);
		} catch (PasDeResultatException e) {
			
		}
	}

}
