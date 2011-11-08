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
package motej.event;

import motej.Mote;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class CoreButtonEvent {

	public static final int NO_BUTTON = 0;

	public static final int D_PAD_LEFT = 1;

	public static final int D_PAD_RIGHT = 2;

	public static final int D_PAD_UP = 8;

	public static final int D_PAD_DOWN = 4;

	public static final int BUTTON_ONE = 512;

	public static final int BUTTON_TWO = 256;

	public static final int BUTTON_A = 2048;

	public static final int BUTTON_B = 1024;

	public static final int BUTTON_PLUS = 16;

	public static final int BUTTON_MINUS = 4096;

	public static final int BUTTON_HOME = 32768;

	private int modifiers;

	private Mote source;

	public CoreButtonEvent(Mote source, int modifiers) {
		this.source = source;
		this.modifiers = modifiers;
	}

	public int getButton() {
		return modifiers;
	}

	public Mote getSource() {
		return source;
	}
	
	public boolean isButtonAPressed() {
		return (BUTTON_A & modifiers) == BUTTON_A;
	}
	
	public boolean isButtonBPressed() {
		return (BUTTON_B & modifiers) == BUTTON_B;
	}
	
	public boolean isButtonHomePressed() {
		return (BUTTON_HOME & modifiers) == BUTTON_HOME;
	}
	
	public boolean isButtonMinusPressed() {
		return (BUTTON_MINUS & modifiers) == BUTTON_MINUS;
	}
	
	public boolean isButtonPlusPressed() {
		return (BUTTON_PLUS & modifiers) == BUTTON_PLUS;
	}
	
	public boolean isButtonOnePressed() {
		return (BUTTON_ONE & modifiers) == BUTTON_ONE;
	}
	
	public boolean isButtonTwoPressed() {
		return (BUTTON_TWO & modifiers) == BUTTON_TWO;
	}
	
	public boolean isDPadLeftPressed() {
		return (D_PAD_LEFT & modifiers) == D_PAD_LEFT;
	}
	
	public boolean isDPadRightPressed() {
		return (D_PAD_RIGHT & modifiers) == D_PAD_RIGHT;
	}
	
	public boolean isDPadUpPressed() {
		return (D_PAD_UP & modifiers) == D_PAD_UP;
	}
	
	public boolean isDPadDownPressed() {
		return (D_PAD_DOWN & modifiers) == D_PAD_DOWN;
	}

	public boolean isNoButtonPressed() {
		return modifiers == NO_BUTTON;
	}
}
