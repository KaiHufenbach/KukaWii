package kukaWii.wiiHandle.Security;


/**
 * Eigene Exception um einen gezielten Abbruch zu realisieren.
 * Dabei wird die Verbindung abgebaut.
 * @author Kai
 *
 */
class InterruptException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2653202130797959296L;

	public InterruptException(String reason){
		super(reason);
	}
}
