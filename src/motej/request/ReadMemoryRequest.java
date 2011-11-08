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
public class ReadMemoryRequest implements MoteRequest {

	private byte[] offset;
	
	private byte[] size;
	
	public ReadMemoryRequest(byte[] offset, byte[] size) {
		this.offset = offset;
		this.size = size;
	}
	
	public byte[] getBytes() {
		byte[] data = new byte[8];
		
		data[0] = 0x52;
		data[1] = 0x17;
		
		// eeprom memory address space
		data[2] = 0x00;
		
		System.arraycopy(offset, 0, data, 3, 3);
//		for (int i = 0; i < 3; i++) {
//			data[3 + i] = offset[i];
//		}
		
		System.arraycopy(size, 0, data, 6, 2);
//		for (int i = 0; i < 2; i++) {
//			data[6 + i] = size[i];
//		}
		
		return data;
	}

}
