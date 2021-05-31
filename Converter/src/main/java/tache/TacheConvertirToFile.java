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

import exception.NoVideoFoundException;
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
		try {
			List<Video> listeMauvaisFichiers = new ArrayList<>();
			List<Video> listeVideoSansTelechargement = new ArrayList<>();
			int tailleListe = listeVideos.size();
			boolean hasMp4;
			int cpt = 0;

			if(folder == null) {
				return new ArrayList<>();
			}
			if(listeExtensions.isEmpty()) {
				return new ArrayList<>();
			}
			hasMp4 = listeExtensions.contains("mp4");
			File folderMp4 = null;
			if(hasMp4) {
				folderMp4 = new File(folder.getPath() + File.separator + "mp4");
			}
			else {
				folderMp4 = new File(folder.getPath() + File.separator + "mp4H");
			}
			folderMp4.mkdirs();
			if(hasMp4) {
				listeExtensions.remove("mp4");
			} else {
				try {
					Files.setAttribute(Paths.get(folderMp4.getPath()), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
				} catch (IOException e) {
					Logger.getInstance().showErrorAlert(e);
					e.printStackTrace();
				}
			}
			for(Video video : listeVideos) {
				try {
					listeFichiers.add(convertVideo(video, folder, folderMp4, hasMp4));
				}
				catch(EncoderException e) {
					e.printStackTrace();
					if(!listeMauvaisFichiers.contains(video)) {
						listeMauvaisFichiers.add(video);
					}
				}
				catch(NoVideoFoundException e) {
					e.printStackTrace();
					listeVideoSansTelechargement.add(video);
				}
				catch(Exception e) {
					e.printStackTrace();
					listeMauvaisFichiers.add(video);
				}
				this.updateProgress(++cpt, tailleListe);
			}
			Logger.getInstance().showWarningAlertIsNotVideoFile(listeMauvaisFichiers);
			Logger.getInstance().showErrorAlertNoVideoFound(listeVideoSansTelechargement);
			try {
				if(!hasMp4) {
					deleteFile(folderMp4);
				}
			} catch (IOException e) {
				Logger.getInstance().showErrorAlert(e);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return listeFichiers;
	}

	/**
	 * Modifie le message pour qu'il soit conforme avec le déroulement de la tâche.
	 * @param video
	 * @throws Exception
	 */
	private void update(Video video) {
		updateMessage("en téléchargement : " + video.getTitre());
	}

	private void deleteFile(File folder) throws IOException {
		Files.walk(folder.toPath())
		.sorted(Comparator.reverseOrder())
		.map(Path::toFile)
		.forEach(File::delete);
	}

	/**
	 * Covertit la {@link Video} en fichier vidéo.
	 * @param video
	 * @param folder
	 * @param folderMp4
	 * @param goodQuality
	 * @return
	 * @throws Exception
	 */
	private File convertVideo(Video video, File folder, File folderMp4, boolean goodQuality) throws Exception {
		ArrayList<File> listeMp4 = new ArrayList<>();
		File fichier = null, fichierMp4 = null;
		Convertisseur convertisseur = null;
		try {
			update(video);
		} catch (Exception e) {
			Logger.getInstance().showErrorAlert(e);
		}
		if(goodQuality) {
			fichierMp4 = video.convertToMp4GoodQuality(folderMp4);
		} else {
			fichierMp4 = video.convertToMp4(folderMp4);
		}
		listeMp4.add(fichierMp4);
		String titre = Utils.removeIllegalChars(video.getTitre());
		for(String extension : listeExtensions) {
			updateMessage("conversion de " + titre + " en " + extension);
			File folderVideo = new File(folder + File.separator + extension.toLowerCase());
			if(!folderVideo.exists()) {
				folderVideo.mkdirs();
			}
			fichier = new File(folderVideo.getPath() + File.separator + titre + "." + extension.toLowerCase());
			if(extension.equalsIgnoreCase("mp3")) {
				convertisseur = new ConvertisseurMusique(bitRate);
			} else {
				convertisseur = new ConvertisseurVideo(extension, bitRate, true);
			}
			if(!extension.equalsIgnoreCase("mp4")) {
				fichier = convertisseur.convertir(fichierMp4, fichier);
			}
		}
		return fichier;
	}

	/**
	 * Supprime le fichier donné en paramètre.
	 * @param folder
	 * @param hasMp4
	 */
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
