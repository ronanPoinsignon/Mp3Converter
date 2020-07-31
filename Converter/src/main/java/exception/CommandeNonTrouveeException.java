package exception;

/**
 * 
 * @author ronan
 *
 */
public class CommandeNonTrouveeException extends Exception {

	private static final String message = "La commande n'est pas dans le gestionnaire";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CommandeNonTrouveeException() {
		super(message);
	}

}
