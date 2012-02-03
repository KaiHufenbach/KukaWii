package kukaWii.wiiHandle.filter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import kukaWii.wiiHandle.packet.AbstractPacket;
import kukaWii.wiiHandle.packet.AccelerometerPacket;
import kukaWii.wiiHandle.packet.MotionPlusPacket;

/*
 * Dies ist eine Beispielimplementation eines Durchschnittswertefilters.
 * Diese Klasse schmeißt stark abweichende Werte, oder Störungen aus dem Wertestrom und puffert nach.
 */

public class MovingAverageFilter extends AbstractPacketFilter {

	private List<AbstractPacket> queue = new ArrayList<AbstractPacket>();

	@Override
	public AbstractPacket compute(AbstractPacket input) {

		double x = 0;
		double y = 0;
		double z = 0;
		boolean processed = false;
		AbstractPacket calcInput = null;
		
		//Füge ein Packet der Liste hinzu.
		if (input instanceof AccelerometerPacket) {
			x = ((AccelerometerPacket) input).getX();
			y = ((AccelerometerPacket) input).getY();
			z = ((AccelerometerPacket) input).getZ();
			logValues(x, y, z);
			queue.add(input);
		}

/*		if (input instanceof MotionPlusPacket) {
			double pitch = ((MotionPlusPacket) input).getPitch();
			double pitchdownspeed = ((MotionPlusPacket) input)
					.getPitchDownSpeed();
			double roll = ((MotionPlusPacket) input).getRoll();
			double rollleftspeed = ((MotionPlusPacket) input)
					.getRollLeftSpeed();
			double yaw = ((MotionPlusPacket) input).getYaw();
			double yawleftspeed = ((MotionPlusPacket) input).getYawLeftSpeed();
		} */

		//Wenn 5 Pakete gequeued wurden, dann fange die Berechnung an.
		//Testimplementation von einem Durchschnittswert.
		if (queue.lastIndexOf(input) == 4) {
			double[] calcX = new double[5];
			double[] calcY = new double[5];
			double[] calcZ = new double[5];
			for (int i = 0; i <= queue.lastIndexOf(input); i++) {
				AbstractPacket packet = queue.get(i);
				if (packet instanceof AccelerometerPacket){
				calcX[i] = ((AccelerometerPacket) packet).getX();
				calcY[i] = ((AccelerometerPacket) packet).getY();
				calcZ[i] = ((AccelerometerPacket) packet).getZ();	
				}
			}
			for (int i = 0; i <= calcX.length; i++){
				calcX[0] = calcX[0] + calcX[i];
				calcY[0] = calcY[0] + calcY[i];
				calcZ[0] = calcZ[0] + calcZ[i];
			}
			calcX[0] = calcX[0]/calcX.length;
			calcY[0] = calcY[0]/calcY.length;
			calcZ[0] = calcZ[0]/calcZ.length;
			
			((AccelerometerPacket)calcInput).setX(calcX[0]);
			((AccelerometerPacket)calcInput).setX(calcY[0]);
			((AccelerometerPacket)calcInput).setX(calcZ[0]);
			
			processed = true;
		}
		
		//Gebe das neu errechnete Paket zurück wenn processed = true ist, ansonsten null.
		if (processed == true) {
			return calcInput;
		} else
			return null;
	}

	private void logValues(double x, double y, double z) {
		System.out.println("X Beschleunigung: " + x);
		System.out.println("Y Beschleunigung: " + y);
		System.out.println("Z Beschleunigung: " + z);
	}

}
