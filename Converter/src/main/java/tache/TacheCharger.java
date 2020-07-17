package tache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.YoutubeException.BadPageException;

import javafx.concurrent.Task;
import prog.Video;

/**
 * Classe permettant de charger une ou plusieurs vidéos dans la liste de vidéo.
 * @author ronan
 *
 */
public class TacheCharger extends Task<List<Video>>{

	private List<String> listeUrls;

	private ArrayList<String> listeUrlsMauvaisLien = new ArrayList<>(), listeUrlsErreur = new ArrayList<>();
	
	public TacheCharger(List<String> listeUrls) {
		this.listeUrls = listeUrls;
	}
	
	@Override
	protected List<Video> call() {
		listeUrlsMauvaisLien = new ArrayList<>();
		listeUrlsErreur = new ArrayList<>();
		ArrayList<Video> listeVideos = new ArrayList<>();
		int cpt = 0, tailleListe = listeUrls.size();
		for(String url : listeUrls) {
			try {
				this.updateMessage("chargement de " + url);
				listeVideos.add(new Video(url));
			} catch(BadPageException e) {
				listeUrlsMauvaisLien.add(url);
			} catch (YoutubeException | IOException e) {
				listeUrlsErreur.add(url);
			}
			this.updateProgress(++cpt, tailleListe);
		}
		return listeVideos;
	}

	/**
	 * Retourne un clone de la liste d'url de vidéos étant déjà présentes dans la liste de vidéos.
	 * @return
	 */
	public ArrayList<String> getListeUrlsMauvaisLien() {
		ArrayList<String> nouvelleListeUrls = new ArrayList<>();
		nouvelleListeUrls.addAll(listeUrlsMauvaisLien);
		return nouvelleListeUrls;
	}

	/**
	 * Retourne un clone de la liste d'url ayant eu une erreur après avoir charger la liste de vidéos.
	 * @return
	 */
	public ArrayList<String> getListeUrlsErreur() {
		ArrayList<String> nouvelleListeUrls = new ArrayList<>();
		nouvelleListeUrls.addAll(listeUrlsErreur);
		return nouvelleListeUrls;
	}
	
}
