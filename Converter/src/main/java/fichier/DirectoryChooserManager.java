package fichier;

import javafx.stage.DirectoryChooser;

public class DirectoryChooserManager {

	private static DirectoryChooserManager chooser = null;
	private final DirectoryChooser directoryChooser = new DirectoryChooser();
	
	public static DirectoryChooser getInstance() {
		if(chooser == null)
			chooser = new DirectoryChooserManager();
		return chooser.getDirectoryChooser();
	}
	
	private DirectoryChooserManager() {
		
	}
	
	private DirectoryChooser getDirectoryChooser() {
		return this.directoryChooser;
	}
}
