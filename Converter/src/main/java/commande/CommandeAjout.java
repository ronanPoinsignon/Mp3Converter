package commande;

import java.util.List;

import affichage.demande.TableVideo;
import log.Logger;
import prog.video.Video;

/**
 * Classe permettant l'ajout de vidéos à la liste.
 * @author ronan
 *
 */
public class CommandeAjout extends Commande {
	
	public CommandeAjout(TableVideo table, List<Video> listeVideos) {
		super(table, listeVideos);
	}

	@Override
	public boolean execute() {
		List<Video> listeVideosDejaPresentes = table.addAll(listeVideos);
		listeVideos.removeAll(listeVideosDejaPresentes);
		if(!listeVideosDejaPresentes.isEmpty())
			Logger.getInstance().showWarningAlertVideosDejaPresentes(listeVideosDejaPresentes);
		return !listeVideos.isEmpty();
	}

	@Override
	public boolean annuler() {
		table.removeAll(listeVideos);
		return !listeVideos.isEmpty();
	}

	@Override
	public boolean reexecute() {
		List<Video> listeVideosDejaPresentes = table.addAll(listeVideos);
		Logger.getInstance().showWarningAlertVideosDejaPresentes(listeVideosDejaPresentes);
		return !listeVideos.isEmpty();
	}

}
