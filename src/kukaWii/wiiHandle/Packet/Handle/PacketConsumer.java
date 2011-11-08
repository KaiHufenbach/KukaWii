package kukaWii.wiiHandle.Packet.Handle;

import java.util.concurrent.BlockingQueue;

import kukaWii.wiiHandle.Packet.Base.AbstractPacket;

/**
 * Basis-Interface für Consumer.
 * Diese müssen eine InputQueue registrieren können.
 * @author InternetMini
 *
 */
public interface PacketConsumer {
	/**
	 * Registriert eine Queue bei dem Consumer.
	 * Diesem ist selbst überlassen, was er damit tut
	 * @param queue
	 */
	public void registerQueue(BlockingQueue<AbstractPacket> inputQueue);
	
	
	/**
	 * Lässt einen Consumer starten.
	 */
	public void start();
}
