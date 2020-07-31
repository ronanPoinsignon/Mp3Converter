package exception;

/**
 * 
 * @author ronan
 *
 */
public class PasDeResultatException extends Exception {

	private static final String message = "Aucun résultat n'a été donné";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PasDeResultatException() {
		super(message);
	}

}
