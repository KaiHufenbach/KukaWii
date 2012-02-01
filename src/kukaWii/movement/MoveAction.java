package kukaWii.movement;

/**
 * Stellt ein Bewegungspaket dar, in dem ein Ziel in Koordinaten, sowie eine Geschwindigkeit hinterlegt wird.
 * 
 * @author Kai
 *
 */
public class MoveAction {

	private double x;
	private double y;
	private double z;
	private double speed;

	/**
	 * Erzeugt eine neue Bewegungsanweisung. Diese wird durch einen Vektor
	 * dargestellt.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param speed
	 */
	public MoveAction(double x, double y, double z, double speed) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.speed = speed;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getZ() {
		return z;
	}

	public double getSpeed() {
		return speed;
	}
}
