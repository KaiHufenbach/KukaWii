/*
 * Copyright 2007-2008 Volker Fritzsch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package motej;

import java.io.IOException;

import javax.bluetooth.L2CAPConnection;
import javax.microedition.io.Connector;

import motej.request.ReportModeRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
class IncomingThread extends Thread {
	
	private static final long THREAD_SLEEP = 1l;

	private Logger log = LoggerFactory.getLogger(IncomingThread.class);

	private Mote source;
	
	private Extension extension;

	private volatile boolean active;

	private L2CAPConnection incoming;

	private IrPoint[] interleavedIrCameraData;

	private int[] interleavedAccelerometerData;
	
	private AccelerationConstants localAccelerationConstants;
	
	double xAcceleration;
	double yAcceleration;
	double zAcceleration;
	
	double pitch;
	double roll;
	boolean isStill = false;
	CalibrationDataReport calibration;

	protected IncomingThread(Mote source, String btaddress)
			throws IOException, InterruptedException {
		super("in:" + btaddress);
		this.source = source;
		
		String l2cap = "btl2cap://" + btaddress
		+ ":13;authenticate=false;encrypt=false;master=false";
		
		if (log.isDebugEnabled()) {
			log.debug("Opening incoming connection: " + l2cap);
		}
		
		incoming = (L2CAPConnection) Connector.open(l2cap, Connector.READ, true);

		if (log.isDebugEnabled()) {
			log.debug("Incoming connection is " + incoming.toString());
		}
		
		Thread.sleep(THREAD_SLEEP);
		active = true;
		
		
	}

	public void disconnect() {
		active = false;
	}

	protected void parseAccelerometerData(byte[] bytes) {
	
		int x = bytes[4] & 0xff;
		int y = bytes[5] & 0xff;
		int z = bytes[6] & 0xff;
		
		int i = ((bytes[4] & 0xFF) << 2) + ((bytes[2] & 0x60) >> 5);
	    int j = ((bytes[5] & 0xFF) << 2) + ((bytes[3] & 0x60) >> 5);
	    int k = ((bytes[6] & 0xFF) << 2) + ((bytes[3] & 0x80) >> 6);
	    
	    
	    if(localAccelerationConstants==null)
	    {
	    	this.calibration = source.getCalibrationDataReport();
	    	this.localAccelerationConstants = new AccelerationConstants(0,0,0,0,0,0);
//		    //xOne:99.0 xZero505.0yOne:102.0 yZero502.0zOne:107.0 zZero500.0
//		    localAccelerationConstants.xOne=99.0;
//		    localAccelerationConstants.xZero=505.0;
//		    localAccelerationConstants.yOne=102.0;
//		    localAccelerationConstants.yZero=502.0;
//		    localAccelerationConstants.zOne=107.0;
//		    localAccelerationConstants.zZero=500.0;
//	    	return;
	    	
	    	
	   // 	byte[] arrayOfByte = readData(new byte[] { 0, 0, 0, 22 }, 8);
	   
	   //     this.accelConstants = new AccelerationConstants(((arrayOfByte[0] & 0xFF) << 2) + (arrayOfByte[3] & 0x3), ((arrayOfByte[1] & 0xFF) << 2) + ((arrayOfByte[3] & 0xC) >> 2), ((arrayOfByte[2] & 0xFF) << 2) + ((arrayOfByte[3] & 0x30) >> 4), ((arrayOfByte[4] & 0xFF) << 2) + (arrayOfByte[7] & 0x3), ((arrayOfByte[5] & 0xFF) << 2) + ((arrayOfByte[7] & 0xC) >> 2), ((arrayOfByte[6] & 0xFF) << 2) + ((arrayOfByte[7] & 0x30) >> 4));
	    

	    }
	    
	    localAccelerationConstants.xOne	=calibration.getZeroX()-calibration.getGravityX();
	    localAccelerationConstants.xZero=calibration.getGravityX();
	    localAccelerationConstants.yOne	=calibration.getZeroY()-calibration.getGravityY();
	    localAccelerationConstants.yZero=calibration.getGravityY();
	    localAccelerationConstants.zOne	=calibration.getZeroZ()-calibration.getGravityZ();
	    localAccelerationConstants.zZero=calibration.getGravityZ();
 
//	    
//	    
//		AccelerationConstants d = localAccelerationConstants;
//		System.out.println(	"xOne:"+d.xOne()+" xZero"+d.xZero()+
//							" yOne:"+d.yOne()+" yZero"+d.yZero()+
//							" zOne:"+d.zOne()+" zZero"+d.zZero());


	    this.xAcceleration = ((i - localAccelerationConstants.xZero()) / localAccelerationConstants.xOne());
	    this.yAcceleration = ((j - localAccelerationConstants.yZero()) / localAccelerationConstants.yOne());
	    this.zAcceleration = ((k - localAccelerationConstants.zZero()) / localAccelerationConstants.zOne());

	    double acceleration = Math.sqrt(xAcceleration*xAcceleration+yAcceleration*yAcceleration+zAcceleration*zAcceleration)-1;
	    
	    initImplied();
	    
//	    System.out.println(acceleration+" X:" +xAcceleration+" Y:"+yAcceleration+" Z:"+zAcceleration+" Roll:"+this.roll+" Pitch:"+this.pitch);
	    
	    
		
		source.fireAccelerometerEvent(xAcceleration, yAcceleration, zAcceleration);
	}

	protected void parseBasicIrCameraData(byte[] bytes, int offset) {
		int x0 = bytes[offset] & 0xff ^ (bytes[offset + 2] & 0x30) << 4;
		int y0 = bytes[offset + 1] & 0xff ^ (bytes[offset + 2] & 0xc0) << 2;
		IrPoint p0 = new IrPoint(x0, y0);

		int x1 = bytes[offset + 3] & 0xff ^ (bytes[offset + 2] & 0x03) << 8;
		int y1 = bytes[offset + 4] & 0xff ^ (bytes[offset + 2] & 0x0c) << 6;
		IrPoint p1 = new IrPoint(x1, y1);

		int x2 = bytes[offset + 5] & 0xff ^ (bytes[offset + 7] & 0x30) << 4;
		int y2 = bytes[offset + 6] & 0xff ^ (bytes[offset + 7] & 0xc0) << 2;
		IrPoint p2 = new IrPoint(x2, y2);

		int x3 = bytes[offset + 8] & 0xff ^ (bytes[offset + 7] & 0x03) << 8;
		int y3 = bytes[offset + 9] & 0xff ^ (bytes[offset + 7] & 0x0c) << 6;
		IrPoint p3 = new IrPoint(x3, y3);
		
		source.fireIrCameraEvent(IrCameraMode.BASIC, p0, p1, p2, p3);
	}

	protected void parseCoreButtonData(byte[] bytes) {
		int modifiers = bytes[2] & 0xff ^ (bytes[3] & 0xff) << 8;
		source.fireCoreButtonEvent(modifiers);
	}

	protected void parseExtendedIrCameraData(byte[] bytes, int offset) {
		int x0 = bytes[7] & 0xff ^ (bytes[9] & 0x30) << 4;
		int y0 = bytes[8] & 0xff ^ (bytes[9] & 0xc0) << 2;
		int size0 = bytes[9] & 0x0f;
		IrPoint p0 = new IrPoint(x0, y0, size0);

		int x1 = bytes[10] & 0xff ^ (bytes[12] & 0x30) << 4;
		int y1 = bytes[11] & 0xff ^ (bytes[12] & 0xc0) << 2;
		int size1 = bytes[12] & 0x0f;
		IrPoint p1 = new IrPoint(x1, y1, size1);

		int x2 = bytes[13] & 0xff ^ (bytes[15] & 0x30) << 4;
		int y2 = bytes[14] & 0xff ^ (bytes[15] & 0xc0) << 2;
		int size2 = bytes[15] & 0x0f;
		IrPoint p2 = new IrPoint(x2, y2, size2);

		int x3 = bytes[16] & 0xff ^ (bytes[18] & 0x30) << 4;
		int y3 = bytes[17] & 0xff ^ (bytes[18] & 0xc0) << 2;
		int size3 = bytes[18] & 0x0f;
		IrPoint p3 = new IrPoint(x3, y3, size3);

		source.fireIrCameraEvent(IrCameraMode.EXTENDED, p0, p1, p2, p3);
	}
	
	protected void parseExtensionData(byte[] bytes, int offset, int length) {
		if (extension == null) {
			return;
		}
		byte[] extensionData = new byte[length];
		System.arraycopy(bytes, offset, extensionData, 0, length);
		extension.parseExtensionData(extensionData);
	}
	
	protected void parseFullIrCameraData(byte[] bytes, int reportMode) {
		if (interleavedIrCameraData == null) {
			interleavedIrCameraData = new IrPoint[4];
		}
		if (reportMode == ReportModeRequest.DATA_REPORT_0x3e) {
			int x0 = (bytes[5] & 0xff) ^ ((bytes[7] & 0x30) << 4);
			int y0 = (bytes[6] & 0xff) ^ ((bytes[7] & 0xc0) << 2);
			int size0 = bytes[7] & 0x0f;
			int xmin0 = bytes[8] & 0x7f;
			int ymin0 = bytes[9] & 0x7f;
			int xmax0 = bytes[10] & 0x7f;
			int ymax0 = bytes[11] & 0x7f;
			int intensity0 = bytes[13] & 0xff;
			interleavedIrCameraData[0] = new IrPoint(x0, y0, size0, xmin0, ymin0, xmax0, ymax0, intensity0);
		
			int x1 = (bytes[14] & 0xff) ^ ((bytes[16] & 0x30) << 4);
			int y1 = (bytes[15] & 0xff) ^ ((bytes[16] & 0xc0) << 2);
			int size1 = bytes[16] & 0x0f;
			int xmin1 = bytes[17] & 0x7f;
			int ymin1 = bytes[18] & 0x7f;
			int xmax1 = bytes[19] & 0x7f;
			int ymax1 = bytes[20] & 0x7f;
			int intensity1 = bytes[22] & 0xff;
			interleavedIrCameraData[1] = new IrPoint(x1, y1, size1, xmin1, ymin1, xmax1, ymax1, intensity1);
		}
		
		if (reportMode == ReportModeRequest.DATA_REPORT_0x3f) {
			int x2 = (bytes[5] & 0xff) ^ ((bytes[7] & 0x30) << 4);
			int y2 = (bytes[6] & 0xff) ^ ((bytes[7] & 0xc0) << 2);
			int size2 = bytes[7] & 0x0f;
			int xmin2 = bytes[8] & 0x7f;
			int ymin2 = bytes[9] & 0x7f;
			int xmax2 = bytes[10] & 0x7f;
			int ymax2 = bytes[11] & 0x7f;
			int intensity2 = bytes[13] & 0xff;
			interleavedIrCameraData[2] = new IrPoint(x2, y2, size2, xmin2, ymin2, xmax2, ymax2, intensity2);
			
			int x3 = (bytes[14] & 0xff) ^ ((bytes[16] & 0x30) << 4);
			int y3 = (bytes[15] & 0xff) ^ ((bytes[16] & 0xc0) << 2);
			int size3 = bytes[16] & 0x0f;
			int xmin3 = bytes[17] & 0x7f;
			int ymin3 = bytes[18] & 0x7f;
			int xmax3 = bytes[19] & 0x7f;
			int ymax3 = bytes[20] & 0x7f;
			int intensity3 = bytes[22] & 0xff;
			interleavedIrCameraData[3] = new IrPoint(x3, y3, size3, xmin3, ymin3, xmax3, ymax3, intensity3);
		}
		
		if (interleavedIrCameraData[0] != null &&
				interleavedIrCameraData[2] != null) {
			IrPoint p0 = interleavedIrCameraData[0];
			IrPoint p1 = interleavedIrCameraData[1];
			IrPoint p2 = interleavedIrCameraData[2];
			IrPoint p3 = interleavedIrCameraData[3];
			interleavedIrCameraData = null;
			source.fireIrCameraEvent(IrCameraMode.FULL, p0, p1, p2, p3);
		}
	}

	protected void parseInterleavedAccelerometerData(byte[] bytes, int reportMode) {
		System.out.println("parse interleaved");
		int x = 0;
		int y = 0;
		int z = 0;
		
		if (reportMode == ReportModeRequest.DATA_REPORT_0x3e) {
			x = bytes[4] & 0xff;
			z = ((bytes[3] & 0x60) << 1) ^ ((bytes[2] & 0x60) >> 1);
		}
		
		if (reportMode == ReportModeRequest.DATA_REPORT_0x3f) {
			y = bytes[4] & 0xff;
			z = ((bytes[3] & 0x60) >> 3) ^ ((bytes[2] & 0x60) >> 5);
		}
		
		if (interleavedAccelerometerData == null) {
			interleavedAccelerometerData = new int[3];
			interleavedAccelerometerData[0] ^= x;
			interleavedAccelerometerData[1] ^= y;
			interleavedAccelerometerData[2] ^= z;
		} else {
			x ^= interleavedAccelerometerData[0];
			y ^= interleavedAccelerometerData[1];
			z ^= interleavedAccelerometerData[2];
			interleavedAccelerometerData = null;
			source.fireAccelerometerEvent(x, y, z);
		}
	}

	protected void parseMemoryData(byte[] bytes) {
		int size = ((bytes[4] >> 4) & 0x0f) + 1;
		int error = bytes[4] & 0x0f;
		byte[] address = new byte[] { bytes[5], bytes[6] };
		byte[] payload = new byte[size];

		System.arraycopy(bytes, 7, payload, 0, size);

		source.fireReadDataEvent(address, payload, error);
	}

	protected void parseStatusInformation(byte[] bytes) {
		boolean[] leds = new boolean[] { (bytes[4] & 0x10) == 0x10,
				(bytes[4] & 0x20) == 0x20, (bytes[4] & 0x40) == 0x40,
				(bytes[4] & 0x80) == 0x80 };
		boolean extensionControllerConnected = (bytes[4] & 0x02) == 0x02;
		boolean speakerEnabled = (bytes[4] & 0x04) == 0x04;
		boolean continuousReportingEnabled = (bytes[4] & 0x08) == 0x08;
		byte batteryLevel = bytes[7];
		
		StatusInformationReport info = new StatusInformationReport(leds, speakerEnabled, continuousReportingEnabled, extensionControllerConnected, batteryLevel);
		source.fireStatusInformationChangedEvent(info);
	}
	
	  private void initImplied()
	  {
	    this.isStill = isStill(this.xAcceleration, this.yAcceleration, this.zAcceleration);
	    if (this.isStill)
	    {
	      if (this.yAcceleration > 1.0D) this.pitch = 1.570796326794897D;
	      else if (this.yAcceleration < -1.0D) this.pitch = -1.570796326794897D;
	      else {
	        this.pitch = Math.asin(this.yAcceleration);
	      }

	      if (this.xAcceleration > 1.0D) { this.roll = 1.570796326794897D;
	      } else if (this.xAcceleration < -1.0D) { this.roll = -1.570796326794897D;
	      } else {
	        this.roll = Math.asin(this.xAcceleration);
	        if (this.zAcceleration < 0.0D) {
	          this.roll = (3.141592653589793D - this.roll);
	        }
	        this.roll = ((this.roll + 6.283185307179586D) % 6.283185307179586D);
	      }
	    }
	    else
	    {
	      this.pitch = (0.0D / 0.0D);
	      this.roll = (0.0D / 0.0D);
	    }
	  }
	  
	  private static boolean isStill(double paramDouble1, double paramDouble2, double paramDouble3)
	  {
	    double d = Math.sqrt(paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2 + paramDouble3 * paramDouble3);
	    return (d > 0.7D) && (d < 1.4D);
	  }

	public void run() {
		while (active) {
			try {
				byte[] buf = new byte[23];
				incoming.receive(buf);
				
				if (log.isTraceEnabled()) {
					StringBuffer sb = new StringBuffer();
					log.trace("received:");
					for (int i = 0; i < 23; i++) {
						String hex = Integer.toHexString(buf[i] & 0xff);
						sb.append(hex.length() == 1 ? "0x0" : "0x").append(hex).append(" ");
						if ((i + 1) % 8 == 0) {
							log.trace(sb.toString());
							sb.delete(0, sb.length());
						}
					}
					if (sb.length() > 0) {
						log.trace(sb.toString());
					}
				}
				
				switch (buf[1]) {
				case ReportModeRequest.DATA_REPORT_0x20:
					parseStatusInformation(buf);
					break;

				case ReportModeRequest.DATA_REPORT_0x21:
					parseCoreButtonData(buf);
					parseMemoryData(buf);
					break;

				case ReportModeRequest.DATA_REPORT_0x30:
					parseCoreButtonData(buf);
					break;

				case ReportModeRequest.DATA_REPORT_0x31:
					parseCoreButtonData(buf);
					parseAccelerometerData(buf);
					break;

				case ReportModeRequest.DATA_REPORT_0x32:
					parseCoreButtonData(buf);
					parseExtensionData(buf, 4, 8);
					break;

				case ReportModeRequest.DATA_REPORT_0x33:
					parseCoreButtonData(buf);
					parseAccelerometerData(buf);
					parseExtendedIrCameraData(buf, 7);
					break;

				case ReportModeRequest.DATA_REPORT_0x34:
					parseCoreButtonData(buf);
					parseExtensionData(buf, 4, 19);
					break;

				case ReportModeRequest.DATA_REPORT_0x35:
					parseCoreButtonData(buf);
					parseAccelerometerData(buf);
					parseExtensionData(buf, 7, 16);
					break;

				case ReportModeRequest.DATA_REPORT_0x36:
					parseCoreButtonData(buf);
					parseBasicIrCameraData(buf, 4);
					parseExtensionData(buf, 14, 9);
					break;

				case ReportModeRequest.DATA_REPORT_0x37:
					parseCoreButtonData(buf);
					parseAccelerometerData(buf);
					parseBasicIrCameraData(buf, 7);
					parseExtensionData(buf, 17, 6);
					break;

				case ReportModeRequest.DATA_REPORT_0x3d:
					parseExtensionData(buf, 2, 21);
					break;

				case ReportModeRequest.DATA_REPORT_0x3e:
					parseCoreButtonData(buf);
					parseInterleavedAccelerometerData(buf, ReportModeRequest.DATA_REPORT_0x3e);
					parseFullIrCameraData(buf, ReportModeRequest.DATA_REPORT_0x3e);
					break;

				case ReportModeRequest.DATA_REPORT_0x3f:
					parseCoreButtonData(buf);
					parseInterleavedAccelerometerData(buf, ReportModeRequest.DATA_REPORT_0x3f);
					parseFullIrCameraData(buf, ReportModeRequest.DATA_REPORT_0x3f);
					break;

				default:
					if (log.isDebugEnabled()) {
						String hex = Integer.toHexString(buf[1] & 0xff);
						log.debug("Unknown or not yet implemented data report: " + (hex.length() == 1 ? "0x0" + hex : "0x" + hex));
					}
				}

				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException ex) {
				log.error("incoming wiimote thread interrupted.", ex);
			} catch (IOException ex) {
				log.error("connection closed?", ex);
				active = false;
				// Only fire a disconnection event
				// when something goes wrong
				source.fireMoteDisconnectedEvent();
			}
		}
		try {
			incoming.close();
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	public void setExtension(Extension extension) {
		this.extension = extension;
	}
	
	public class AccelerationConstants
	{
	  private double xZero;
	  private double yZero;
	  private double zZero;
	  private double xOne;
	  private double yOne;
	  private double zOne;

	  public AccelerationConstants(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
	  {
	    this.xZero = paramDouble1;
	    this.yZero = paramDouble2;
	    this.zZero = paramDouble3;

	    this.xOne = (paramDouble4 - paramDouble1);
	    this.yOne = (paramDouble5 - paramDouble2);
	    this.zOne = (paramDouble6 - paramDouble3);
	  }

	  public double xZero()
	  {
	    return this.xZero;
	  }

	  public double yZero()
	  {
	    return this.yZero;
	  }

	  public double zZero()
	  {
	    return this.zZero;
	  }

	  public double xOne()
	  {
	    return this.xOne;
	  }

	  public double yOne()
	  {
	    return this.yOne;
	  }

	  public double zOne()
	  {
	    return this.zOne;
	  }
	}
}
