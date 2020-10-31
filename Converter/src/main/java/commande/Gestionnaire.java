package commande;

import java.util.ArrayList;

import exception.CommandeNonTrouveeException;

/**
 * Classe permettant de gérer les différentes commandes à effectuer.
 * @author ronan
 *
 */
public class Gestionnaire {

	private static Gestionnaire gestionnaire = null;
	
	public static Gestionnaire getInstance() {
		if(gestionnaire == null)
			gestionnaire = new Gestionnaire();
		return gestionnaire;
	}
	
	private ArrayList<CommandeInterface> listeCommandes = new ArrayList<CommandeInterface>();
	private ArrayList<CommandeInterface> listeCommandesEffectuees = new ArrayList<CommandeInterface>();
	private ArrayList<CommandeInterface> listeCommandesAnnulees = new ArrayList<CommandeInterface>();
	private ArrayList<CommandeInterface> listeCommandesAReexecuteer = new ArrayList<CommandeInterface>();
	
	private int cptActions = 0;
	
	private Gestionnaire() {
		
	}
	
	/**
	 * Ajoute une commande à la liste des commandes devant être effectuées.
	 * @param commande
	 */
	public Gestionnaire addCommande(Commande commande) {
		listeCommandes.add(commande);
		return this;
	}
	
	/**
	 * Exécute la prochaine commande de la liste des commandes à faire.
	 */
	public Gestionnaire executer() {
		CommandeInterface commande = listeCommandes.remove(0);
		if(commande.executer()) {
			listeCommandesEffectuees.add(commande);
			listeCommandesAReexecuteer.removeAll(listeCommandesAReexecuteer);
			cptActions++;
		}
		return this;
	}
	
	/**
	 * Exécute toute les commandes de la liste des commandes à faire.
	 */
	public Gestionnaire executerAll() {
		for(CommandeInterface commande : listeCommandes) {
			if(commande.executer()) {
				listeCommandesEffectuees.add(commande);
				listeCommandes.remove(commande);
				cptActions++;
			}
		}
		listeCommandesAReexecuteer.removeAll(listeCommandes);
		return this;
	}
	
	/**
	 * Exécute une commande de la liste des commandes à faire. Renvoie une exception 
	 * si la commande ne se trouve pas dans cette liste.
	 * @param commande
	 * @throws CommandeNonTrouveeException
	 */
	public Gestionnaire executer(Commande commande) throws CommandeNonTrouveeException {
		if(!listeCommandes.contains(commande))
			throw new CommandeNonTrouveeException();
		if(commande.executer()) {
			commande.executer();
			listeCommandes.remove(commande);
			listeCommandesEffectuees.add(commande);
			listeCommandesAReexecuteer.removeAll(listeCommandes);
			cptActions++;
		}
		return this;
	}
	
	public Gestionnaire executerInstant(Commande commande) {
		commande.executer();
		return this;
	}
	
	/**
	 * Annule la dernière commande effectuée.
	 */
	public Gestionnaire annuler() {
		CommandeInterface commande = listeCommandesEffectuees.remove(listeCommandesEffectuees.size() - 1);
		if(commande.annuler()) {
			listeCommandesAnnulees.add(commande);
			listeCommandesAReexecuteer.add(commande);
			cptActions--;
		}
		return this;
	}
	
	/**
	 * Réexécute la commande annulée.
	 */
	public Gestionnaire reexecuter() {
		CommandeInterface commande = listeCommandesAReexecuteer.remove(listeCommandesAReexecuteer.size() - 1);
		if(commande.reexecuter()) {
			listeCommandesEffectuees.add(commande);
			cptActions++;
		}
		return this;
	}
	
	public Gestionnaire clean() {
		listeCommandes.removeAll(listeCommandes);
		listeCommandesAnnulees.removeAll(listeCommandesAnnulees);
		listeCommandesAReexecuteer.removeAll(listeCommandesAReexecuteer);
		listeCommandesEffectuees.removeAll(listeCommandesEffectuees);
		return this;
	}
	
	/**
	 * Retourne TRUE si il est possible de sauvegarder sa liste de vidéos.
	 * @return
	 */
	public boolean canSave() {
		return cptActions != 0;
	}
	
	/**
	 * Retourne TRUE si il est possible d'annuler la dernière commande effectuée.
	 * @return
	 */
	public boolean canAnnuler() {
		return !listeCommandesEffectuees.isEmpty();
	}
	
	/**
	 * Retourne TRUE si il est possible de refaire la dernière commande annulée.
	 * @return
	 */
	public boolean canReexecuter() {
		return !listeCommandesAReexecuteer.isEmpty();
	}
	
	public void notifySave() {
		cptActions = 0;
	}
}
