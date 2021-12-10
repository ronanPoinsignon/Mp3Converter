package exception;

/**
 *
 * @author ronan
 *
 */
public class PasDeResultatException extends Exception {

	private static final String MESSAGE = "Aucun résultat n'a été trouvé";

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public PasDeResultatException() {
		super(PasDeResultatException.MESSAGE);
	}

}
