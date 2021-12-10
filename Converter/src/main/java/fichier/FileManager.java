package fichier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import commande.Gestionnaire;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import prog.video.Video;

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
		if(FileManager.fileManager == null) {
			FileManager.fileManager = new FileManager();
		}
		return FileManager.fileManager;
	}

	private FileManager() {

	}

	/**
	 * Ouvre une fenêtre de séléction de fichier.
	 * @param window
	 * @param extension
	 * @return
	 */
	public File getFile(Window window) {
		FileChooser chooser = new FileChooser();
		ExtensionFilter extFilter = new FileChooser.ExtensionFilter(FileManager.DESCRIPTION_TYPE_FICHIER, FileManager.TYPE_FICHIER);
		chooser.getExtensionFilters().add(extFilter);
		File fichier = chooser.showOpenDialog(window);
		if(fichier != null) {
			DirectoryChooserManager.getInstance("sauvegarder").setInitialDirectory(fichier);
		}
		return fichier;
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
		DirectoryChooser directory = DirectoryChooserManager.getInstance("sauvegarder");
		chooser.setInitialDirectory(directory.getInitialDirectory());
		ExtensionFilter extFilter = new FileChooser.ExtensionFilter(FileManager.DESCRIPTION_TYPE_FICHIER, FileManager.TYPE_FICHIER);
		chooser.getExtensionFilters().add(extFilter);
		File file = directory.getInitialDirectory();
		if(file == null) {
			file = chooser.showSaveDialog(window);
		}
		if(file == null) {
			return;
		}
		directory.setInitialDirectory(file);
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))){
			output.writeObject(listeVideos);
			output.flush();
			Gestionnaire.getInstance().notifySave();
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
		File file = FileManager.fileManager.getFile(window);
		if(file == null) {
			return new ArrayList<>();
		}
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))){
			return (List<Video>) input.readObject();
		}
	}
}
