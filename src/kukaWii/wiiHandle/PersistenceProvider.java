
package kukaWii.wiiHandle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import kukaWii.wiiHandle.Packet.Base.AbstractPacket;
import kukaWii.wiiHandle.Packet.Handle.AbstractPacketProvider;

public class PersistenceProvider extends AbstractPacketProvider{

	List<AbstractPacket> packets;
	
	public PersistenceProvider(){
		try {
			FileInputStream fis = new FileInputStream("packets1.pck");
			ObjectInputStream ois = new ObjectInputStream(fis);
			packets = (List<AbstractPacket>) ois.readObject();
			ois.close();
			System.out.println("File read - "+packets.size()+" packets loaded");
		} catch (FileNotFoundException e) {
			System.out.println("Die Datei packets1.pck wurde nicht gefunden. Mit einem Persistence Consumer kann diese jedoch erzeugt werden.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Muss aktiviert werden, damit der Provider anfängt.
	 */
	public void start(){
		
		new Thread(new Runnable() {
			long time = packets.get(0).getTimestamp();
			@Override
			public void run() {
				for(AbstractPacket packet : packets){
					try {
						Thread.sleep(packet.getTimestamp()-time);
						providePacket(packet);
						time = packet.getTimestamp();
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}
}
