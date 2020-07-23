package commande;

import java.util.List;

import affichage.demande.TableVideo;
import prog.video.Video;

public class CommandeListe extends Commande {

	protected List<Video> listeVideos;

	public CommandeListe(TableVideo table, List<Video> listeVideos) {
		super(table);
		this.listeVideos = listeVideos;
	}

	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean annuler() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reexecute() {
		// TODO Auto-generated method stub
		return false;
	}

}
