package kukaWii.main;

import kukaWii.wiiHandle.DataCollector;

public abstract class AbstractMain {
	
public boolean simulation;

public static AbstractMain singleton;

public static DataCollector collector;

public boolean getSimulation() {
	return simulation;
}

public void setSimulation(boolean simulation) {
	this.simulation = simulation;
}

public DataCollector getCollector() {
	return collector;
}

public static AbstractMain getInstance() {
	// TODO Auto-generated method stub
	return singleton;
}

}
