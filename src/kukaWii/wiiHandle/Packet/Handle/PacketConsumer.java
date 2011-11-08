package kukaWii.wiiHandle.Packet.Handle;

import java.util.concurrent.BlockingQueue;

import kukaWii.wiiHandle.Packet.Base.AbstractPacket;

/**
 * Basis-Interface f�r Consumer.
 * Diese m�ssen eine InputQueue registrieren k�nnen.
 * @author InternetMini
 *
 */
public interface PacketConsumer {
	/**
	 * Registriert eine Queue bei dem Consumer.
	 * Diesem ist selbst �berlassen, was er damit tut
	 * @param queue
	 */
	public void registerQueue(BlockingQueue<AbstractPacket> inputQueue);
	
	
	/**
	 * L�sst einen Consumer starten.
	 */
	public void start();
}
