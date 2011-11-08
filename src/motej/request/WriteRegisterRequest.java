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
public class WriteRegisterRequest implements MoteRequest {

	private byte[] offset;
	
	private byte[] payload;
	
	public WriteRegisterRequest(byte[] offset, byte[] payload) {
		this.offset = offset;
		this.payload = payload;
	}
	
	public byte[] getBytes() {
		// write commands must be padded to 16 data bytes, thus this length
		byte[] data = new byte[23];
		
		// HID command
		data[0] = 0x52;
		
		// output report 0x16
		data[1] = 0x16;
		
		// select control register address space
		data[2] = 0x04;
		
		// set offset
		System.arraycopy(offset, 0, data, 3, 3);
//		for (int i = 0; i < 3; i++) {
//			data[3 + i] = offset[i];
//		}
		
		// size information
		data[6] = (byte) payload.length;
		
		// our payload
		System.arraycopy(payload, 0, data, 7, payload.length);
//		for (int i = 0; i < payload.length; i++) {
//			data[7 + i] = payload[i];
//		}

		return data;
	}

}
