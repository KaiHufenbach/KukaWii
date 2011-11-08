package kukaWii.wiiHandle.Packet.Handle;

import java.util.concurrent.BlockingQueue;

import kukaWii.wiiHandle.Packet.Base.AbstractPacket;

/**
 * Abstrakte Basisimplementation für einen PacketConsumer.
 * @author Kai
 *
 */
public abstract class AbstractPacketConsumer implements PacketConsumer{
	
	private BlockingQueue<AbstractPacket> input;
	private boolean run = true;
	
	
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
						consume(input.take());
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}
	
	public void stop(){
		this.run = false;
	}

	protected abstract void consume(AbstractPacket packet);
}
