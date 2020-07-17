package exception;

/**
 * 
 * @author ronan
 *
 */
public class VideoDejaPresenteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public VideoDejaPresenteException() {
		super("La vidéo est déjà présente dans la table");
	}

}
