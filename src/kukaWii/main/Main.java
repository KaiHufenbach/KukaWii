package kukaWii.main;

import kukaWii.wiiHandle.Connection;
import kukaWii.wiiHandle.DataCollector;
import kukaWii.wiiHandle.PersistenceConsumer;
import motej.Mote;

/**
 * Nur diese Klasse besitzt die Security-Features.
 * @author Kai
 *
 */
public class Main extends AbstractMain {
	
	private Connection connection;
	
	
	//Aus Sicherheitsgründen
	
	public static void main(String[] args){
		singleton = new Main();
	}
	
	
	private Main(){
		System.setProperty("InterruptCheck", "100");
		connection = new Connection();
		Mote mote = connection.getRemote();
		collector = new DataCollector(mote);
		collector.addConsumer(new PersistenceConsumer());
	}

	public Connection getConnection(){
		return connection;
	}
	
	
			
}
