package commande;

import java.util.List;

import affichage.demande.TableVideo;
import prog.Video;

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
	public abstract void execute();
	
	/**
	 * Annule l'exécution/
	 */
	public abstract void annuler();
	
	/**
	 * Réexécute la commande après annulation de celle-ci.
	 */
	public abstract void reexecute();
}
