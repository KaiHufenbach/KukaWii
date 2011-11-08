package kukaWii.wiiHandle.Packet.Handle;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kukaWii.wiiHandle.Packet.Base.AbstractPacket;

/**
 * Abstrakte Klasse, um WiiPakete anzubieten.
 * Dazu muss die Methode providePacket genutzt werden.
 * @author Kai
 *
 */
public abstract class AbstractPacketProvider {

	private BlockingQueue<AbstractPacket> out = new ArrayBlockingQueue<AbstractPacket>(10000, true);
	private Lock providerLock = new ReentrantLock(true);
	private PacketConsumer consumer = null;
	
	/**
	 * Methode, um ein Packet in die OutputQueue zu schieben
	 * @param packet
	 */
	protected void providePacket(AbstractPacket packet){
		providerLock.lock();
		try{
			//Erm�glicht eine Zirkulation der Pakete, wenn kein Verbrauch stattfindet.
			if(!out.offer(packet)){
				out.poll();
				out.offer(packet);
			}
		}finally{
			providerLock.unlock();
		}
	}
	
	/**
	 * F�gt ans Ende der Kette einen Consumer an.
	 * In folgender Reihenfolge k�nnen hinzugef�gt werden:
	 * 	- n Filter
	 * 	- 1 Consumer
	 * @param consumer
	 */
	public void addConsumer(PacketConsumer consumer){
		if(this.consumer != null){
			((AbstractPacketProvider)this.consumer).addConsumer(consumer);
		}else{
			this.consumer = consumer;
			this.consumer.registerQueue(out);
			this.consumer.start();
		}
	}
	
}
