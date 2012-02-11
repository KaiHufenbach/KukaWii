package kukaWii.wiiHandle.packet;


public class MotionPlusPacket extends AbstractPacket{
	
	private static final long serialVersionUID = -7455998699795920269L;
	
	private double pitchDownSpeed;
	
	private double rollLeftSpeed;
	
	private double yawLeftSpeed;
	
	public MotionPlusPacket(double pitchDownSpeed, double rollLeftSpeed, double yawLeftSpeed){
		super();
		this.pitchDownSpeed = pitchDownSpeed;
		this.rollLeftSpeed = rollLeftSpeed;
		this.yawLeftSpeed = yawLeftSpeed;
	}


	public double getPitchDownSpeed() {
		return pitchDownSpeed;
	}

	public double getRollLeftSpeed() {
		return rollLeftSpeed;
	}

	public double getYawLeftSpeed() {
		return yawLeftSpeed;
	}

	public void setPitchDownSpeed(double pitchDownSpeed) {
		this.pitchDownSpeed = pitchDownSpeed;
	}

	public void setRollLeftSpeed(double rollLeftSpeed) {
		this.rollLeftSpeed = rollLeftSpeed;
	}

	public void setYawLeftSpeed(double yawLeftSpeed) {
		this.yawLeftSpeed = yawLeftSpeed;
	}
	
	
}
