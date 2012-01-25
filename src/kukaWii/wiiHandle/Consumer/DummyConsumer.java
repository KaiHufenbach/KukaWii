package kukaWii.wiiHandle.Consumer;

import java.util.ArrayList;
import java.util.List;

import kukaWii.wiiHandle.Packet.AbstractPacket;
import kukaWii.wiiHandle.Packet.AccelerometerPacket;
import kukaWii.wiiHandle.Packet.MotionPlusPacket;

public class DummyConsumer extends AbstractPacketConsumer {
	
	private int accelPackets;
	private int mopluPackets;
	
	private long mopluTimestamp = System.currentTimeMillis();
	private long accelTimestamp = System.currentTimeMillis();
	
	private int sysoutRate = 100;
	

	@Override
	protected void consume(AbstractPacket packet) {
		if (packet instanceof MotionPlusPacket) {
			mopluPackets++;
			if (mopluPackets % sysoutRate == 0) {
				System.out
						.println("MotionPlus Speed: "
								+ (sysoutRate * 1000 / (packet.getTimestamp() - mopluTimestamp))
								+ " Packets/sec");
				mopluTimestamp = packet.getTimestamp();
				mopluPackets = 0;
			}
		} else if (packet instanceof AccelerometerPacket) {
			accelPackets++;
			if (accelPackets % sysoutRate == 0) {
				System.out
						.println("AccelPacket Speed: "
								+ (sysoutRate * 1000 / (packet.getTimestamp() - accelTimestamp))
								+ " Packets/sec");
				accelTimestamp = packet.getTimestamp();
				accelPackets = 0;
			}
		}
	}

}
