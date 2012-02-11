package kukaWii.wiiHandle.service;

/**
 * Stellt die Kalibrierungsdaten für die Wii zentral zur Verfügung
 * @author Kai
 *
 */
public class CalibrationService {

	private static CalibrationService instance;
	
	
	//Accel Daten
	private double xAmp;
	private double xMin;
	private double xGravMin;
	
	private double yAmp;
	private double yMin;
	private double yGravMin;
	
	private double zAmp;
	private double zMin;
	private double zGravMin;
	
	//MotionPlus Daten
	private double rollMin;
	private double rollAmp;
	
	private double yawMin;
	private double yawAmp;
	
	private double pitchMin;
	private double pitchAmp;
	
	
	
	private CalibrationService(double xAmp, double xMin, double xGravMin,
			double yAmp, double yMin, double yGravMin, double zAmp,
			double zMin, double zGravMin, double rollMin, double rollAmp,
			double yawMin, double yawAmp, double pitchMin, double pitchAmp) {
		super();
		this.xAmp = xAmp;
		this.xMin = xMin;
		this.xGravMin = xGravMin;
		this.yAmp = yAmp;
		this.yMin = yMin;
		this.yGravMin = yGravMin;
		this.zAmp = zAmp;
		this.zMin = zMin;
		this.zGravMin = zGravMin;
		this.rollMin = rollMin;
		this.rollAmp = rollAmp;
		this.yawMin = yawMin;
		this.yawAmp = yawAmp;
		this.pitchMin = pitchMin;
		this.pitchAmp = pitchAmp;
	}

	public static synchronized void initializeCalibrationService(double xAmp, double xMin, double xGravMin, double yAmp, double yMin, double yGravMin, double zAmp, double zMin, double zGravMin, double rollAmp, double rollMin, double yawAmp, double yawMin, double pitchAmp, double pitchMin){
		instance = new CalibrationService(xAmp, xMin, xGravMin, yAmp, yMin, yGravMin, zAmp, zMin, zGravMin, rollMin, rollAmp, yawMin, yawAmp, pitchMin, pitchAmp);
	}
	
	private static synchronized CalibrationService getInstance(){
		if(instance == null){
			throw new IllegalStateException("Der CalibrationService muss zu anfangs initialisiert werden.");
		}
		return instance;
	}

	public static synchronized void setInstance(CalibrationService instance) {
		CalibrationService.instance = instance;
	}

	public synchronized void setxAmp(double xAmp) {
		this.xAmp = xAmp;
	}

	public synchronized void setxMin(double xMin) {
		this.xMin = xMin;
	}

	public synchronized void setxGravMin(double xGravMin) {
		this.xGravMin = xGravMin;
	}

	public synchronized void setyAmp(double yAmp) {
		this.yAmp = yAmp;
	}

	public synchronized void setyMin(double yMin) {
		this.yMin = yMin;
	}

	public synchronized void setyGravMin(double yGravMin) {
		this.yGravMin = yGravMin;
	}

	public synchronized void setzAmp(double zAmp) {
		this.zAmp = zAmp;
	}

	public synchronized void setzMin(double zMin) {
		this.zMin = zMin;
	}

	public synchronized void setzGravMin(double zGravMin) {
		this.zGravMin = zGravMin;
	}

	public synchronized void setRollMin(double rollMin) {
		this.rollMin = rollMin;
	}

	public synchronized void setRollAmp(double rollAmp) {
		this.rollAmp = rollAmp;
	}

	public synchronized void setYawMin(double yawMin) {
		this.yawMin = yawMin;
	}

	public synchronized void setYawAmp(double yawAmp) {
		this.yawAmp = yawAmp;
	}

	public synchronized void setPitchMin(double pitchMin) {
		this.pitchMin = pitchMin;
	}

	public synchronized void setPitchAmp(double pitchAmp) {
		this.pitchAmp = pitchAmp;
	}
	
	
	
}
