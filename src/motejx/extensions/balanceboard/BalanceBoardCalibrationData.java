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


/**
 * 
 * <p>
 * @author Kohei Matsumura
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class BalanceBoardCalibrationData {

	private int[] sensorA = new int[3];
	private int[] sensorB = new int[3];
	private int[] sensorC = new int[3];
	private int[] sensorD = new int[3];

	public static enum Sensor {
		A, B, C, D;
	}
	
	public static enum Weight {
		KG_0(0), KG_17(1), KG_34(2);
		
		private final int idx;
		private Weight(int idx) {
			this.idx = idx;
		}
	}

	public static final int SENSOR_A = 0;
	public static final int SENSOR_B = 1;
	public static final int SENSOR_C = 2;
	public static final int SENSOR_D = 4;

	public static final int WEIGHT_0_KG = 0;
	public static final int WEIGHT_17_KG = 1;
	public static final int WEIGHT_34_KG = 2;

	public int getCalibration(Sensor sensor, Weight weight) {
		int[] tmp = null;

		switch (sensor) {
		case A:
			tmp = sensorA;
			break;
		case B:
			tmp = sensorB;
			break;
		case C:
			tmp = sensorC;
			break;
		case D:
			tmp = sensorD;
			break;
		default:
			throw new RuntimeException("No such sensor.");
		}
		
		return tmp[weight.idx];
	}
	
	public void setCalibration(Sensor sensor, Weight weight, int data) {
		switch (sensor) {
		case A:
			sensorA[weight.idx] = data;
		case B:
			sensorB[weight.idx] = data;
		case C:
			sensorC[weight.idx] = data;
		case D:
			sensorD[weight.idx] = data;
		}
	}

//	public void setCalibration(char sensor, byte[] calibrationData, int index) {
//		System.out.println(index);
//		switch (sensor) {
//		case SENSOR_A:
//			this.calibrationA.add(index, ((calibrationData[0] & 0xff) * 256)
//					+ (calibrationData[1] & 0xff));
//			break;
//		case SENSOR_B:
//			this.calibrationB.add(index, ((calibrationData[0] & 0xff) * 256)
//					+ (calibrationData[1] & 0xff));
//			break;
//		case SENSOR_C:
//			this.calibrationC.add(index, ((calibrationData[0] & 0xff) * 256)
//					+ (calibrationData[1] & 0xff));
//			break;
//		case SENSOR_D:
//			this.calibrationD.add(index, ((calibrationData[0] & 0xff) * 256)
//					+ (calibrationData[1] & 0xff));
//			break;
//		default:
//			;
//		}
//	}
}
