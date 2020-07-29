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

import javafx.concurrent.Task;
import log.Logger;
import prog.Utils;
import prog.convertisseur.Convertisseur;
import prog.convertisseur.ConvertisseurMusique;
import prog.convertisseur.ConvertisseurVideo;
import prog.video.Video;
import ws.schild.jave.EncoderException;

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
	protected List<File> call() {
		ArrayList<File> listeFichiers = new ArrayList<>();
		List<Video> listeMauvaisFichiers = new ArrayList<>();
		int tailleListe = listeVideos.size();
		boolean hasMp4;
		int cpt = 0;

		if(folder == null)
			return new ArrayList<File>();
		if(listeExtensions.isEmpty())
			return new ArrayList<File>();
		hasMp4 = listeExtensions.contains("mp4");
		File folderMp4 = null;
		if(hasMp4) {
			folderMp4 = new File(folder.getPath() + "\\mp4");
		}
		else {
			folderMp4 = new File(folder.getPath() + "\\mp4H");
		}
		folderMp4.mkdirs();
		try {
			Files.setAttribute(Paths.get(folderMp4.getPath()), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e) {
			Logger.getInstance().showErrorAlert(e);
			e.printStackTrace();
		}
		if(hasMp4)
			listeExtensions.remove("mp4");
		for(Video video : listeVideos) {
			try {
				listeFichiers.add(this.convertVideo(video, folder, folderMp4, hasMp4));
			}
			catch(EncoderException e) {
				e.printStackTrace();
				if(!listeMauvaisFichiers.contains(video))
					listeMauvaisFichiers.add(video);
			}
			catch(Exception e) {
				e.printStackTrace();
				listeMauvaisFichiers.add(video);
			}
			this.updateProgress(++cpt, tailleListe);
		}
		Logger.getInstance().showErrorAlertIsNotVideoFile(listeMauvaisFichiers);
		try {
			if(!hasMp4)
				deleteFile(folderMp4);
		} catch (IOException e) {
			Logger.getInstance().showErrorAlert(e);
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
	
	private File convertVideo(Video video, File folder, File folderMp4, boolean goodQuality) throws Exception {
		ArrayList<File> listeMp4 = new ArrayList<>();
		File fichier = null, fichierMp4 = null;
		Convertisseur convertisseur = null;
		try {
			this.update(video);
		} catch (Exception e) {
			Logger.getInstance().showErrorAlert(e);
		}
		if(goodQuality) {
			fichierMp4 = video.convertToMp4GoodQuality(folderMp4);
		}
		else
			fichierMp4 = video.convertToMp4(folderMp4);
		listeMp4.add(fichierMp4);
		for(String extension : listeExtensions) {
			File folderVideo = new File(folder + "\\" + extension.toLowerCase());
			if(!folderVideo.exists())
				folderVideo.mkdirs();
			String titre = Utils.getVideoTitleWithoutIllegalChar(video.getTitre());
			System.out.println(titre);
			fichier = new File(folderVideo.getPath() + "\\" + titre + "." + extension.toLowerCase());
			if(extension.equalsIgnoreCase("mp3")) {
				convertisseur = new ConvertisseurMusique(bitRate);
			}
			else
				convertisseur = new ConvertisseurVideo(extension, bitRate, true);
			if(!extension.equalsIgnoreCase("mp4")) {
				fichier = convertisseur.convertir(fichierMp4, fichier);
			}
		}
		return fichier;
	}
	
	public void deleteFolder(File folder, boolean hasMp4) {
		if(!hasMp4) {
			try {
				deleteFile(folder);
			} catch (IOException e) {
				Logger.getInstance().showErrorAlert(e);
			}
		}
	}

}
