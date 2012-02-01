package kukaWii.wiiHandle.packet;

public class AccelerometerPacket extends AbstractPacket{
	
	private static final long serialVersionUID = 4644188684655184819L;
	
	private double x;
	private double y;
	private double z;
	
	public AccelerometerPacket(double x, double y, double z){
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	
	
	
	
	
}
