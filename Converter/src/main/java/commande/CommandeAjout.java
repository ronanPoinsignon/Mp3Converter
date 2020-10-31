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
public class CommandeAjout extends CommandeListe {
	
	public CommandeAjout(TableVideo table, List<Video> listeVideos) {
		super(table, listeVideos);
	}

	@Override
	public boolean executer() {
		List<Video> listeVideosDejaPresentes = table.addAll(listeVideos);
		listeVideos.removeAll(listeVideosDejaPresentes);
		Logger.getInstance().showWarningAlertVideosDejaPresentes(listeVideosDejaPresentes);
		return !listeVideos.isEmpty(); //si cette liste est vide, aucune vidéo n'a donc été ajoutée et cette commande est donc inutile
	}

	@Override
	public boolean annuler() {
		table.removeAll(listeVideos);
		return !listeVideos.isEmpty();
	}

	@Override
	public boolean reexecuter() {
		table.addAll(listeVideos);
		return !listeVideos.isEmpty();
	}

}
