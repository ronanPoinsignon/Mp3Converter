package commande;

import java.util.List;

import affichage.demande.TableVideo;
import prog.video.Video;

public abstract class CommandeListe extends Commande {

	protected List<Video> listeVideos;

	public CommandeListe(TableVideo table, List<Video> listeVideos) {
		super(table);
		this.listeVideos = listeVideos;
	}

}
