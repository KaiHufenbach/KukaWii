package kukaWii.main;

import kukaWii.wiiHandle.consumer.SimulationConsumer;
import kukaWii.wiiHandle.filter.MovingAverageFilter;
import kukaWii.wiiHandle.filter.NearestNeighbourFilter;
import kukaWii.wiiHandle.provider.PersistenceProvider;

public class MainWOWii {

	public static void main(String[] args) {
		new MainWOWii();
	}

	private MainWOWii() {
		System.setProperty("Simulation", "true");
		PersistenceProvider provider = new PersistenceProvider();
		// Hier k�nnen dem Provider mit addConsumer ebensolche hinzugef�gt
		// werden

		System.setProperty("InterruptCheck", "100");

		MovingAverageFilter movingAverageFilter = new MovingAverageFilter();
		NearestNeighbourFilter nearestNeighbourFilter = new NearestNeighbourFilter();
		// DummyConsumer consumer = new DummyConsumer();
		SimulationConsumer consumer = new SimulationConsumer();

		// provider.addFilter(movingAverageFilter);
		provider.addConsumer(nearestNeighbourFilter);
		provider.addConsumer(consumer);
		provider.start();
	}
}
