package affichage.demande;

import java.util.ArrayList;
import java.util.List;

import exception.VideoDejaPresenteException;
import exception.VideoNonTrouveeException;
import javafx.scene.control.TableView;
import prog.video.Video;

/**
 * Classe gérant la liste de vidéos.
 * @author ronan
 *
 */
public class TableVideo extends TableView<Video> {

	/**
	 * Ajoute la vidéo dans la liste. Renvoie une erreur si la vidéo est déjà présente.
	 * Renvoie TRUE si l'insertion a fonctionné.
	 * @param video
	 * @return
	 * @throws VideoDejaPresenteException
	 */
	public boolean add(Video video) throws VideoDejaPresenteException {
		if(video == null) {
			return false;
		}
		if(getItems().contains(video)) {
			throw new VideoDejaPresenteException();
		}
		return getItems().add(video);
	}

	/**
	 * Ajoute la vidéo dans la liste. Renvoie une erreur si la vidéo est déjà présente.
	 * Renvoie TRUE si l'insertion a fonctionné.
	 * @param video
	 * @return
	 * @throws VideoDejaPresenteException
	 */
	public boolean add(Video video, int index) throws VideoDejaPresenteException, UnsupportedOperationException,
	ClassCastException, NullPointerException, IllegalArgumentException {
		if(video == null) {
			return false;
		}
		if(getItems().contains(video)) {
			throw new VideoDejaPresenteException();
		}
		getItems().add(index, video);
		return true;
	}

	/**
	 * Ajoute toutes les vidéos à la liste. Renvoie les vidéos qui sont déjà dans la liste.
	 * @param listeVideos
	 * @return
	 */
	public List<Video> addAll(List<Video> listeVideosASuppr) {
		ArrayList<Video> listeVideoDejaPresentes = new ArrayList<>();
		for(Video video : listeVideosASuppr) {
			try {
				this.add(video);
			} catch (VideoDejaPresenteException e) {
				listeVideoDejaPresentes.add(video);
			}
		}
		return listeVideoDejaPresentes;
	}

	/**
	 * Supprime une vidéo de la liste à partir de son index.
	 * @param index
	 * @return
	 */
	public Video remove(int index) {
		return getItems().remove(index);
	}

	/**
	 * Supprime la vidéo de la liste. Renvoie une erreur si la vidéo demandée n'est pas dans la liste.
	 * @param video
	 * @return
	 * @throws VideoNonTrouveeException
	 */
	public boolean remove(Video video) throws VideoNonTrouveeException {
		if(!getItems().contains(video)) {
			throw new VideoNonTrouveeException();
		}
		return getItems().remove(video);
	}

	/**
	 * Supprime toutes les vidéos de la liste. Renvoie TRUE si la suppression a fonctionné.
	 * @return
	 */
	public List<Video> removeAll() {
		List<Video> listeVideosRm = new ArrayList<>(getItems());
		getItems().removeAll(listeVideosRm);
		return listeVideosRm;
	}

	/**
	 * Supprime les vidéos données de la liste de vidéos. Renvoie les vidéos qui n'étaient pas dans la liste.
	 * @param listeVideos
	 * @return
	 */
	public List<Video> removeAll(List<Video> listeVideos) {
		ArrayList<Video> listeVideosASuppr = new ArrayList<>(listeVideos);
		ArrayList<Video> listeVideoNonPresentes = new ArrayList<>();
		for(Video video : listeVideosASuppr) {
			try {
				this.remove(video);
			} catch (VideoNonTrouveeException e) {
				listeVideoNonPresentes.add(video);
			}
		}
		return listeVideoNonPresentes;
	}
}
