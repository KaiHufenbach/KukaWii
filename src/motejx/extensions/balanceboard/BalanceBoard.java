/*
 * Copyright 2008 motej
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
package motejx.extensions.balanceboard;

import javax.swing.event.EventListenerList;

import motej.AbstractExtension;
import motej.Mote;
import motej.event.DataEvent;
import motej.event.DataListener;
import motejx.extensions.balanceboard.BalanceBoardCalibrationData.Sensor;
import motejx.extensions.balanceboard.BalanceBoardCalibrationData.Weight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * 
 * @author Kohei Matsumura
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class BalanceBoard extends AbstractExtension implements DataListener {

	private Mote mote;
	
	private BalanceBoardCalibrationData calibrationData = new BalanceBoardCalibrationData();
	
	private Logger log = LoggerFactory.getLogger(BalanceBoard.class);
	
	private EventListenerList listenerList = new EventListenerList();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see motej.event.DataListener#dataRead(motej.event.DataEvent)
	 */
	public void dataRead(DataEvent evt) {
		
		if (evt.getError() == 0
				&& evt.getAddress()[0] == 0x00
				&& (evt.getAddress()[1] & 0xff) == 0x24
				&& evt.getPayload().length == 16 ) {

			log.debug("First Part of Balance Board Calibration Data received.");

			byte[] payload = evt.getPayload();

			calibrationData.setCalibration(Sensor.A, Weight.KG_0, ((payload[0] & 0xff) << 8) ^ (payload[1] & 0xff));
			calibrationData.setCalibration(Sensor.B, Weight.KG_0, ((payload[2] & 0xff) << 8) ^ (payload[3] & 0xff));
			calibrationData.setCalibration(Sensor.C, Weight.KG_0, ((payload[4] & 0xff) << 8) ^ (payload[5] & 0xff));
			calibrationData.setCalibration(Sensor.D, Weight.KG_0, ((payload[6] & 0xff) << 8) ^ (payload[7] & 0xff));
			
			calibrationData.setCalibration(Sensor.A, Weight.KG_17, ((payload[8] & 0xff) << 8) ^ (payload[9] & 0xff));
			calibrationData.setCalibration(Sensor.B, Weight.KG_17, ((payload[10] & 0xff) << 8) ^ (payload[11] & 0xff));
			calibrationData.setCalibration(Sensor.C, Weight.KG_17, ((payload[12] & 0xff) << 8) ^ (payload[13] & 0xff));
			calibrationData.setCalibration(Sensor.D, Weight.KG_17, ((payload[14] & 0xff) << 8) ^ (payload[15] & 0xff));
		}
		
		if (evt.getError() == 0
				&& evt.getAddress()[0] == 0x00
				&& (evt.getAddress()[1] &0xff) == 0x34
				&& evt.getPayload().length == 8) {
			
			log.debug("Second Part of Balance Board Calibration Data received.");

			byte[] payload = evt.getPayload();
			
			calibrationData.setCalibration(Sensor.A, Weight.KG_34, ((payload[0] & 0xff) << 8) ^ (payload[1] & 0xff));
			calibrationData.setCalibration(Sensor.B, Weight.KG_34, ((payload[2] & 0xff) << 8) ^ (payload[3] & 0xff));
			calibrationData.setCalibration(Sensor.C, Weight.KG_34, ((payload[4] & 0xff) << 8) ^ (payload[5] & 0xff));
			calibrationData.setCalibration(Sensor.D, Weight.KG_34, ((payload[6] & 0xff) << 8) ^ (payload[7] & 0xff));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see motej.Extension#initialize()
	 */
	public void initialize() {
		mote.addDataListener(this);
		
		// initialize
		mote.writeRegisters(new byte[] { (byte) 0xa4, 0x00, 0x40}, new byte[] { 0x00 });
		
		// request calibration data
		mote.readRegisters(new byte[] { (byte) 0xa4, 0x00, 0x24 }, new byte[] { 0x00, 0x18 }); 
	}

	public BalanceBoardCalibrationData getCalibrationData() {
		return calibrationData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see motej.Extension#parseExtensionData(byte[])
	 */
	public void parseExtensionData(byte[] extensionData) {
		fireBalanceBoardEvent(extensionData);
	}

	protected void fireBalanceBoardEvent(byte[] extensionData) {
		BalanceBoardListener[] listeners = listenerList.getListeners(BalanceBoardListener.class);
		if (listeners.length == 0) {
			return;
		}
		
//		if (log.isDebugEnabled()) {
//			StringBuffer sb = new StringBuffer();
//			log.debug("received:");
//			for (int i = 0; i < extensionData.length; i++) {
//				String hex = Integer.toHexString(extensionData[i] & 0xff);
//				sb.append(hex.length() == 1 ? "0x0" : "0x").append(hex).append(" ");
//				if ((i + 1) % 8 == 0) {
//					log.debug(sb.toString());
//					sb.delete(0, sb.length());
//				}
//			}
//			if (sb.length() > 0) {
//				log.debug(sb.toString());
//			}
//		}
		
		int weightA = ((extensionData[0] & 0xff) << 8) ^ (extensionData[1] & 0xff);
		int weightB = ((extensionData[2] & 0xff) << 8) ^ (extensionData[3] & 0xff);
		int weightC = ((extensionData[4] & 0xff) << 8) ^ (extensionData[5] & 0xff);
		int weightD = ((extensionData[6] & 0xff) << 8) ^ (extensionData[7] & 0xff);
//		log.debug("Weights: " + weightA + " - " + weightB + " - " + weightC + " - " + weightD);
		
		BalanceBoardEvent evt = new BalanceBoardEvent(this, weightA, weightB, weightC, weightD);
		for (BalanceBoardListener l : listeners) {
			l.balanceBoardChanged(evt);
		}
	}

	public void addBalanceBoardListener(BalanceBoardListener listener) {
		listenerList.add(BalanceBoardListener.class, listener);
	}
	
	public void removeBalanceBoardListener(BalanceBoardListener listener) {
		listenerList.remove(BalanceBoardListener.class, listener);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see motej.Extension#setMote(motej.Mote)
	 */
	public void setMote(Mote mote) {
		this.mote = mote;
	}

}
