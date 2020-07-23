package commande;

import affichage.demande.TableVideo;

/**
 * Classe abstraite permettant d'effectuer des actions sur la liste de vidéos.
 * @author ronan
 *
 */
public abstract class Commande {
	
	protected TableVideo table;
	
	public Commande(TableVideo table) {
		this.table = table;
	}

	/**
	 * Execute la commande.
	 */
	public abstract boolean execute();
	
	/**
	 * Annule l'exécution/
	 */
	public abstract boolean annuler();
	
	/**
	 * Réexécute la commande après annulation de celle-ci.
	 */
	public abstract boolean reexecute();
}
