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
package motejx.extensions.classic;

import java.awt.Point;

import javax.swing.event.EventListenerList;

import motej.AbstractExtension;
import motej.Mote;

/**
 * As of now, this classic controller implementation lacks calibration data.
 *  
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class ClassicController extends AbstractExtension {

	private EventListenerList listenerList = new EventListenerList();
	
	private Mote mote;
	
	public void addClassicControllerAnalogListener(ClassicControllerAnalogListener listener) {
		listenerList.add(ClassicControllerAnalogListener.class, listener);
	}
	
	public void addClassicControllerButtonListener(ClassicControllerButtonListener listener) {
		listenerList.add(ClassicControllerButtonListener.class, listener);
	}
	
	protected void fireAnalogEvents(byte[] data) {
		ClassicControllerAnalogListener[] listeners = listenerList.getListeners(ClassicControllerAnalogListener.class);
		if (listeners.length == 0) {
			return;
		}
		
		int lx = data[0] & 0x3f;
		int ly = data[1] & 0x3f;
		int rx = ((data[0] & 0xc0) >> 3) ^ ((data[1] & 0xc0) >> 5) ^ ((data[2] & 0x80) >> 7);
		int ry = data[2] & 0x1f;
		int rt = data[3] & 0x1f;
		int lt = ((data[2] & 0x60) >> 2) ^ ((data[3] & 0xe0) >> 5);
		
		LeftAnalogStickEvent leftStickEvt = new LeftAnalogStickEvent(this, new Point(lx, ly));
		RightAnalogStickEvent rightStickEvt = new RightAnalogStickEvent(this, new Point(rx, ry));
		LeftAnalogTriggerEvent leftTriggerEvt = new LeftAnalogTriggerEvent(this, lt);
		RightAnalogTriggerEvent rightTriggerEvt = new RightAnalogTriggerEvent(this, rt);
		
		for (ClassicControllerAnalogListener l : listeners) {
			l.leftAnalogStickChanged(leftStickEvt);
			l.rightAnalogStickChanged(rightStickEvt);
			l.leftAnalogTriggerChanged(leftTriggerEvt);
			l.rightAnalogTriggerChanged(rightTriggerEvt);
		}
	}

	protected void fireButtonEvent(byte[] data) {
		ClassicControllerButtonListener[] listeners = listenerList
				.getListeners(ClassicControllerButtonListener.class);
		if (listeners.length == 0) {
			return;
		}

		// we invert the original data as the classic controller returns
		// button pressed as nil and thats not that useable.
		int modifiers = (((data[5] & 0xff) ^ 0xff) << 8) ^ ((data[4] & 0xff) ^ 0xff);

		ClassicControllerButtonEvent evt = new ClassicControllerButtonEvent(this, modifiers);
		for (ClassicControllerButtonListener l : listeners) {
			l.buttonPressed(evt);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see motej.Extension#initialize()
	 */
	public void initialize() {
		// initialize
		mote.writeRegisters(new byte[] { (byte) 0xa4, 0x00, 0x40}, new byte[] { 0x00 });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see motej.Extension#parseExtensionData(byte[])
	 */
	public void parseExtensionData(byte[] extensionData) {
		decrypt(extensionData);
		
		fireAnalogEvents(extensionData);
		fireButtonEvent(extensionData);
	}
	
	public void removeClassicControllerAnalogListener(ClassicControllerAnalogListener listener) {
		listenerList.remove(ClassicControllerAnalogListener.class, listener);
	}
	
	public void removeClassicControllerButtonListener(ClassicControllerButtonListener listener) {
		listenerList.remove(ClassicControllerButtonListener.class, listener);
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
