package motej.demos.motionplus;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import motej.Extension;
import motej.Mote;
import motej.MoteFinder;
import motej.MoteFinderListener;
import motej.event.ExtensionEvent;
import motej.event.ExtensionListener;
import motej.request.ReportModeRequest;
import motejx.extensions.motionplus.MotionPlus;
import motejx.extensions.motionplus.MotionPlusEvent;
import motejx.extensions.motionplus.MotionPlusListener;


@SuppressWarnings("serial")
public class MotionPlusDemo  implements MoteFinderListener, ExtensionListener, MotionPlusListener {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		
			public void run() {
				new MotionPlusDemo();
			}
		
		});
	}
	
//	private MyAngleAdapter angleA = new MyAngleAdapter(this);

	private JFrame frame;
	private JTextField yawSpeed;
	private JTextField rollSpeed;
	private JTextField pitchSpeed;
	private JTextField yawAngle;
	private JTextField rollAngle;
	private JTextField pitchAngle;
	private Mote mote;
	private MoteFinder finder;
	GraphPanel graphPanel;
	private MotionPlus motionplus;
	
	private double yawVal;
	private double rollVal;
	private double pitchVal;
	
	private MotionPlusEvent lastMotionPlusEvent = null;
	
	private Action startDiscoverAction = new AbstractAction() {
	
		public void actionPerformed(ActionEvent e) {
			finder.startDiscovery();
		}
	
	};

	private Action cancelDiscoverAction = new AbstractAction() {
		
		public void actionPerformed(ActionEvent e) {
			finder.stopDiscovery();
		}
	
	};
	
	public MotionPlusDemo() {
		finder = MoteFinder.getMoteFinder();
		finder.addMoteFinderListener(this);
		finder.startDiscovery();
		initGui();
	}
	
	

	public void extensionConnected(ExtensionEvent evt) {
		final Extension ext = evt.getExtension();
		
		if (ext instanceof MotionPlus) {
			motionplus = (MotionPlus) ext;
			motionplus.addMotionPlusEventListener(this);
			motionplus.newCalibration();
//			motionplus.addMotionPlusEventListener(angleA);
			mote.setReportMode(ReportModeRequest.DATA_REPORT_0x32);
//			motionplus.addMotionPlusEventListener( this );
//			nunchuk.addAccelerometerListener(this);
//			nunchuk.addAnalogStickListener(this);
//			nunchuk.addNunchukButtonListener(this);
//			
//			Thread t = new Thread(new Runnable() {
//			
//				public void run() {
//					while (nunchuk.getCalibrationData() == null) {
//						try {
//							Thread.sleep(1l);
//						} catch (InterruptedException ex) {
//							ex.printStackTrace();
//						}
//					}
//					analogDisplay.setNunchukCalibrationData(nunchuk.getCalibrationData());
//					
//					SwingUtilities.invokeLater(new Runnable() {
//					
//						public void run() {
//							if (nunchuk != null && nunchuk.getCalibrationData() != null) {
//								Point min = nunchuk.getCalibrationData().getMinimumAnalogPoint();
//								Point max = nunchuk.getCalibrationData().getMaximumAnalogPoint();
//								Point center = nunchuk.getCalibrationData().getCenterAnalogPoint();
//								minPoint.setText("cal (min) - x: " + min.x + " / y: " + min.y);
//								maxPoint.setText("cal (max) - x: " + max.x + " / y: " + max.y);
//								centerPoint.setText("cal (center) - x: " + center.x + " / y: " + center.y);
//
//								int zero = nunchuk.getCalibrationData().getZeroForceX();
//								int earth = nunchuk.getCalibrationData().getGravityForceX();
//								accelerometerPanel.setCalibrationDataX(zero, earth);
//								
//								zero = nunchuk.getCalibrationData().getZeroForceY();
//								earth = nunchuk.getCalibrationData().getGravityForceY();
//								accelerometerPanel.setCalibrationDataY(zero, earth);
//								
//								zero = nunchuk.getCalibrationData().getZeroForceZ();
//								earth = nunchuk.getCalibrationData().getGravityForceZ();
//								accelerometerPanel.setCalibrationDataZ(zero, earth);
//							}
//						}
//					
//					});
//				}
//			
//			});
//			t.start();
//			
//			
//			mote.setReportMode(ReportModeRequest.DATA_REPORT_0x32);
		}
		
//		SwingUtilities.invokeLater(new Runnable() {
//			
//			public void run() {
//				extensionLabel.setText(ext.toString());
//			}
//		
//		});
	}

	public void extensionDisconnected(ExtensionEvent evt) {
//		SwingUtilities.invokeLater(new Runnable() {
//		
//			public void run() {
//				extensionLabel.setText("none");
//				minPoint.setText("");
//				maxPoint.setText("");
//				centerPoint.setText("");
//			}
//		
//		});
	}
	
	protected void initGui() {
		frame = new JFrame("MotionPlusDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		
			@Override
			public void windowClosing(WindowEvent e) {
				if (mote != null) {
					mote.setReportMode(ReportModeRequest.DATA_REPORT_0x30);
					mote.disconnect();
				}
			}
		
		});
		
		yawSpeed = new JTextField();
		rollSpeed = new JTextField();
		pitchSpeed = new JTextField();
		yawAngle = new JTextField();
		rollAngle = new JTextField();
		pitchAngle = new JTextField();
		Container pane = frame.getContentPane();
		Container pane1 = new Container();
		pane1.setLayout(new GridLayout(3,4) );
		pane1.add(new JLabel("Yaw Left Speed:"));
		pane1.add(yawSpeed);
		pane1.add(new JLabel("Yaw Angle:"));
		pane1.add(yawAngle);
		pane1.add(new JLabel("Roll Left Speed:"));
		pane1.add(rollSpeed);
		pane1.add(new JLabel("Roll Angle:"));
		pane1.add(rollAngle);
		pane1.add(new JLabel("Pitch Down Speed:"));
		pane1.add(pitchSpeed);
		pane1.add(new JLabel("Pitch Angle:"));
		pane1.add(pitchAngle);
		Container pane2 = new Container();
		pane2.setLayout( new FlowLayout() );
		graphPanel = new GraphPanel();
		pane2.add(graphPanel);
//		pane2.setSize(graphPanel.getDimension());
		
		pane.setLayout( new GridLayout(2,1));
		pane.add(pane1);
		pane.add(pane2);
//		frame.setSize(250, 100);
		frame.pack();
		
		frame.setVisible(true);
	}

	public void moteFound(final Mote mote) {
		this.mote = mote;
		finder.stopDiscovery();
		mote.setPlayerLeds(new boolean[] {true, false, false, false} );
//		SwingUtilities.invokeLater(new Runnable() {
//		
//			public void run() {
//				moteLabel.setText(mote.toString());
//			}
//		
//		});
		mote.addExtensionListener(this);
		mote.activateMotionPlus();
	}


	public void speedChanged(MotionPlusEvent evt) {
		
		yawSpeed.setText( new Double( evt.getYawLeftSpeed() ).toString() );
		rollSpeed.setText( new Double( evt.getRollLeftSpeed() ).toString() );
		pitchSpeed.setText( new Double( evt.getPitchDownSpeed() ).toString() );
	
		//graphPanel.addValue(evt.getYawLeftSpeed(), evt.getRollLeftSpeed(), evt.getPitchDownSpeed());
		
		if (lastMotionPlusEvent == null)
		{
			lastMotionPlusEvent = evt;;
			return;
		}
		
		long duration_msec = evt.getEventTime() - lastMotionPlusEvent.getEventTime();
		double duration_sec = (double) duration_msec / 1000;
	//	System.out.println(duration_msec + " - " + duration_sec);
		
		this.yawVal+= evt.getYawLeftSpeed() * duration_sec ;
		this.rollVal+= evt.getRollLeftSpeed() * duration_sec;
		this.pitchVal+= evt.getPitchDownSpeed() * duration_sec;
		
		yawAngle.setText( new Integer( (int) yawVal ).toString() );
		rollAngle.setText( new Integer( (int) rollVal ).toString() );
		pitchAngle.setText( new Integer( (int) pitchVal ).toString() );
		
	//	System.out.println(evt.getYawLeftSpeed() + " - " + evt.getRollLeftSpeed() + " - " + evt.getPitchDownSpeed());
	
		this.lastMotionPlusEvent = evt;
	}
}
