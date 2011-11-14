package kukaWii.wiiHandle.Packet.Handle;

import java.util.concurrent.BlockingQueue;

import kukaWii.wiiHandle.Packet.Base.AbstractPacket;
import kukaWii.wiiHandle.Security.InterruptException;

/**
 * Abstrakte Basisimplementation für einen PacketConsumer.
 * @author Kai
 *
 */
public abstract class AbstractPacketConsumer implements PacketConsumer{
	
	private BlockingQueue<AbstractPacket> input;
	private boolean run = true;
	
	private int differenceCheck;
	
	/**
	 * Muss von den Consumern aufgerufen werden
	 */
	public AbstractPacketConsumer(){
		differenceCheck = Integer.parseInt(System.getProperty("InterruptCheck"));
	}
	
	@Override
	public void registerQueue(BlockingQueue<AbstractPacket> inputQueue) {
		this.input = inputQueue;
	}

	@Override
	public void start() {
		this.run = true;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(run){
					try {
						checkAntwortzeit(input.peek());
						consume(input.take());
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}
	
	@Override
	public void stop(){
		this.run = false;
	}

	protected abstract void consume(AbstractPacket packet);

	@Override
	public void checkAntwortzeit(AbstractPacket packet) {
		if(packet != null){
			long difference = System.currentTimeMillis()-packet.getTimestamp();
			if(difference>differenceCheck){
				throw new InterruptException("Zu lange Antwortzeit: "+difference+"ms");
			}
		}
		
		
	}
	
	
}
