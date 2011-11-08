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
package motej.request;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class ReadRegisterRequest implements MoteRequest {

	private byte[] offset;
	
	private byte[] size;
	
	public ReadRegisterRequest(byte[] offset, byte[] size) {
		this.offset = offset;
		this.size = size;
	}
	
	public byte[] getBytes() {
		byte[] data = new byte[8];
		
		// HID command
		data[0] = 0x52;
		
		// Output report 0x17
		data[1] = 0x17;
		
		// select control register address space
		data[2] = 0x04;
		
		// offset
		System.arraycopy(offset, 0, data, 3, 3);
		
		// size
		System.arraycopy(size, 0, data, 6, 2);
		
		return data;
	}

}
