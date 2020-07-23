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
	KeyCombination supprimer = new KeyCodeCombination(KeyCode.DELETE, KeyCombination.META_ANY);
	KeyCombination inverserHaut = new KeyCodeCombination(KeyCode.UP, KeyCombination.ALT_DOWN);
	KeyCombination inverserBas = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.ALT_DOWN);
	
	private Selection selection;
	
	public ClavierEventHandler(Selection selection) {
		this.selection = selection;
	}
	
	@Override
	public void handle(KeyEvent event) {
		if(event.getCode().equals(KeyCode.ALT))
			event.consume();
		
		if(copier.match(event)) {
			copier();
			event.consume();
			return;
		}
		
		if(coller.match(event)) {
			coller();
			event.consume();
			return;
		}
		
		if(supprimer.match(event)) {
			supprimer();
			event.consume();
			return;
		}
		
		if(inverserHaut.match(event)) {
			swapUp();
			event.consume();
			return;
		}
		
		if(inverserBas.match(event)) {
			swapDown();
			event.consume();
			return;
		}
	}
	
	public void copier() {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		try {
			content.putString(selection.getSelectedLink());
			clipboard.setContent(content);
		}
		catch(ArrayIndexOutOfBoundsException e) {

		}
	}
	
	public void coller() {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		String str = clipboard.getString();
		if(str != null && !str.isEmpty())
			selection.addVideoToTable(clipboard.getString());
	}
	
	public void supprimer() {
		try {
			selection.removeLine();
		}
		catch(ArrayIndexOutOfBoundsException e) {
			
		}
	}
	
	public void swapUp() {
		try {
			selection.swapUp();
		}
		catch(ArrayIndexOutOfBoundsException e) {
			
		}
	}
	
	public void swapDown() {
		try {
			selection.swapDown();
		}
		catch(ArrayIndexOutOfBoundsException e) {
			
		}
	}

}
