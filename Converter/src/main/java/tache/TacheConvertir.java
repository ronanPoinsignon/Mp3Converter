package tache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kiulian.downloader.YoutubeException;

import affichage.demande.TableVideo;
import it.sauronsoftware.jave.EncoderException;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import log.Logger;
import prog.Converter;
import prog.Downloader;
import prog.Video;

/**
 * Classe permettant la conversion des vidéos de la liste.
 * @author ronan
 *
 */
public class TacheConvertir extends Task<List<File>>{

	private TableVideo table;
	private File folder;
	private int bitRate;
	
	public TacheConvertir(TableVideo table, File folder, int bitRate) {
		this.table = table;
		this.folder = folder;
		this.bitRate = bitRate;
	}
	
	@Override
	protected List<File> call() throws Exception {
		ArrayList<File> listeFichiers = new ArrayList<>();
		if(folder == null)
			return null;
		Downloader downloader = new Downloader();
		Converter converter = new Converter();
		ObservableList<Video> listeVideos = table.getItems();
		int tailleListe = listeVideos.size();
		int cpt = 0;
		for(Video video : listeVideos) {
			 this.update(video);
			try {
				File folderMp4 = new File(folder + "\\mp4");
				if(!folderMp4.exists())
					folderMp4.mkdirs();
				
				File folderMp3 = new File(folder + "\\mp3");
				if(!folderMp3.exists())
					folderMp3.mkdirs();
				
				File videoMp4 = downloader.download(folderMp4, video.getId());
				File fichier = new File(folderMp3.getPath() + "\\" + videoMp4.getName().substring(0, videoMp4.getName().lastIndexOf(".")) + ".mp3");
				converter.encode(videoMp4, fichier, bitRate);
				listeFichiers.add(fichier);
			} catch (YoutubeException | IOException | IllegalArgumentException | EncoderException e) {
				Logger.getInstance().showErrorAlert(e);
			}
			this.updateProgress(++cpt, tailleListe);
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

}
