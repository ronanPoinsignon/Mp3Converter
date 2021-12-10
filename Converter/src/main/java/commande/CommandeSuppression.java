package commande;

import java.util.ArrayList;
import java.util.List;

import affichage.demande.TableVideo;
import exception.VideoDejaPresenteException;
import prog.video.Video;

/**
 * Classe permettant la suppression de vidéos de la liste.
 * @author ronan
 *
 */
public class CommandeSuppression extends CommandeListe {

	private List<Integer> listeIndex = new ArrayList<>();

	public CommandeSuppression(TableVideo table, List<Video> listeVideos) {
		super(table, listeVideos);
	}

	@Override
	public boolean executer() {
		for(Video video : listeVideos) {
			listeIndex.add(table.getItems().indexOf(video));
		}
		List<Video> listeVideosNonPresentes = table.removeAll(listeVideos);
		listeVideos.removeAll(listeVideosNonPresentes);
		return !listeVideos.isEmpty();
	}

	@Override
	public boolean annuler() {
		for(int i = 0; i < listeVideos.size(); i++) {
			try {
				int index = listeIndex.get(i);
				table.add(listeVideos.get(i), index);
			} catch (UnsupportedOperationException | ClassCastException | NullPointerException
					| IllegalArgumentException | VideoDejaPresenteException e) {
				e.printStackTrace();
			}
		}
		listeIndex = new ArrayList<>();
		return !listeVideos.isEmpty();
	}

}
