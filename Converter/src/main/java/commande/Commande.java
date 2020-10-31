package commande;

import affichage.demande.TableVideo;

/**
 * Classe abstraite permettant d'effectuer des actions sur la liste de vidéos.
 * @author ronan
 *
 */
public abstract class Commande implements CommandeInterface {
	
	protected TableVideo table;
	
	public Commande(TableVideo table) {
		this.table = table;
	}
	
}
