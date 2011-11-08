package kukaWii.main;

import kukaWii.wiiHandle.Connection;
import kukaWii.wiiHandle.DataCollector;
import kukaWii.wiiHandle.PersistenceConsumer;
import motej.Mote;


public class Main {
	
	private static Main singleton;
	private Connection connection;
	
	
	public static void main(String[] args){
		singleton = new Main();
	}
	
	
	private Main(){
		connection = new Connection();
		Mote mote = connection.getRemote();
		DataCollector collector = new DataCollector(mote);
		collector.addConsumer(new PersistenceConsumer());
	}


	public static Main getController() {
		return singleton;
	}
	
	public Connection getConnection(){
		return connection;
	}
	
	
			
}
