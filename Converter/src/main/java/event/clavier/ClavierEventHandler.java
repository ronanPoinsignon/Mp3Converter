package event.clavier;

import affichage.demande.Selection;
import javafx.event.EventHandler;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class ClavierEventHandler implements EventHandler<KeyEvent> {

	KeyCombination copier = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
	KeyCombination coller = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);

	private Selection selection;
	
	public ClavierEventHandler(Selection selection) {
		this.selection = selection;
	}
	
	@Override
	public void handle(KeyEvent event) {
		if(copier.match(event)) {
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			content.putString(selection.getSelectedLink());
			clipboard.setContent(content);
			return;
		}
		
		if(coller.match(event)) {
			Clipboard clipboard = Clipboard.getSystemClipboard();
			selection.addVideoToTable(clipboard.getString());
		}
	}

}
