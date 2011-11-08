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
package motejx.extensions.nunchuk;

import java.awt.Point;

import javax.swing.event.EventListenerList;

import motej.AbstractExtension;
import motej.Mote;
import motej.event.AccelerometerEvent;
import motej.event.AccelerometerListener;
import motej.event.DataEvent;
import motej.event.DataListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * 
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class Nunchuk extends AbstractExtension implements DataListener {

	private EventListenerList listenerList = new EventListenerList();

	private Mote mote;

	private Logger log = LoggerFactory.getLogger(Nunchuk.class);

	private NunchukCalibrationData calibrationData;

	/**
	 * @param listener
	 */
	public void addAccelerometerListener(AccelerometerListener<Nunchuk> listener) {
		listenerList.add(AccelerometerListener.class, listener);
	}

	/**
	 * @param listener
	 */
	public void addAnalogStickListener(AnalogStickListener listener) {
		listenerList.add(AnalogStickListener.class, listener);
	}

	/**
	 * @param listener
	 */
	public void addNunchukButtonListener(NunchukButtonListener listener) {
		listenerList.add(NunchukButtonListener.class, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see motej.event.DataListener#dataRead(motej.event.DataEvent)
	 */
	public void dataRead(DataEvent evt) {
		if (calibrationData == null && evt.getError() == 0
				&& evt.getAddress()[0] == 0x00
				&& (evt.getAddress()[1] & 0xff) == 0x30
				&& evt.getPayload().length == 0x0f) {

			log.debug("Calibration Data received.");

			byte[] payload = evt.getPayload();

			calibrationData = new NunchukCalibrationData();
			calibrationData.setZeroForceX(payload[0] & 0xff);
			calibrationData.setZeroForceY(payload[1] & 0xff);
			calibrationData.setZeroForceZ(payload[2] & 0xff);
			calibrationData.setGravityForceX(payload[4] & 0xff);
			calibrationData.setGravityForceY(payload[5] & 0xff);
			calibrationData.setGravityForceZ(payload[6] & 0xff);
			calibrationData.setMinimumAnalogPoint(new Point(payload[9] & 0xff,
					payload[12] & 0xff));
			calibrationData.setMaximumAnalogPoint(new Point(payload[8] & 0xff,
					payload[11] & 0xff));
			calibrationData.setCenterAnalogPoint(new Point(payload[10] & 0xff,
					payload[13] & 0xff));
		}
	}

	/**
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	protected void fireAccelerometerEvent(byte[] data) {
		AccelerometerListener<Nunchuk>[] listeners = listenerList
				.getListeners(AccelerometerListener.class);
		if (listeners.length == 0) {
			return;
		}

		// remark: I am currently ignoring the LSB as I do with the LSB of the
		// calibration data.
		// if someone comes up with reliable data, we'll add it, i promise.
		int ax = ((data[2] & 0xff)); // << 2) ^ (data[5] & 0x0c);
		int ay = ((data[3] & 0xff)); // << 2) ^ (data[5] & 0x30);
		int az = ((data[4] & 0xff)); // << 2) ^ (data[5] & 0xc0);
		AccelerometerEvent<Nunchuk> evt = new AccelerometerEvent<Nunchuk>(this,
				ax, ay, az);
		for (AccelerometerListener<Nunchuk> l : listeners) {
			l.accelerometerChanged(evt);
		}
	}

	protected void fireAnalogStickEvent(byte[] data) {
		AnalogStickListener[] listeners = listenerList
				.getListeners(AnalogStickListener.class);
		if (listeners.length == 0) {
			return;
		}

		int sx = data[0] & 0xff;
		int sy = data[1] & 0xff;
		AnalogStickEvent evt = new AnalogStickEvent(this, new Point(sx & 0xff,
				sy & 0xff));
		for (AnalogStickListener l : listeners) {
			l.analogStickChanged(evt);
		}
	}

	protected void fireButtonEvent(byte[] data) {
		NunchukButtonListener[] listeners = listenerList
				.getListeners(NunchukButtonListener.class);
		if (listeners.length == 0) {
			return;
		}

		// we invert the original data as the wiimote returns
		// button pressed as nil and thats not that useable.
		int modifiers = (data[5] & 0x03) ^ 0x03;

		NunchukButtonEvent evt = new NunchukButtonEvent(this, modifiers);
		for (NunchukButtonListener l : listeners) {
			l.buttonPressed(evt);
		}
	}

	/**
	 * @return
	 */
	public NunchukCalibrationData getCalibrationData() {
		return calibrationData;
	}

	/**
	 * @return
	 */
	public Mote getMote() {
		return mote;
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
		mote.readRegisters(new byte[] { (byte) 0xa4, 0x00, 0x30 }, new byte[] {
				0x00, 0x0f });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see motej.Extension#parseExtensionData(byte[])
	 */
	public void parseExtensionData(byte[] extensionData) {
		decrypt(extensionData);
		fireAnalogStickEvent(extensionData);
		fireAccelerometerEvent(extensionData);
		fireButtonEvent(extensionData);
	}

	/**
	 * @param listener
	 */
	public void removeAccelerometerListener(
			AccelerometerListener<Nunchuk> listener) {
		listenerList.remove(AccelerometerListener.class, listener);
	}

	/**
	 * @param listener
	 */
	public void removeAnalogStickListener(AnalogStickListener listener) {
		listenerList.remove(AnalogStickListener.class, listener);
	}

	/**
	 * @param listener
	 */
	public void removeNunchukButtonListener(NunchukButtonListener listener) {
		listenerList.remove(NunchukButtonListener.class, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see motej.Extension#setMote(motej.Mote)
	 */
	public void setMote(Mote mote) {
		this.mote = mote;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Nunchuk";
	}
}
