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

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class NunchukCalibrationData {

	private int zeroForceX;

	private int zeroForceY;

	private int zeroForceZ;

	private int gravityForceX;

	private int gravityForceY;

	private int gravityForceZ;
	
	private Point minimumAnalogPoint;
	
	private Point maximumAnalogPoint;
	
	private Point centerAnalogPoint;
	
	public NunchukCalibrationData() {
		
	}

	public int getZeroForceX() {
		return zeroForceX;
	}

	public void setZeroForceX(int zeroForceX) {
		this.zeroForceX = zeroForceX;
	}

	public int getZeroForceY() {
		return zeroForceY;
	}

	public void setZeroForceY(int zeroForceY) {
		this.zeroForceY = zeroForceY;
	}

	public int getZeroForceZ() {
		return zeroForceZ;
	}

	public void setZeroForceZ(int zeroForceZ) {
		this.zeroForceZ = zeroForceZ;
	}

	public int getGravityForceX() {
		return gravityForceX;
	}

	public void setGravityForceX(int gravityForceX) {
		this.gravityForceX = gravityForceX;
	}

	public int getGravityForceY() {
		return gravityForceY;
	}

	public void setGravityForceY(int gravityForceY) {
		this.gravityForceY = gravityForceY;
	}

	public int getGravityForceZ() {
		return gravityForceZ;
	}

	public void setGravityForceZ(int gravityForceZ) {
		this.gravityForceZ = gravityForceZ;
	}

	public Point getMinimumAnalogPoint() {
		return minimumAnalogPoint;
	}

	public void setMinimumAnalogPoint(Point minimumAnalogPoint) {
		this.minimumAnalogPoint = minimumAnalogPoint;
	}

	public Point getMaximumAnalogPoint() {
		return maximumAnalogPoint;
	}

	public void setMaximumAnalogPoint(Point maximumAnalogPoint) {
		this.maximumAnalogPoint = maximumAnalogPoint;
	}

	public Point getCenterAnalogPoint() {
		return centerAnalogPoint;
	}

	public void setCenterAnalogPoint(Point centerAnalogPoint) {
		this.centerAnalogPoint = centerAnalogPoint;
	}

}
