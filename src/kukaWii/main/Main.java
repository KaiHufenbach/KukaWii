package kukaWii.main;

import kukaWii.wiiHandle.Consumer.PersistenceConsumer;
import kukaWii.wiiHandle.Internal.Connection;
import kukaWii.wiiHandle.Provider.DataCollector;
import kukaWii.wiiHandle.Security.SecurityService;
import motej.Mote;

/**
 * Nur diese Klasse besitzt die Security-Features.
 * @author Kai
 *
 */
public class Main {
	
	private Connection connection;
	private DataCollector collector;
	
	
	//Aus Sicherheitsgründen
	
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
