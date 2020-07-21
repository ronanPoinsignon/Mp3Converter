package commande;

import java.util.List;

import affichage.demande.TableVideo;
import prog.video.Video;

/**
 * Classe abstraite permettant d'effectuer des actions sur la liste de vidéos.
 * @author ronan
 *
 */
public abstract class Commande {
	
	protected TableVideo table;
	protected List<Video> listeVideos;
	
	public Commande(TableVideo table, List<Video> listeVideos) {
		this.table = table;
		this.listeVideos = listeVideos;
	}

	/**
	 * Execute la commande.
	 */
	public abstract boolean execute();
	
	/**
	 * Annule l'exécution/
	 */
	public abstract boolean annuler();
	
	/**
	 * Réexécute la commande après annulation de celle-ci.
	 */
	public abstract boolean reexecute();
}
