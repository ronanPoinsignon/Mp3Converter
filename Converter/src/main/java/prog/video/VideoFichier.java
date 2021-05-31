package prog.video;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Classe permettant la gestion de fichier vidéo donné directement depuis l'ordinateur.
 * @author ronan
 *
 */
public class VideoFichier extends Video {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public VideoFichier(String lien){
		super("", lien);
		File fichier = new File(lien);
		titre = fichier.getName().substring(0, fichier.getName().lastIndexOf("."));
	}

	@Override
	public File convertToMp4(File folder) throws IOException {
		File fichier = new File(lien);
		File nouveauFichier = new File(folder.getPath() + File.separator + fichier.getName());
		return Files.copy(fichier.toPath(), nouveauFichier.toPath(), StandardCopyOption.REPLACE_EXISTING).toFile();
	}

	@Override
	public File convertToMp4GoodQuality(File folder) throws Exception {
		File fichier = new File(lien);
		File nouveauFichier = new File(folder.getPath() + File.separator + fichier.getName());
		return Files.copy(fichier.toPath(), nouveauFichier.toPath(), StandardCopyOption.REPLACE_EXISTING).toFile();
	}

}
