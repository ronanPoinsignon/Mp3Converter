package fichier;

import java.util.HashMap;

import javafx.stage.DirectoryChooser;

public class DirectoryChooserManager {

	private static HashMap<String, DirectoryChooser> map = new HashMap<>();
	
	private final DirectoryChooser directoryChooser = new DirectoryChooser();
	
	public static DirectoryChooser getInstance(String instance) {
		if(map.get(instance) == null)
			map.put(instance, new DirectoryChooserManager().getDirectoryChooser());
		return map.get(instance);
	}
	
	private DirectoryChooserManager() {
		
	}
	
	private DirectoryChooser getDirectoryChooser() {
		return this.directoryChooser;
	}
}
