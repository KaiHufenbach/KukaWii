package kukaWii.main;

import kukaWii.wiiHandle.PersistenceConsumer;
import kukaWii.wiiHandle.PersistenceProvider;

public class MainWOWii {
	
	private static MainWOWii singleton;
	
	public static void main(String[] args){
		singleton = new MainWOWii();
	}
	
	private MainWOWii(){
		PersistenceProvider provider = new PersistenceProvider();
		
		//Hier können dem Provider mit addConsumer ebensolche hinzugefügt werden
		
		provider.start();
		
	}
}
