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

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public enum IrCameraSensitivity {
	
	/**
	 * Sensitivity Settings as provided by <a href="http://www.wiibrew.org/wiki/Wiimote#Sensitivity_Settings">Marcan</a>.
	 */
	MARCAN(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x90, 0x00,
			(byte) 0xc0 }, new byte[] { 0x40, 0 }),

	/**
	 * Sensitivity settings as provided by <a href="http://www.wiibrew.org/wiki/Wiimote#Sensitivity_Settings">inio</a>.
	 */
	INIO(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x90, 0x00, 0x41 },
			new byte[] { 0x40, 0x00 }),
			
	WII_LEVEL_1(new byte[] { 0x02, 0x00, 0x00, 0x71, 0x01, 0x00, 0x64, 0x00, (byte) 0xfe }, new byte[] { (byte) 0xfd, 0x05 }),
	
	WII_LEVEL_2(new byte[] { 0x02, 0x00, 0x00, 0x71, 0x01, 0x00, (byte) 0x96, 0x00, (byte) 0xb4 }, new byte[] { (byte) 0xb3, 0x04 }),
	
	WII_LEVEL_3(new byte[] { 0x02, 0x00, 0x00, 0x71, 0x01, 0x00, (byte) 0xaa, 0x00, 0x64 }, new byte[] { 0x63, 0x03 }),
	
	WII_LEVEL_4(new byte[] { 0x02, 0x00, 0x00, 0x71, 0x01, 0x00, (byte) 0xc8, 0x00, 0x36 }, new byte[] { 0x35, 0x03 }),
	
	WII_LEVEL_5(new byte[] { 0x07, 0x00, 0x00, 0x71, 0x01, 0x00, 0x72, 0x00, 0x20 }, new byte[] { 0x1f, 0x03 });

	private final byte[] block1;
	private final byte[] block2;

	private IrCameraSensitivity(byte[] block1, byte[] block2) {
		this.block1 = block1;
		this.block2 = block2;
	}

	public byte[] block1() {
		return block1;
	}

	public byte[] block2() {
		return block2;
	}
}
