package kukaWii.orientation;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Service zur Verwaltung der aktuellen Ausrichtung der Wii
 * Der Service deckt zwei Anforderungen ab:
 * 
 * - Verwaltung der Position der Wii
 * - Zur Verfügungstellung einer AusgabeQueue für Positionsänderungsbefehle
 *
 * @author InternetMini
 *
 */
public class OrientationService {

	private int bufferSize = 100;
	
	private double yaw = 0;
	private double pitch = 0;
	private double roll = 0;
	
	private static OrientationService instance;
	private BlockingQueue<OrientationAction> moveQueue;
	
	private OrientationService(){
		moveQueue = new ArrayBlockingQueue<OrientationAction>(bufferSize);
	}
	
	public static void initializeService(){
		instance = new OrientationService();
	}
	
	public static OrientationService getService(){
		if(instance==null){
			throw new IllegalStateException("Der OrientationService muss zu anfangs initialisiert werden.");
		}else{
			return instance;
		}
	}
	
	public synchronized void updateOrientation(OrientationAction action, boolean createChange){
		yaw = action.getYawSpeed() * action.getDuration();
		pitch = action.getPitchSpeed() * action.getDuration();
		roll = action.getRollSpeed() * action.getDuration();
		
		if(createChange){
			moveQueue.add(action);
		}
	}
	
	public synchronized void changeOrientation(double yaw, double pitch, double roll, boolean createChange){
		if(createChange){
			moveQueue.clear();
			moveQueue.add(new OrientationAction(0, yaw, pitch, roll));
		}
	}
	
	public synchronized OrientationAction getNextOrientation(){
		return moveQueue.poll();
	}

	public  double getYaw() {
		return yaw;
	}

	public  double getPitch() {
		return pitch;
	}


	public  double getRoll() {
		return roll;
	}

	
	
	
	
	
	
	
}
