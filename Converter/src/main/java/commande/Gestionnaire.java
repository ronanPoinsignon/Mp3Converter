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
	private ArrayList<Commande> listeCommandes = new ArrayList<Commande>();
	private ArrayList<Commande> listeCommandesEffectuees = new ArrayList<Commande>();
	private ArrayList<Commande> listeCommandesAnnulees = new ArrayList<Commande>();
	private ArrayList<Commande> listeCommandesAReexecuteer = new ArrayList<Commande>();
	
	private Gestionnaire() {
		
	}
	
	/**
	 * Ajoute une commande à la liste des commandes devant être effectuées.
	 * @param commande
	 */
	public void addCommande(Commande commande) {
		listeCommandes.add(commande);
	}
	
	/**
	 * Exécute la prochaine commande de la liste des commandes à faire.
	 */
	public void executer() {
		Commande commande = listeCommandes.remove(0);
		commande.execute();
		listeCommandesEffectuees.add(commande);
		listeCommandesAReexecuteer.removeAll(listeCommandesAReexecuteer);
	}
	
	/**
	 * Exécute toute les commandes de la liste des commandes à faire.
	 */
	public void executerAll() {
		for(Commande commande : listeCommandes) {
			commande.execute();
			listeCommandesEffectuees.add(commande);
			listeCommandes.remove(commande);
		}
		listeCommandesAReexecuteer.removeAll(listeCommandes);
	}
	
	/**
	 * Exécute une commande de la liste des commandes à faire. Renvoie une exception 
	 * si la commande ne se trouve pas dans cette liste.
	 * @param commande
	 * @throws CommandeNonTrouveeException
	 */
	public void executer(Commande commande) throws CommandeNonTrouveeException {
		if(!listeCommandes.remove(commande))
			throw new CommandeNonTrouveeException();
		commande.execute();
		listeCommandesEffectuees.add(commande);
		listeCommandesAReexecuteer.removeAll(listeCommandes);
	}
	
	/**
	 * Annule la dernière commande effectuée.
	 */
	public void annuler() {
		Commande commande = listeCommandesEffectuees.remove(listeCommandesEffectuees.size() - 1);
		commande.annuler();
		listeCommandesAnnulees.add(commande);
		listeCommandesAReexecuteer.add(commande);
	}
	
	/**
	 * Réexécute la commande annulée.
	 */
	public void reexecuter() {
		Commande commande = listeCommandesAReexecuteer.remove(listeCommandesAReexecuteer.size() - 1);
		commande.reexecute();
		listeCommandesEffectuees.add(commande);
	}
	
	/**
	 * Retourne TRUE si il est possible de sauvegarder sa liste de vidéos.
	 * @return
	 */
	public boolean canSave() {
		return !listeCommandesEffectuees.isEmpty();
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
}