package affichage.demande;

import java.util.ArrayList;
import java.util.List;

import exception.VideoDejaPresenteException;
import exception.VideoNonTrouveeException;
import javafx.scene.control.TableView;
import prog.Video;

/**
 * Classe gérant la liste de vidéos.
 * @author ronan
 *
 */
public class TableVideo extends TableView<Video> {

	private ArrayList<Video> listeVideos = new ArrayList<>();
	
	public TableVideo() {
		
	}
	
	/**
	 * Ajoute la vidéo dans la liste. Renvoie une erreur si la vidéo est déjà présente.
	 * Renvoie TRUE si l'insertion a fonctionné.
	 * @param video
	 * @return
	 * @throws VideoDejaPresenteException
	 */
	public boolean add(Video video) throws VideoDejaPresenteException {
		if(video == null)
			return false;
		if(listeVideos.contains(video))
			throw new VideoDejaPresenteException();
		this.getItems().add(video);
		return this.listeVideos.add(video);
	}
	
	/**
	 * Ajoute toutes les vidéos à la liste. Renvoie les vidéos qui sont déjà dans la liste.
	 * @param listeVideos
	 * @return
	 */
	public List<Video> addAll(List<Video> listeVideos) {
		ArrayList<Video> listeVideoDejaPresentes = new ArrayList<>();
		for(Video video : listeVideos)
			try {
				this.add(video);
				this.listeVideos.add(video);
			} catch (VideoDejaPresenteException e) {
				listeVideoDejaPresentes.add(video);
			}
		return listeVideoDejaPresentes;
	}
	
	/**
	 * Supprime une vidéo de la liste à partir de son index.
	 * @param index
	 * @return
	 */
	public Video remove(int index) {
		this.getItems().remove(index);
		return this.listeVideos.remove(index);
	}
	
	/**
	 * Supprime la vidéo de la liste. Renvoie une erreur si la vidéo demandée n'est pas dans la liste.
	 * @param video
	 * @return
	 * @throws VideoNonTrouveeException
	 */
	public boolean remove(Video video) throws VideoNonTrouveeException {
		if(!this.getItems().contains(video))
			throw new VideoNonTrouveeException();
		this.getItems().remove(video);
		return this.listeVideos.remove(video);
	}
	
	/**
	 * Supprime toutes les vidéos de la liste. Renvoie TRUE si la suppression a fonctionné.
	 * @return
	 */
	public boolean removeAll() {
		this.getItems().removeAll(this.getItems());
		return this.listeVideos.removeAll(this.listeVideos);
	}
	
	/**
	 * Supprime les vidéos données de la liste de vidéos. Renvoie les vidéos qui n'étaient pas dans la liste.
	 * @param listeVideos
	 * @return
	 */
	public ArrayList<Video> removeAll(List<Video> listeVideos) {
		ArrayList<Video> listeVideoNonPresentes = new ArrayList<>();
		for(Video video : listeVideos)
			try {
				this.remove(video);
				this.listeVideos.remove(video);
			} catch (VideoNonTrouveeException e) {
				listeVideoNonPresentes.add(video);
			}
		return listeVideoNonPresentes;
	}
}
