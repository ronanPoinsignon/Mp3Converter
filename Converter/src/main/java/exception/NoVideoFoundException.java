package exception;

public class NoVideoFoundException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "Aucune vidéo n'a pu être trouvée";

	public NoVideoFoundException() {
		super(NoVideoFoundException.MESSAGE);
	}

}
