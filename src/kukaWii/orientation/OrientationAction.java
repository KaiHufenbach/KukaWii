package kukaWii.orientation;


public class OrientationAction {

	private long duration;
	private double yawSpeed;
	private double pitchSpeed;
	private double rollSpeed;
	
	//Maximale Drehgeschwindigkeit
	public static final double MAX_SPEED = 180;
	
	
	
	public OrientationAction(long duration, double yawSpeed, double pitchSpeed,
			double rollSpeed) {
		super();
		this.duration = duration;
		this.yawSpeed = yawSpeed;
		this.pitchSpeed = pitchSpeed;
		this.rollSpeed = rollSpeed;
	}
	
	public boolean getAbsolute(){
		return duration == 0;
	}

	public synchronized long getDuration() {
		return duration;
	}

	public synchronized double getYawSpeed() {
		return yawSpeed;
	}

	public synchronized double getPitchSpeed() {
		return pitchSpeed;
	}

	public synchronized double getRollSpeed() {
		return rollSpeed;
	}
	
	
	
	
}
