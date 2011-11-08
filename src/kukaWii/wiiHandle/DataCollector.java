package kukaWii.wiiHandle;

import kukaWii.wiiHandle.Packet.Base.AccelerometerPacket;
import kukaWii.wiiHandle.Packet.Base.MotionPlusPacket;
import kukaWii.wiiHandle.Packet.Handle.AbstractPacketProvider;
import motej.Extension;
import motej.Mote;
import motej.event.AccelerometerEvent;
import motej.event.AccelerometerListener;
import motej.event.ExtensionEvent;
import motej.event.ExtensionListener;
import motej.request.ReportModeRequest;
import motejx.extensions.motionplus.MotionPlus;
import motejx.extensions.motionplus.MotionPlusEvent;
import motejx.extensions.motionplus.MotionPlusListener;

public class DataCollector extends AbstractPacketProvider implements MotionPlusListener, AccelerometerListener<Mote>{
	
	private final Mote mote;
	private final DataCollector instance = this;
	
	/**
	 * Initialisiert einen neuen DataCollector für eine Wii-Mote.
	 * Nimmt Signale der MotionPlus und der Accelerometer entgegen 
	 * @param mote
	 */
	public DataCollector(Mote m){
		this.mote = m;
		
		mote.addExtensionListener(new ExtensionListener() {
			
			private MotionPlus motionPlus;
			
			@Override
			public void extensionDisconnected(ExtensionEvent evt) {
				
			}
			
			@Override
			public void extensionConnected(ExtensionEvent evt) {
				Extension ext = evt.getExtension();
				if(ext instanceof MotionPlus){
					System.out.println("MotionPlus found");
					motionPlus = (MotionPlus)ext;
					motionPlus.addMotionPlusEventListener(instance);
					System.out.println("Start Calibration");
					motionPlus.newCalibration();
					mote.setReportMode(ReportModeRequest.DATA_REPORT_0x35);
				}
				
			}
		});
		
		mote.addAccelerometerListener(this);
		mote.activateMotionPlus();
		System.out.println("DataCollector initialized");
		
	}

	@Override
	public void speedChanged(MotionPlusEvent evt) {
		providePacket(new MotionPlusPacket(evt.getPitch(), evt.getPitchDownSpeed(),evt.getRoll(), evt.getRollLeftSpeed(), evt.getYaw(), evt.getYawLeftSpeed()));
	}

	@Override
	public void accelerometerChanged(AccelerometerEvent<Mote> evt) {
		providePacket(new AccelerometerPacket(evt.getX(), evt.getY(), evt.getZ()));
	}

}
