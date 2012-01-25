package kukaWii.wiiHandle.Consumer;

import java.util.concurrent.BlockingQueue;

import kukaWii.wiiHandle.Packet.AbstractPacket;
import kukaWii.wiiHandle.Security.SecurityService;

/**
 * Abstrakte Basisimplementation fŸr einen PacketConsumer.
 * 
 * @author Kai
 * 
 */
public abstract class AbstractPacketConsumer implements IPacketConsumer {

	private BlockingQueue<AbstractPacket> input;
	private boolean run = true;

	private int differenceCheck;

	/**
	 * Muss von den Consumern aufgerufen werden
	 */
	public AbstractPacketConsumer() {
		try {
			differenceCheck = Integer.parseInt(System
					.getProperty("InterruptCheck"));
		} catch (Exception e) {
			// TODO: handle exception
			differenceCheck = 100;
		}
	}

	protected abstract void consume(AbstractPacket packet);

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
						AbstractPacket packet = input.take();
						checkAntwortzeit(packet);
						consume(packet);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}

	@Override
	public void stop() {
		this.run = false;
	}

	@Override
	public void checkAntwortzeit(AbstractPacket packet) {
		if (packet != null) {
			long difference = System.currentTimeMillis()
					- packet.getTimestamp();
			if (System.getProperty("Simulation").equals("false")) {
				if (difference > differenceCheck) {
					SecurityService.panicInterrupt("Zu lange Antwortzeit: "
							+ difference + "ms");
				}
			}
		}

	}

}
