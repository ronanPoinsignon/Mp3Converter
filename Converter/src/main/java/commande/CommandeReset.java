package commande;

import java.util.ArrayList;
import java.util.List;

import affichage.demande.TableVideo;
import prog.video.Video;

public class CommandeReset extends Commande {

	List<Video> listeVideosSupprimees = new ArrayList<>();
	
	public CommandeReset(TableVideo table, List<Video> listeVideos) {
		super(table, listeVideos);
	}

	@Override
	public boolean execute() {
		listeVideosSupprimees = table.removeAll();
		table.addAll(listeVideos);
		return !listeVideos.isEmpty();
	}

	@Override
	public boolean annuler() {
		table.removeAll();
		table.addAll(listeVideosSupprimees);
		return !listeVideosSupprimees.isEmpty();
	}

	@Override
	public boolean reexecute() {
		table.removeAll();
		table.addAll(listeVideos);
		return !listeVideos.isEmpty();
	}

}
