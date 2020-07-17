package fichier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import prog.Video;

/**
 * Classe permettant de pouvoir sauvegarder et charger des fichiers. 
 * @author ronan
 *
 */
public class FileManager {

	private static final String TYPE_FICHIER = "*.conv";
	private static final String DESCRIPTION_TYPE_FICHIER = "CONVERTER FILE (.conv)";
	
	private static FileManager fileManager = null;
	
	public static FileManager getInstance() {
		if(fileManager == null)
			fileManager = new FileManager();
		return fileManager;
	}
	
	private FileManager() {

	}
	
	/**
	 * Ouvre une fenêtre de séléction de fichier.
	 * @param window
	 * @param extension
	 * @return
	 */
	public File getFile(Window window, String extension) {
		FileChooser chooser = new FileChooser();
		ExtensionFilter extFilter = new FileChooser.ExtensionFilter(DESCRIPTION_TYPE_FICHIER, TYPE_FICHIER);
		chooser.getExtensionFilters().add(extFilter);
		return chooser.showOpenDialog(window);
	}
	
	/**
	 * Ouvre une fenêtre de sélection de dossier.
	 * @param window
	 * @return
	 */
	public File getFolder(Window window) {
		DirectoryChooser chooser = new DirectoryChooser();
		return chooser.showDialog(window);
	}
	
	/**
	 * Demande un endroit de sauvegarde et sauvegarde les vidéos données dans un fichier.
	 * @param window
	 * @param listeVideos
	 * @throws IOException
	 */
	public void save(Window window, List<Video> listeVideos) throws IOException {
		FileChooser chooser = new FileChooser();
		ExtensionFilter extFilter = new FileChooser.ExtensionFilter(DESCRIPTION_TYPE_FICHIER, TYPE_FICHIER);
		chooser.getExtensionFilters().add(extFilter);
		File file = chooser.showSaveDialog(window);
		if(file == null)
			return;
		FileOutputStream fos = null;
		ObjectOutputStream output = null;
		try {
			fos = new FileOutputStream(file);
			output = new ObjectOutputStream(fos);
			output.writeObject(listeVideos);
			output.flush();
		}
		catch(IOException e) {
			throw e;
		}
		finally {
			if(output != null)
				output.close();
			if(fos != null)
				fos.close();
		}
	}
	
	/**
	 * Retourne une liste de vidéos depuis un fichier demandé.
	 * @param window
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<Video> load(Window window) throws IOException, ClassNotFoundException {
		File file = fileManager.getFile(window, TYPE_FICHIER);
		if(file == null)
			return new ArrayList<Video>();
		FileInputStream fis = null;
		ObjectInputStream input = null;
		try {
			fis = new FileInputStream(file);
			input = new ObjectInputStream(fis);
			return (List<Video>) input.readObject();
		}
		catch(IOException e) {
			throw e;
		}
		finally {
			if(input != null)
				input.close();
			if(fis != null)
				fis.close();
		}
	}
}