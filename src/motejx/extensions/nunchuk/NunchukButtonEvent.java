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

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class NunchukButtonEvent {

	public static final int NO_BUTTON = 1;
	
	public static final int BUTTON_C = 0x02;
	
	public static final int BUTTON_Z = 0x01;
	
	private int modifiers;
	
	private Nunchuk source;
	
	public NunchukButtonEvent(Nunchuk source, int modifiers) {
		this.source = source;
		this.modifiers = modifiers;
	}
	
	public int getButton() {
		return modifiers;
	}
	
	public Nunchuk getSource() {
		return source;
	}
	
	public boolean isButtonCPressed() {
		return (BUTTON_C & modifiers) == BUTTON_C;
	}
	
	public boolean isButtonZPressed() {
		return (BUTTON_Z & modifiers) == BUTTON_Z;
	}
	
	public boolean isNoButtonPressed() {
		return modifiers == NO_BUTTON;
	}
}
