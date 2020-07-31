package tache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.YoutubeException.BadPageException;

import log.Logger;
import prog.video.Video;
import prog.video.VideoFichier;
import prog.video.VideoYtb;

/**
 * Classe permettant de charger une ou plusieurs vidéos dans la liste de vidéo.
 * @author ronan
 *
 */
public class TacheCharger extends Tache<List<Video>>{

	private List<String> listeUrls = new ArrayList<>();

	private ArrayList<String> listeUrlsMauvaisLien = new ArrayList<>(), listeUrlsErreur = new ArrayList<>();
	
	public TacheCharger(List<String> listeUrls) {
		this.listeUrls = listeUrls;
	}
	
	public TacheCharger(String url) {
		listeUrls.add(url);
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
				File fichier = new File(url);
				if(fichier.exists()) {
					if(fichier.isFile())
						listeVideos.add(new VideoFichier(url));
					else {
						System.out.println("else");
						Logger.getInstance().showWarningAlertIsDIrectory();
					}
				}
				else
					listeVideos.add(new VideoYtb(url));
			} catch(BadPageException e) {
				listeUrlsMauvaisLien.add(url);
				e.printStackTrace();
			} catch (YoutubeException | IOException e) {
				listeUrlsErreur.add(url);
				e.printStackTrace();
			} catch(NullPointerException e) {
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
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
