package kukaWii.wiiHandle.Security;

import kukaWii.main.Main;

/**
 * Eigene Exception um einen gezielten Abbruch zu realisieren.
 * Dabei wird die Verbindung abgebaut.
 * @author Kai
 *
 */
public class InterruptException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2653202130797959296L;

	public InterruptException(String reason){
		super(reason);
		Main.getInstance().getCollector().panicInterrupt();
	}
}
