package kukaWii.wiiHandle.packet;

import javax.vecmath.Vector3d;

public class AccelerometerPacket extends AbstractPacket{
	
	private static final long serialVersionUID = 4644188684655184819L;
	
	private Vector3d vector;
	
	public AccelerometerPacket(double x, double y, double z){
		super();
		vector = new Vector3d(x, y, z);
	}

	public double getX() {
		return vector.getX();
	}

	public double getY() {
		return vector.getY();
	}

	public double getZ() {
		return vector.getZ();
	}

	public void setX(double x) {
		vector.setX(x);
	}

	public void setY(double y) {
		vector.setY(y);
	}

	public void setZ(double z) {
		vector.setZ(z);
	}
	
	public double getLength(){
		return vector.length();
	}
	
	
	
	
}
