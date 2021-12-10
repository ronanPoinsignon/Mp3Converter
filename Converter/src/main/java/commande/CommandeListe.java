package commande;

import java.util.List;

import affichage.demande.TableVideo;
import prog.video.Video;

/**
 * Classe abstraite correspondant à toutes les commandes devant accéder à la table en elle-même.
 * @author ronan
 *
 */
public abstract class CommandeListe extends Commande {

	protected List<Video> listeVideos;

	protected CommandeListe(TableVideo table, List<Video> listeVideos) {
		super(table);
		this.listeVideos = listeVideos;
	}

}
