package kukaWii.wiiHandle;

import motej.Mote;
import motej.MoteFinder;
import motej.MoteFinderListener;
import motej.event.MoteDisconnectedEvent;
import motej.event.MoteDisconnectedListener;

/**
 * Verwaltet eine Verbindung zu einer WiiMote. Reißt die Verbindung ab, so wird automatisch <code>mote = null</code> gesetzt.
 * Dazu ist ggf. ein Listener auf der Mote im Programm anzumelden.
 * @author Kai Hufenbach
 *
 */
public class Connection {

	private static Mote mote = null;
	private final MoteFinder finder;
	private static final Object LOCK = new Object();
	
	public Connection(){
		finder = MoteFinder.getMoteFinder();
		
		MoteFinderListener moteFinderListener = new MoteFinderListener() {
			
			@Override
			public void moteFound(Mote m) {
				System.out.println("Remote found: " + m.getBluetoothAddress());
				mote = m;
				mote.setPlayerLeds(new boolean[]{true, false, false, false});
				mote.addMoteDisconnectedListener(new MoteDisconnectedListener<Mote>() {
					
					@Override
					public void moteDisconnected(MoteDisconnectedEvent<Mote> evt) {
						System.out.println("Remote disconnected!");
						mote.disconnect();
						mote = null;
					}
				});
				
				synchronized (LOCK){
					LOCK.notify();
				}
				
			}
		};
		
		finder.addMoteFinderListener(moteFinderListener);
	}
	
	private void discover(){
		if(mote == null){
			System.out.println("No remote connected - discovering for 30 seconds.");
			finder.startDiscovery();
			synchronized (LOCK) {
				try {
					LOCK.wait(30000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Stop discovery");
			finder.stopDiscovery();
		}
	}
	
	/**
	 * Gibt Wii-Remote Objekt zurück. Sucht, wenn nötig nach einer neuen Wii-Remote.
	 * @return Die Wii-Remote
	 */
	public Mote getRemote(){
		while(mote == null){
			discover();
		}
		return mote;
	}
	
	
}
