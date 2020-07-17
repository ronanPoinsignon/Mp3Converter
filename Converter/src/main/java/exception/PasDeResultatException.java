package exception;

/**
 * 
 * @author ronan
 *
 */
public class PasDeResultatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PasDeResultatException() {
		super("Aucun résultat n'a été donné");
	}

}
