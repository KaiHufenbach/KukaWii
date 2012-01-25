package kukaWii.wiiHandle.Packet;

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
	
	
	
	
}
