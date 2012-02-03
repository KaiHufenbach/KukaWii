package kukaWii.wiiHandle.filter;

import java.util.ArrayList;
import java.util.List;

import kukaWii.wiiHandle.packet.AbstractPacket;
import kukaWii.wiiHandle.packet.AccelerometerPacket;
import kukaWii.wiiHandle.packet.MotionPlusPacket;

public class InterferenceFilter extends AbstractPacketFilter {

	double xbefore = 0;
	double ybefore = 0;
	double zbefore = 0;

	//Dieser Filter behandelt einfachste Störungen und zu schnelle Bewegungen
	@Override
	public AbstractPacket compute(AbstractPacket input) {

		List<AbstractPacket> queue = new ArrayList<AbstractPacket>();
		input = maxG(input);
		input = interfere(input);

		return input;
	}

	private boolean compare(double x, double y, double z) {
		if (x != 0 && y != 0 && z != 0) {
			if (x > xbefore * 2 || y > ybefore * 2 || z > zbefore * 2) {
				return true;
			}
			xbefore = x;
			ybefore = y;
			zbefore = z;
		}
		return false;
	}

	//Ab einem Wert von 4G wird die beschleunigung als zu schnell angesehen und verwirft das Packet
	private AbstractPacket maxG(AbstractPacket packet) {
		if (packet instanceof AccelerometerPacket) {
			double x, y, z;
			x = ((AccelerometerPacket) packet).getX();
			y = ((AccelerometerPacket) packet).getY();
			z = ((AccelerometerPacket) packet).getZ();
			if (x >= 4 || y >= 4 || z >= 4) {
				return null;
			}
		}
		return packet;
	}

	//Wenn ein Packet unmöglich zu den anderen Packets passt, dann wird dieses verworfen.
	//TODO empirische Untersuchung, was es für Interferenzen geben könnte.
	private AbstractPacket interfere(AbstractPacket packet) {

		double x = 0;
		double y = 0;
		double z = 0;

		if (packet instanceof AccelerometerPacket) {
			x = ((AccelerometerPacket) packet).getX();
			y = ((AccelerometerPacket) packet).getY();
			z = ((AccelerometerPacket) packet).getZ();
			boolean toogreat = compare(x, y, z);
			if (toogreat == true) {
				return null;
			}
		}

		if (packet instanceof MotionPlusPacket) {
			double roll;
			double pitch;
			double yaw;
		}

		return packet;
	}

}
