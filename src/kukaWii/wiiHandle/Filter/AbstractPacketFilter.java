package kukaWii.wiiHandle.Filter;

import java.util.concurrent.BlockingQueue;

import kukaWii.wiiHandle.Consumer.IPacketConsumer;
import kukaWii.wiiHandle.Packet.AbstractPacket;
import kukaWii.wiiHandle.Provider.AbstractPacketProvider;

public abstract class AbstractPacketFilter extends AbstractPacketProvider implements IPacketConsumer{
	
	private BlockingQueue<AbstractPacket> input;
	private boolean run = true;
	
	@Override
	public void registerQueue(BlockingQueue<AbstractPacket> inputQueue) {
		input = inputQueue;
	}
	
	/**
	 * Diese Methode wird vom Filter aufgerufen, um ein Paket zu verändern.
	 * @param input
	 * @return AbstractPacket, wenn null, dann wird dieses nicht in die outputQueueAufgenommen
	 */
	public abstract AbstractPacket compute(AbstractPacket input);

	@Override
	public void start() {
		this.run = true;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(run){
					try {
						AbstractPacket res = compute(input.take());
						if(res != null){
							providePacket(res);
						}
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

	@Override
	public void checkAntwortzeit(AbstractPacket packet) {
	}
	
	
	
}
