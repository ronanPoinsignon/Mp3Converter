package commande;

import java.util.ArrayList;
import java.util.List;

import affichage.demande.TableVideo;
import prog.video.Video;

/**
 * Commande permettant de changer la table existante en une nouvelle.
 * @author ronan
 *
 */
public class CommandeReset extends CommandeListe {

	List<Video> listeVideosSupprimees = new ArrayList<>();

	public CommandeReset(TableVideo table, List<Video> listeVideos) {
		super(table, listeVideos);
	}

	@Override
	public boolean executer() {
		if(table.getItems().equals(listeVideos)) {
			return false;
		}
		listeVideosSupprimees = table.removeAll();
		table.addAll(listeVideos);
		return !listeVideos.isEmpty();
	}

	@Override
	public boolean annuler() {
		table.removeAll();
		table.addAll(listeVideosSupprimees);
		return !listeVideos.isEmpty();
	}

}
