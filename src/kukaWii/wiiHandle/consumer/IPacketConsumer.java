package kukaWii.wiiHandle.consumer;

import java.util.concurrent.BlockingQueue;

import kukaWii.wiiHandle.packet.AbstractPacket;

/**
 * Basis-Interface f�r Consumer.
 * Diese m�ssen eine InputQueue registrieren k�nnen.
 * @author InternetMini
 *
 */
public interface IPacketConsumer {
	/**
	 * Registriert eine Queue bei dem Consumer.
	 * Diesem ist selbst �berlassen, was er damit tut
	 * @param queue
	 */
	public void registerQueue(BlockingQueue<AbstractPacket> inputQueue);
	
	
	/**
	 * Lässt einen Consumer starten.
	 */
	public void start();
	
	/**
	 * Lässt einen Consumer anhalten.
	 */
	public void stop();
	
	/**
	 * Überprüft die Antwortzeit.
	 */
	public void checkAntwortzeit(AbstractPacket packet);
}
