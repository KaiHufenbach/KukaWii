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
package motejx.extensions.balanceboard;

import motejx.extensions.balanceboard.BalanceBoardCalibrationData.Sensor;
import motejx.extensions.balanceboard.BalanceBoardCalibrationData.Weight;

/**
 * 
 * <p>
 * @author Kohei Matsumura
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class BalanceBoardEvent {
    private BalanceBoard source;
    private int topRight;
    private int bottomRight;
    private int topLeft;
    private int bottomLeft;
    
    public BalanceBoardEvent(BalanceBoard source, int topRight, int bottomRight, int topLeft, int bottomLeft) {
        this.source = source;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.topLeft = topLeft;
        this.bottomLeft = bottomLeft;
    }

	public BalanceBoard getSource() {
		return source;
	}

	public int getTopRight() {
		return topRight;
	}

	public int getBottomRight() {
		return bottomRight;
	}

	public int getTopLeft() {
		return topLeft;
	}

	public int getBottomLeft() {
		return bottomLeft;
	}

	protected int interpolate(Sensor sensor, int raw) {
		BalanceBoardCalibrationData calibrationData = source.getCalibrationData();
		
		if (raw < calibrationData.getCalibration(sensor, Weight.KG_0)) {
			return 0;
		}
		
		if (raw < calibrationData.getCalibration(sensor, Weight.KG_17)) {
			return (int) (0  + (((raw - calibrationData.getCalibration(sensor, Weight.KG_0))  * (17d -  0)) / (calibrationData.getCalibration(sensor, Weight.KG_17) - calibrationData.getCalibration(sensor, Weight.KG_0))));
		}
		
		if (raw < calibrationData.getCalibration(sensor, Weight.KG_34)) {
			return (int) (17d + (((raw - calibrationData.getCalibration(sensor, Weight.KG_17)) * (34d - 17d)) / (calibrationData.getCalibration(sensor, Weight.KG_34) - calibrationData.getCalibration(sensor, Weight.KG_17))));
		}
		
		return (int) (((double) raw / (double) calibrationData.getCalibration(sensor, Weight.KG_34)) * 34d);
	}
	public int getTopRightInterpolated() {
		return interpolate(Sensor.A, topRight);
	}

	public int getBottomRightInterpolated() {
		return interpolate(Sensor.B, bottomRight);
	}

	public int getTopLeftInterpolated() {
		return interpolate(Sensor.C, topLeft);
	}

	public int getBottomLeftInterpolated() {
		return interpolate(Sensor.D, bottomLeft);
	}
	
}
