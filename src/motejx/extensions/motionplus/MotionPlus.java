/*
 * Copyright 2009 Jan Loesbrock
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

package motejx.extensions.motionplus;

import java.util.Collections;
import java.util.Vector;
import javax.swing.event.EventListenerList;
import motej.AbstractExtension;
import motej.Mote;
import motej.event.DataEvent;
import motej.event.DataListener;

public class MotionPlus extends AbstractExtension implements DataListener{

	final static int calibationLength = 80;
	private EventListenerList listenerList = new EventListenerList();
	
	private boolean yawCalibrated = false;
	private boolean rollCalibrated = false;
	private boolean pitchCalibrated = false;
	private MotionPlusCalibrationData calibrationData;
		
	private Vector<Integer> yawCalibrationData;
	private Vector<Integer> rollCalibrationData;
	private Vector<Integer> pitchCalibrationData;
	
	private Mote mote;
	
	private double absYaw=0;
	private double absRoll=0;
	private double absPitch=0;
	
	private double yawVelocity;
	private double rollVelocity;
	private double pitchVelocity;
	
	private MotionPlusEvent lastEvt = null;
	private long lastEvtTime;
	private WiiMotionPlusCalibrationData localWiiMotionPlusCalibrationData;
		

	public void initialize() {
		
		yawCalibrationData = new Vector<Integer>(calibationLength,calibationLength);
		rollCalibrationData = new Vector<Integer>(calibationLength,calibationLength);
		pitchCalibrationData = new Vector<Integer>(calibationLength,calibationLength);
		this.calibrationData = new MotionPlusCalibrationData();
		this.mote.addDataListener(this);
		
//		System.out.println("MotionPlus initilized");
	}


	public void parseExtensionData(byte[] extensionData) {
		fireEvent(extensionData);
	}


	public void setMote(Mote mote) {
		// TODO Auto-generated method stub
		this.mote = mote;
	}
	
	protected void fireEvent(byte[] data){
		
//		int i = data[0] + ((data[3] & 0xFC) << 6);
//	    int j = data[1] + ((data[4] & 0xFC) << 6);
//	    int k = data[2] + ((data[5] & 0xFC) << 6);
//
//	    boolean bool1 = (data[3] & 0x02)>0?true:false;
//	    boolean bool2 = (data[3] & 0x01)>0?true:false; 
//	    boolean bool3 = (data[4] & 0x02)>0?true:false;
//
//	    if(localWiiMotionPlusCalibrationData==null)
//	    localWiiMotionPlusCalibrationData = new WiiMotionPlusCalibrationData(8063.0D, 8063.0D, 8063.0D, 20.0D, 20.0D, 20.0D, 8063.0D, 8063.0D, 8063.0D, 4.0D, 4.0D, 4.0D);
//	    this.yawVelocity = ((i - ((bool1) ? localWiiMotionPlusCalibrationData.getYawZeroLow() : localWiiMotionPlusCalibrationData.getYawZeroHigh())) / ((bool1) ? localWiiMotionPlusCalibrationData.getYawOneLow() : localWiiMotionPlusCalibrationData.getYawOneHigh()));
//	    this.rollVelocity = ((j - ((bool3) ? localWiiMotionPlusCalibrationData.getRollZeroLow() : localWiiMotionPlusCalibrationData.getRollZeroHigh())) / ((bool3) ? localWiiMotionPlusCalibrationData.getRollOneLow() : localWiiMotionPlusCalibrationData.getRollOneHigh()));
//	    this.pitchVelocity = ((k - ((bool2) ? localWiiMotionPlusCalibrationData.getPitchZeroLow() : localWiiMotionPlusCalibrationData.getPitchZeroHigh())) / ((bool2) ? localWiiMotionPlusCalibrationData.getPitchOneLow() : localWiiMotionPlusCalibrationData.getPitchOneHigh()));
//	  
		
		//////working method
		
		int yaw = 	(data[0] & 0xff) ^ ((data[3] & 0xfc) << 6);  // 0xfc -> 11111100
		int roll = 	(data[1] & 0xff) ^ ((data[4] & 0xfc) << 6);
		int pitch = (data[2] & 0xff) ^ ((data[5] & 0xfc) << 6);
				
		boolean yawSlow =  (data[3] & 0x02)>0?true:false;
		boolean pitchSlow = (data[3] & 0x01)>0?true:false; 
		boolean rollSlow = (data[4] & 0x02)>0?true:false;
		//boolean extensionConnected = (data[4] & 0x01)>0?true:false;

		if (!yawCalibrated || !pitchCalibrated || !rollCalibrated)
		{
			this.calibrate(yaw, roll, pitch);
			return;
		}
		
		double calibratedYaw = ((double) ( yaw - this.calibrationData.getYaw() )) / (yawSlow?20.0:4.0);
		double calibratedRoll = ((double) ( roll - this.calibrationData.getRoll() )) / (rollSlow?20.0:4.0);
		double calibratedPitch = ((double) ( pitch - this.calibrationData.getPitch() )) / (pitchSlow?20.0:4.0);

		 
		if (lastEvt == null)
		{
			lastEvt = new MotionPlusEvent(calibratedYaw, calibratedRoll, calibratedPitch,yaw,roll,pitch); //,yawSlow, pitchSlow, rollSlow, extensionConnected);;
			lastEvtTime = System.currentTimeMillis();
			return;
		}
		

		
//		calibratedYaw = (yawDiff < filterVal)?lastEvt.getYawLeftSpeed():calibratedYaw;
//		calibratedRoll = (rollDiff < filterVal)?lastEvt.getRollLeftSpeed():calibratedRoll;
//		calibratedPitch = (pitchDiff < filterVal)?lastEvt.getPitchDownSpeed():calibratedPitch;
		
		// noise filter  
		double filterVal = 0.5;		
		calibratedYaw = (Math.abs(calibratedYaw) < filterVal)?0:calibratedYaw;
		calibratedRoll = (Math.abs(calibratedRoll) < filterVal)?0:calibratedRoll;
		calibratedPitch = (Math.abs(calibratedPitch) < filterVal)?0:calibratedPitch;
		
		long diffTime = (System.currentTimeMillis()-lastEvtTime);
//		System.out.println(lastEvtTime+" "+diffTime);
		if(diffTime!=0)
		{
			absYaw+=(calibratedYaw/((8192/594)*diffTime*2));
			absRoll+=(calibratedRoll/((8192/594)*diffTime*2));
			absPitch+=(calibratedPitch/((8192/594)*diffTime*2));
		}
//		System.out.println("-----------------------------------------------");
//		System.out.println(yawVelocity+" "+rollVelocity+" "+pitchVelocity);
//		System.out.println(absYaw+" "+absRoll+" "+absPitch);
	
		MotionPlusEvent evt = new MotionPlusEvent(calibratedYaw, calibratedRoll, calibratedPitch,absYaw,absRoll,absPitch); //,yawSlow, pitchSlow, rollSlow, extensionConnected);
		lastEvt = evt;
		lastEvtTime = System.currentTimeMillis();
		
		MotionPlusListener[] listener = listenerList.getListeners(MotionPlusListener.class);
		
		for (MotionPlusListener l : listener) {
			l.speedChanged(evt);
		}
		
	}


	public void dataRead(DataEvent evt) {
		// TODO Auto-generated method stub
	}
	
	public void addMotionPlusEventListener(MotionPlusListener listener)
	{
		listenerList.add(MotionPlusListener.class, listener);
	}
	
	public void removeMotionPlusListener( MotionPlusListener listener)
	{
		listenerList.remove(MotionPlusListener.class, listener);
	}

	private void calibrate(int yaw, int roll, int pitch)
	{
		yawCalibrationData.add(yaw);
		rollCalibrationData.add(roll);
		pitchCalibrationData.add(pitch);
		
		if (yawCalibrationData.size() >= calibationLength && !yawCalibrated)
		{	
			//Check calibationLength values
			Vector<Integer> vec = new Vector<Integer>(calibationLength);
			vec.addAll( yawCalibrationData.subList( yawCalibrationData.size()-calibationLength, yawCalibrationData.size()) );
			Collections.sort(vec);
			int min = vec.firstElement(); 
			int max = vec.lastElement() ;
			int diff = max-min;
			if ( diff <= 55 )
			{
				this.calibrationData.setYaw((int)median(vec));
				this.yawCalibrated = true;
			}
		}
		if (rollCalibrationData.size() >= calibationLength && !rollCalibrated)
		{	
			//Check calibationLength values
			Vector<Integer> vec = new Vector<Integer>(calibationLength);
			vec.addAll( rollCalibrationData.subList( rollCalibrationData.size()-calibationLength, rollCalibrationData.size()) );
			Collections.sort(vec);
			int min = vec.firstElement(); 
			int max = vec.lastElement() ;
			int diff = max-min;
			if ( diff <= 55 )
			{
				this.calibrationData.setRoll((int)median(vec));
				this.rollCalibrated = true;
			}
		}
		if (pitchCalibrationData.size() >= calibationLength && !pitchCalibrated)
		{	
			
			
			//Check calibationLength values
			Vector<Integer> vec = new Vector<Integer>(calibationLength);
			vec.addAll( pitchCalibrationData.subList( pitchCalibrationData.size()-calibationLength, pitchCalibrationData.size()) );
			Collections.sort(vec);
			int min = vec.firstElement(); 
			int max = vec.lastElement() ;
			int diff = max-min;
			if ( diff <= 55 )
			{	
				this.calibrationData.setPitch((int)median(vec));
				this.pitchCalibrated = true;
			}
		}
	}
	
	public void newCalibration()
	{
		this.calibrationData = new MotionPlusCalibrationData();
		this.yawCalibrationData = new Vector<Integer>(calibationLength,calibationLength);
		this.yawCalibrated = false;
		this.rollCalibrationData = new Vector<Integer>(calibationLength,calibationLength);
		this.rollCalibrated = false;
		this.pitchCalibrationData = new Vector<Integer>(calibationLength,calibationLength);
		this.pitchCalibrated = false;
	}
	

	private double median(Vector<Integer> m) {
	    int middle = m.size()/2;  // subscript of middle element
	    if (m.size()%2 == 1) {
	        // Odd number of elements -- return the middle one.
	        return m.get(middle);
	    } else {
	       // Even number -- return average of middle two
	       // Must cast the numbers to double before dividing.
	       return (m.get(middle-1) + m.get(middle)) / 2.0;
	    }
	}
	public void resetAbsolutAngles(){
		this.absPitch=0;
		this.absRoll=0;
		this.absYaw=0;
		
	}
	
	public class WiiMotionPlusCalibrationData
	{
	  private double yawZeroLow;
	  private double pitchZeroLow;
	  private double rollZeroLow;
	  private double yawOneLow;
	  private double pitchOneLow;
	  private double rollOneLow;
	  private double yawZeroHigh;
	  private double pitchZeroHigh;
	  private double rollZeroHigh;
	  private double yawOneHigh;
	  private double pitchOneHigh;
	  private double rollOneHigh;

	  public WiiMotionPlusCalibrationData(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, double paramDouble11, double paramDouble12)
	  {
	    this.yawZeroLow = paramDouble1;
	    this.pitchZeroLow = paramDouble2;
	    this.rollZeroLow = paramDouble3;
	    this.yawOneLow = paramDouble4;
	    this.pitchOneLow = paramDouble5;
	    this.rollOneLow = paramDouble6;
	    this.yawZeroHigh = paramDouble7;
	    this.pitchZeroHigh = paramDouble8;
	    this.rollZeroHigh = paramDouble9;
	    this.yawOneHigh = paramDouble10;
	    this.pitchOneHigh = paramDouble11;
	    this.rollOneHigh = paramDouble12;
	  }

	  public double getYawZeroLow()
	  {
	    return this.yawZeroLow;
	  }

	  public double getPitchZeroLow()
	  {
	    return this.pitchZeroLow;
	  }

	  public double getRollZeroLow()
	  {
	    return this.rollZeroLow;
	  }

	  public double getYawOneLow()
	  {
	    return this.yawOneLow;
	  }

	  public double getPitchOneLow()
	  {
	    return this.pitchOneLow;
	  }

	  public double getRollOneLow()
	  {
	    return this.rollOneLow;
	  }

	  public double getYawZeroHigh()
	  {
	    return this.yawZeroHigh;
	  }

	  public double getPitchZeroHigh()
	  {
	    return this.pitchZeroHigh;
	  }

	  public double getRollZeroHigh()
	  {
	    return this.rollZeroHigh;
	  }

	  public double getYawOneHigh()
	  {
	    return this.yawOneHigh;
	  }

	  public double getPitchOneHigh()
	  {
	    return this.pitchOneHigh;
	  }

	  public double getRollOneHigh()
	  {
	    return this.rollOneHigh;
	  }
	}
}
