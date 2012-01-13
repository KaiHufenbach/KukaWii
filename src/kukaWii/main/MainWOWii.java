package kukaWii.main;

import kukaWii.wiiHandle.Connection;
import kukaWii.wiiHandle.PersistenceConsumer;
import kukaWii.wiiHandle.PersistenceProvider;
import kukaWii.wiiHandle.Packet.Filter.FileReader;
import kukaWii.wiiHandle.Packet.Filter.MovingAverageFilter;
import kukaWii.wiiHandle.Packet.Filter.NearestNeighbourFilter;

public class MainWOWii extends AbstractMain {
	
	public static void main(String[] args){
		singleton = new MainWOWii();
	}
	
	private MainWOWii(){
		setSimulation(true);
		PersistenceProvider provider = new PersistenceProvider();
		//Hier können dem Provider mit addConsumer ebensolche hinzugefügt werden
		provider.start();
		
		System.setProperty("InterruptCheck", "100");
		
		MovingAverageFilter movingAverageFilter = new MovingAverageFilter();
		NearestNeighbourFilter nearestNeighbourFilter = new NearestNeighbourFilter();
		FileReader fileReader = new FileReader();
		PersistenceConsumer consumer = new PersistenceConsumer();
		
		provider.addFilter(fileReader);
		provider.addFilter(movingAverageFilter);
		provider.addFilter(nearestNeighbourFilter);
		provider.addConsumer(consumer);
	}
}
