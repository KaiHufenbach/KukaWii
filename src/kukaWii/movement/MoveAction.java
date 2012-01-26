package kukaWii.movement;

public class MoveAction {
	
	private float x;
	private float y;
	private float z;
	private float speed;
	
	/**
	 * Erzeugt eine neue Bewegungsanweisung. Diese wird durch einen Vektor dargestellt.
	 * @param x
	 * @param y
	 * @param z
	 * @param speed
	 */
	public MoveAction(float x, float y, float z, float speed){
		this.x = x;
		this.y = y;
		this.z = z;
		this.speed = speed;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	
	
	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getZ() {
		return z;
	}
	
	public float getSpeed() {
		return speed;
	}
}
