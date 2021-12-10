package exception;

/**
 *
 * @author ronan
 *
 */
public class VideoNonTrouveeException extends Exception {

	private static final String MESSAGE = "La vidéo n'est pas dans la liste.";

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public VideoNonTrouveeException() {
		super(VideoNonTrouveeException.MESSAGE);
	}

}
