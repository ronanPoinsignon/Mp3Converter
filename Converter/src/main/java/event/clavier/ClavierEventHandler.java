package event.clavier;

import affichage.demande.Selection;
import javafx.event.EventHandler;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import javafx.scene.input.KeyEvent;

public class ClavierEventHandler implements EventHandler<KeyEvent> {

	public static final KeyCode KEY_CODE_COPIER = KeyCode.C,
			KEY_CODE_COLLER = KeyCode.V,
			KEY_CODE_SUPPRIMER = KeyCode.DELETE,
			KEY_CODE_INVERSER_HAUT = KeyCode.UP,
			KEY_CODE_INVERSER_BAS = KeyCode.DOWN;
	
	public static final Modifier MODIFIER_COPIER = KeyCombination.CONTROL_DOWN,
			MODIFIER_COLLER = KeyCombination.CONTROL_DOWN,
			MODIFIER_SUPPRIMER = KeyCombination.META_ANY,
			MODIFIER_INVERSER_HAUT = KeyCombination.ALT_DOWN,
			MODIFIER_INVERSER_BAS = KeyCombination.ALT_DOWN;
	
	private KeyCombination copier = new KeyCodeCombination(KEY_CODE_COPIER, MODIFIER_COPIER);
	private KeyCombination coller = new KeyCodeCombination(KEY_CODE_COLLER, MODIFIER_COLLER);
	private KeyCombination supprimer = new KeyCodeCombination(KEY_CODE_SUPPRIMER, MODIFIER_SUPPRIMER);
	private KeyCombination inverserHaut = new KeyCodeCombination(KEY_CODE_INVERSER_HAUT, MODIFIER_INVERSER_HAUT);
	private KeyCombination inverserBas = new KeyCodeCombination(KEY_CODE_INVERSER_BAS, MODIFIER_INVERSER_BAS);
	
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
