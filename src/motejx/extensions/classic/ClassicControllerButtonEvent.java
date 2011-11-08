/*
 * Copyright 2007-2008 motej
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

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class ClassicControllerButtonEvent {
	
	public static final int BUTTON_A = 4096;
	
	public static final int BUTTON_B = 16384;

	public static final int BUTTON_HOME = 8;

	public static final int BUTTON_LEFT_TRIGGER = 32;

	public static final int BUTTON_MINUS = 16;

	public static final int BUTTON_PLUS = 4;

	public static final int BUTTON_RIGHT_TRIGGER = 2;

	public static final int BUTTON_X = 2048;

	public static final int BUTTON_Y = 8192;
	
	public static final int BUTTON_ZL = 32768;
	
	public static final int BUTTON_ZR = 1024;

	public static final int D_PAD_DOWN = 64;

	public static final int D_PAD_LEFT = 512;

	public static final int D_PAD_RIGHT = 128;
	
	public static final int D_PAD_UP = 256;
	
	public static final int NO_BUTTON = 0;

	private int modifiers;
	
	private ClassicController source;
	
	public ClassicControllerButtonEvent(ClassicController source, int modifiers) {
		this.source = source;
		this.modifiers = modifiers;
	}
	
	public ClassicController getSource() {
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
	
	public boolean isButtonLeftTriggerPressed() {
		return (BUTTON_LEFT_TRIGGER & modifiers) == BUTTON_LEFT_TRIGGER;
	}
	
	public boolean isButtonMinusPressed() {
		return (BUTTON_MINUS & modifiers) == BUTTON_MINUS;
	}
	
	public boolean isButtonPlusPressed() {
		return (BUTTON_PLUS & modifiers) == BUTTON_PLUS;
	}
	
	public boolean isButtonRightTriggerPressed() {
		return (BUTTON_RIGHT_TRIGGER & modifiers) == BUTTON_RIGHT_TRIGGER;
	}
	
	public boolean isButtonXPressed() {
		return (BUTTON_X & modifiers) == BUTTON_X;
	}
	
	public boolean isButtonYPressed() {
		return (BUTTON_Y & modifiers) == BUTTON_Y;
	}
	
	public boolean isButtonZLPressed() {
		return (BUTTON_ZL & modifiers) == BUTTON_ZL;
	}
	
	public boolean isButtonZRPressed() {
		return (BUTTON_ZR & modifiers) == BUTTON_ZR;
	}
	
	public boolean isDPadDownPressed() {
		return (D_PAD_DOWN & modifiers) == D_PAD_DOWN;
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
	
	public boolean isNoButtonPressed() {
		return modifiers == NO_BUTTON;
	}
	
}
