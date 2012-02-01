package kukaWii.wiiHandle.consumer;

import kukaWii.movement.MoveAction;
import kukaWii.movement.MovementService;
import kukaWii.simulation.Simulator;
import kukaWii.wiiHandle.packet.AbstractPacket;
import kukaWii.wiiHandle.packet.AccelerometerPacket;

public class SimulationConsumer extends AbstractPacketConsumer {

	private MovementService movementService;
	private long predTimeStamp = 0;
	
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
			} else {
				long timeDifference = accelPacket.getTimestamp()
						- predTimeStamp;

				long timeSquared = timeDifference * timeDifference;

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
