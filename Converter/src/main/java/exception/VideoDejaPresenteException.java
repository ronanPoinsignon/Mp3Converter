package exception;

/**
 *
 * @author ronan
 *
 */
public class VideoDejaPresenteException extends Exception {

	private static final String MESSAGE = "La vidéo est déjà présente dans la table";

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public VideoDejaPresenteException() {
		super(VideoDejaPresenteException.MESSAGE);
	}

}
