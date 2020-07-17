package commande;

import java.util.ArrayList;
import java.util.List;

import affichage.demande.TableVideo;
import prog.Video;

public class CommandeReset extends Commande {

	List<Video> listeVideosSupprimees = new ArrayList<>();
	
	public CommandeReset(TableVideo table, List<Video> listeVideos) {
		super(table, listeVideos);
	}

	@Override
	public void execute() {
		System.out.println("oui");
		listeVideosSupprimees = table.removeAll();
		table.addAll(listeVideos);
	}

	@Override
	public void annuler() {
		table.removeAll();
		table.addAll(listeVideosSupprimees);
	}

	@Override
	public void reexecute() {
		table.removeAll();
		table.addAll(listeVideos);
	}

}
