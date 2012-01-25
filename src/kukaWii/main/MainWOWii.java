package kukaWii.main;

import kukaWii.wiiHandle.Consumer.DummyConsumer;
import kukaWii.wiiHandle.Consumer.PersistenceConsumer;
import kukaWii.wiiHandle.Filter.MovingAverageFilter;
import kukaWii.wiiHandle.Filter.NearestNeighbourFilter;
import kukaWii.wiiHandle.Internal.Connection;
import kukaWii.wiiHandle.Provider.PersistenceProvider;

public class MainWOWii{
	
	public static void main(String[] args){
		new MainWOWii();
	}
	
	private MainWOWii(){
		System.setProperty("Simulation", "true");
		PersistenceProvider provider = new PersistenceProvider();
		//Hier können dem Provider mit addConsumer ebensolche hinzugefügt werden
		
		System.setProperty("InterruptCheck", "100");
		
		MovingAverageFilter movingAverageFilter = new MovingAverageFilter();
		NearestNeighbourFilter nearestNeighbourFilter = new NearestNeighbourFilter();
		DummyConsumer consumer = new DummyConsumer();
		
		//provider.addFilter(movingAverageFilter);
		provider.addConsumer(nearestNeighbourFilter);
		provider.addConsumer(consumer);
		provider.start();
	}
}
