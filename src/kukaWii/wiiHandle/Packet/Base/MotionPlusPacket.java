package kukaWii.wiiHandle.Packet.Base;


public class MotionPlusPacket extends AbstractPacket{
	
	private static final long serialVersionUID = -7455998699795920269L;
	
	private double pitch;
	private double pitchDownSpeed;
	
	private double roll;
	private double rollLeftSpeed;
	
	private double yaw;
	private double yawLeftSpeed;
	
	public MotionPlusPacket(double pitch, double pitchDownSpeed, double roll, double rollLeftSpeed, double yaw, double yawLeftSpeed){
		super();
		this.pitch = pitch;
		this.pitchDownSpeed = pitchDownSpeed;
		this.roll = roll;
		this.rollLeftSpeed = rollLeftSpeed;
		this.yaw = yaw;
		this.yawLeftSpeed = yawLeftSpeed;
	}

	public double getPitch() {
		return pitch;
	}

	public double getPitchDownSpeed() {
		return pitchDownSpeed;
	}

	public double getRoll() {
		return roll;
	}

	public double getRollLeftSpeed() {
		return rollLeftSpeed;
	}

	public double getYaw() {
		return yaw;
	}

	public double getYawLeftSpeed() {
		return yawLeftSpeed;
	}
	
	
}
