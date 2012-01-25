package kukaWii.wiiHandle.Security;

import kukaWii.wiiHandle.Provider.DataCollector;

public class SecurityService {
	
	private DataCollector datacollector;
	private static SecurityService instance;
	
	private SecurityService(DataCollector datacollector) {
		this.datacollector = datacollector;
	}
	
	public static void createSecurityService(DataCollector dc){
		instance = new SecurityService(dc);
	}
	
	public DataCollector getDataCollector() {
		return datacollector;
	}
	
	public static SecurityService getInstance(){
		if(instance == null){
			throw new IllegalStateException("Der SecurityService muss zu anfangs initialisiert werden.");
		}
		return instance;
	}
	
	public static void panicInterrupt(String reason){
		if (System.getProperty("Simulation").equals("false")){
			SecurityService.getInstance().getDataCollector().panicInterrupt();
		}
		throw new InterruptException(reason);
	}
	
	public static void resetPackages(){
		//TODO muss noch implementiert werden
	}

}
