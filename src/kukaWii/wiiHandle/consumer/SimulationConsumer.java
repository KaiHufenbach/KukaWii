package kukaWii.wiiHandle.consumer;

import kukaWii.movement.MoveAction;
import kukaWii.movement.MovementService;
import kukaWii.simulation.Simulator;
import kukaWii.wiiHandle.packet.AbstractPacket;
import kukaWii.wiiHandle.packet.AccelerometerPacket;

public class SimulationConsumer extends AbstractPacketConsumer {

	private MovementService movementService;
	private long predTimeStamp = 0;
	
	private double vx = 0;
	private double vy = 0;
	private double vz = 0;
	
	private double predAX = 0;
	private double predAY = 0;
	private double predAZ = 0;
	
	private static final int FACTOR = 100000;

	public SimulationConsumer() {
		super();
		movementService = MovementService.getService();
		new Simulator();
	}

	@Override
	protected void consume(AbstractPacket packet) {
		if (packet instanceof AccelerometerPacket) {
			AccelerometerPacket accelPacket = (AccelerometerPacket) packet;

			// Das erste Paket muss leider verworfen werden, sonst gehts nicht
			if (predTimeStamp == 0) {
				predTimeStamp = accelPacket.getTimestamp();
				//Vorherige beschleunigung nehmen
				predAX = accelPacket.getX();
				predAY = accelPacket.getY();
				predAZ = accelPacket.getZ();
			} else {
				long timeDifference = accelPacket.getTimestamp()
						- predTimeStamp;
				
				long timeSquared = timeDifference * timeDifference;

				double actualAX = (predAX + accelPacket.getX())/2;
				double actualAY = (predAY + accelPacket.getY())/2;
				double actualAZ = (predAZ + accelPacket.getZ())/2;
				
				predAX = accelPacket.getX();
				predAY = accelPacket.getY();
				predAZ = accelPacket.getZ();
				
				vx += timeDifference * actualAX;
				vy += timeDifference * actualAY;
				vz += timeDifference * actualAZ;
				
				
				double totalSpeed = Math.sqrt(Math.pow(
						(accelPacket.getX() * timeDifference), 2)
						+ Math.pow((accelPacket.getY() * timeDifference), 2)
						+ Math.pow((accelPacket.getZ() * timeDifference), 2))/FACTOR;

				movementService.addMoveAction(new MoveAction(accelPacket.getX()
						* timeSquared/FACTOR, accelPacket.getY() * timeSquared/FACTOR,
						accelPacket.getZ() * timeSquared/FACTOR, totalSpeed));

				predTimeStamp = accelPacket.getTimestamp();
			}

		}

	}

}
