package exception;

public class NoVideoFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "Aucune vidéo n'a pu être trouvée";
	
	public NoVideoFoundException() {
		super(message);
	}

}
