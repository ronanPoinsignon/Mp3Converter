package commande;

import java.util.List;

import affichage.demande.TableVideo;
import prog.Video;

/**
 * Classe permettant la suppression de vidéos de la liste.
 * @author ronan
 *
 */
public class CommandeSuppression extends Commande {
	
	public CommandeSuppression(TableVideo table, List<Video> listeVideos) {
		super(table, listeVideos);
	}
	
	@Override
	public void execute() {
		List<Video> listeVideosNonPresentes = table.removeAll(listeVideos);
		listeVideos.removeAll(listeVideosNonPresentes);
	}

	@Override
	public void annuler() {
		table.addAll(listeVideos);
	}

	@Override
	public void reexecute() {
		table.removeAll(listeVideos);
	}

}
