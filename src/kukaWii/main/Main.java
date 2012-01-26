package kukaWii.main;

import kukaWii.wiiHandle.consumer.PersistenceConsumer;
import kukaWii.wiiHandle.internal.Connection;
import kukaWii.wiiHandle.provider.DataCollector;
import kukaWii.wiiHandle.security.SecurityService;
import motej.Mote;

/**
 * Nur diese Klasse besitzt die Security-Features.
 * @author Kai
 *
 */
public class Main {
	
	private Connection connection;
	private DataCollector collector;
	
	
	//Aus Sicherheitsgr�nden
	
	public static void main(String[] args){
		new Main();
	}
	
	
	private Main(){
		System.setProperty("InterruptCheck", "100");
		System.setProperty("Simulation", "false");
		connection = new Connection();
		Mote mote = connection.getRemote();
		collector = new DataCollector(mote);
		SecurityService.createSecurityService(collector);
		collector.addConsumer(new PersistenceConsumer());
	}

	public Connection getConnection(){
		return connection;
	}
	
	
			
}
