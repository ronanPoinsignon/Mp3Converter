package tache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import it.sauronsoftware.jave.EncoderException;
import javafx.concurrent.Task;
import log.Logger;
import prog.convertisseur.Convertisseur;
import prog.convertisseur.ConvertisseurMusique;
import prog.convertisseur.ConvertisseurVideo;
import prog.video.Video;

/**
 * Classe permettant la conversion des vidéos de la liste.
 * @author ronan
 *
 */
public class TacheConvertirToFile extends Task<List<File>> {

	private List<Video> listeVideos;
	private File folder;
	private int bitRate;
	private List<String> listeExtensions;
	
	public TacheConvertirToFile(List<Video> listeVideos, File folder, int bitRate, List<String> listeExtensions) {
		this.listeVideos = listeVideos;
		this.folder = folder;
		this.bitRate = bitRate;
		this.listeExtensions = listeExtensions;
	}
	
	@Override
	protected List<File> call() throws Exception {
		ArrayList<File> listeFichiers = new ArrayList<>(), listeMp4 = new ArrayList<>();
		if(folder == null)
			return listeFichiers;
		if(listeExtensions.isEmpty())
			return listeFichiers;
		Convertisseur convertisseur = null;
		boolean hasMp4 = listeExtensions.contains("mp4");
		File folderMp4 = null;
		if(hasMp4)
			folderMp4 = new File(folder.getPath() + "\\mp4");
		else {
			folderMp4 = new File(folder.getPath() + "\\mp4H");
			folderMp4.mkdirs();
			Files.setAttribute(Paths.get(folderMp4.getPath()), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
		}
		int tailleListe = listeVideos.size();
		int cpt = 0;
		File fichier = null, fichierMp4 = null;
		if(hasMp4)
			listeExtensions.remove("mp4");
		for(Video video : listeVideos) {
			this.update(video);
			if(hasMp4) {
				fichierMp4 = video.convertToMp4GoodQuality(folderMp4);
			}
			else
				fichierMp4 = video.convertToMp4(folderMp4);
			listeMp4.add(fichierMp4);
			try {
				for(String extension : listeExtensions) {
					File folderVideo = new File(folder + "\\" + extension.toLowerCase());
					if(!folderVideo.exists())
						folderVideo.mkdirs();
					fichier = new File(folderVideo.getPath() + "\\" + video.getTitre() + "." + extension.toLowerCase());
					if(extension.equalsIgnoreCase("mp3")) {
						convertisseur = new ConvertisseurMusique(bitRate);
					}
					else
						convertisseur = new ConvertisseurVideo(extension, bitRate, true);
					if(!extension.equalsIgnoreCase("mp4")) {
						convertisseur.convertir(fichierMp4, fichier);
					}
				}
				listeFichiers.add(fichier);
			} catch (IllegalArgumentException | EncoderException e) {
				Logger.getInstance().showErrorAlert(e);
			}
			this.updateProgress(++cpt, tailleListe);
		}
		if(!hasMp4) {
			deleteFile(folderMp4);
		}
		return listeFichiers;
	}
	
	/**
	 * Modifie le message pour qu'il soit conforme avec le déroulement de la tâche.
	 * @param video
	 * @throws Exception
	 */
	private void update(Video video) throws Exception {
        this.updateMessage("en téléchargement : " + video.getTitre());
    }
	
	private void deleteFile(File folder) throws IOException {
		Files.walk(folder.toPath())
			.sorted(Comparator.reverseOrder())
			.map(Path::toFile)
			.forEach(File::delete);
	}

}
