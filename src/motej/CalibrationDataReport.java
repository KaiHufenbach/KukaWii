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
 * Calibration data for the onboard accelerometer (as stored in the Wiimote's
 * memory, starting at address 0x16 and repeated at 0x20).
 * <p>
 * 
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class CalibrationDataReport {

	private int zeroX;

	private int zeroY;

	private int zeroZ;

	private int gravityX;

	private int gravityY;

	private int gravityZ;

	public CalibrationDataReport(int zeroX, int zeroY, int zeroZ,
			int gravityX, int gravityY, int gravityZ) {
		this.zeroX = zeroX;
		this.zeroY = zeroY;
		this.zeroZ = zeroZ;
		this.gravityX = gravityX;
		this.gravityY = gravityY;
		this.gravityZ = gravityZ;
	}

	/**
	 * Calibrated force of gravity for the accelerometers X axis.
	 * 
	 * @return the force of gravity X axis
	 */
	public int getGravityX() {
		return gravityX;
	}

	/**
	 * Calibrated force of gravity for the accelerometers Y axis.
	 * 
	 * @return the force of gravity Y axis
	 */
	public int getGravityY() {
		return gravityY;
	}

	/**
	 * Calibrated force of gravity for the accelerometers Z axis.
	 * 
	 * @return the force of gravity Z axis
	 */
	public int getGravityZ() {
		return gravityZ;
	}

	/**
	 * Calibrated zero offsets for the accelerometers X axis.
	 * 
	 * @return zero offset X axis
	 */
	public int getZeroX() {
		return zeroX;
	}

	/**
	 * Calibrated zero offsets for the accelerometers Y axis.
	 * 
	 * @return zero offset Y axis
	 */
	public int getZeroY() {
		return zeroY;
	}

	/**
	 * Calibrated zero offsets for the accelerometers Z axis.
	 * 
	 * @return zero offset Z axis
	 */
	public int getZeroZ() {
		return zeroZ;
	}

	@Override
	public String toString() {
		return "CalibrationDataReport[zeroPointAxisX: " + zeroX
				+ ", zeroPointAxisY: " + zeroY + ", zeroPointAxisZ: " + zeroZ
				+ ", plusOneGPointAxisX: " + gravityX
				+ ", plusOneGPointAxisY: " + gravityY
				+ ", plusOneGPointAxisZ: " + gravityZ + "]";
	}
}
