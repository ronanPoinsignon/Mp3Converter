package commande;

import java.util.List;

import affichage.demande.TableVideo;
import prog.video.Video;

/**
 * Classe permettant la suppression de vidéos de la liste.
 * @author ronan
 *
 */
public class CommandeSuppression extends CommandeListe {
	
	public CommandeSuppression(TableVideo table, List<Video> listeVideos) {
		super(table, listeVideos);
	}
	
	@Override
	public boolean execute() {
		List<Video> listeVideosNonPresentes = table.removeAll(listeVideos);
		listeVideos.removeAll(listeVideosNonPresentes);
		return !listeVideos.isEmpty();
	}

	@Override
	public boolean annuler() {
		table.addAll(listeVideos);
		return !listeVideos.isEmpty();
	}

	@Override
	public boolean reexecute() {
		table.removeAll(listeVideos);
		return !listeVideos.isEmpty();
	}

}
