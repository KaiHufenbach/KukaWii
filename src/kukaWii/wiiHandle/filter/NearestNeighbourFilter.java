package kukaWii.wiiHandle.filter;

import kukaWii.wiiHandle.packet.AbstractPacket;
import kukaWii.wiiHandle.packet.AccelerometerPacket;
import kukaWii.wiiHandle.packet.MotionPlusPacket;

public class NearestNeighbourFilter extends AbstractPacketFilter {

	private AbstractPacket[] queue = new AbstractPacket[5];
	int i = 0;
	
	@Override
	public AbstractPacket compute(AbstractPacket input) {

		double x = 0;
		double y = 0;
		double z = 0;
		boolean processed = false;

		if (input instanceof AccelerometerPacket) {
			x = ((AccelerometerPacket) input).getX();
			y = ((AccelerometerPacket) input).getY();
			z = ((AccelerometerPacket) input).getZ();
			logValues(x, y, z);
			queue[i] = input;
			i++;
		}

		if (input instanceof MotionPlusPacket) {
			double pitch = ((MotionPlusPacket) input).getPitch();
			double pitchdownspeed = ((MotionPlusPacket) input)
					.getPitchDownSpeed();
			double roll = ((MotionPlusPacket) input).getRoll();
			double rollleftspeed = ((MotionPlusPacket) input)
					.getRollLeftSpeed();
			double yaw = ((MotionPlusPacket) input).getYaw();
			double yawleftspeed = ((MotionPlusPacket) input).getYawLeftSpeed();
		}

		//Wenn 5 Pakete gequeued wurden, dann fange die Berechnung an.
		if (i == 4) {
			for (int j = 0; j <= i; j++) {

			}
			processed = true;
			i = 0;
		}
		
		//Gebe das neu errechnete Paket zurück wenn processed = true ist, ansonsten null.
		if (processed == true) {
			return input;
		} else
			return null;
	}

	private void logValues(double x, double y, double z) {
		System.out.println("X Beschleunigung: " + x);
		System.out.println("Y Beschleunigung: " + y);
		System.out.println("Z Beschleunigung: " + z);
	}

}
