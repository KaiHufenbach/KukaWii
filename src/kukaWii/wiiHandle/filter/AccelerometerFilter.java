package kukaWii.wiiHandle.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import kukaWii.wiiHandle.packet.AbstractPacket;
import kukaWii.wiiHandle.packet.AccelerometerPacket;
import kukaWii.wiiHandle.packet.MotionPlusPacket;

/**
 * Test-Implementation eines Filters, um Rauschen zu verringern, sowie die Erdbeschleunigung zu herauszufiltern
 * @author InternetMini
 *
 */
public class AccelerometerFilter extends AbstractPacketFilter{

	private static final int CALIBRATION_BEGIN = 200;
	private static final int CALIBRATION_END = 1000;
	
	private static final String[] axes = {"Z-Axis", "X-Axis", "Y-Axis"};
	
	private int calibrationAxis = 0;
	
	private int accelPacketCount = 0;
	private int motionPlusPacketCount = 0;
	
	private boolean calibration = false;
	
	
	//3 Durchläufe
	//xMin, xMax, yMin, yMax, zMin, zMax
	private double[][] accelCalibrationData = new double[3][6]; 
	
	//Für min, max....
	private double[] motionPlusCalibrationData = new double[6];
	
	private double pitchDownMin;
	private double pitchOffset;
	private double pitchRelOffsetSize;
	private double pitchDownAmp;
	
	private double pitchDownPred = 0;
	
	private double yawLeftMin;
	private double yawLeftOffset;
	private double yawLeftOffsetSize;
	private double yawLeftAmp;
	
	private double yawLeftPred = 0;
	
	private double rollLeftMin;
	private double rollLeftOffset;
	private double rollLeftOffsetSize;
	private double rollLeftAmp;
	
	private double rollLeftPred = 0;
	
	
	private double xMin = Double.MAX_VALUE;
	private double xGravMin = Double.MAX_VALUE;
	private double xAmp = 0;
	private double xOffset;
	private double xRelOffsetSize;
	
	private double xPred = 0;
	
	
	
	private double yMin = Double.MAX_VALUE;
	private double yGravMin = Double.MAX_VALUE;
	private double yAmp = 0;
	private double yOffset;
	private double yRelOffsetSize;
		
	private double yPred = 0;
	
	
	private double zMin = Double.MAX_VALUE;
	private double zGravMin = Double.MAX_VALUE;
	private double zAmp = 0;
	private double zOffset;
	private double zRelOffsetSize;
	
	private double zPred = 0;

	boolean firstRun = true;
	
	
	@Override
	public AbstractPacket compute(AbstractPacket input) {
		
		if(input instanceof AccelerometerPacket){
			AccelerometerPacket accelPacket = (AccelerometerPacket)input;
			
			if(accelPacketCount < CALIBRATION_END && calibrationAxis < axes.length){
				accelPacketCount++;
				if(accelPacketCount == CALIBRATION_BEGIN){
					System.out.println("Begin Calibration " + axes[calibrationAxis]);
					System.out.println("Press enter to begin...");
					try {
						new BufferedReader(new InputStreamReader(System.in)).readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("Please do not move Wii!");
					if(firstRun){
						resetAccelValues();
						firstRun = false;
					}
					
					calibration = true;
					return null;
				} else if (accelPacketCount == CALIBRATION_END){
					System.out.println("End Calibration: "+axes[calibrationAxis]);
					
					
					
					System.out.println("XMax: "+accelCalibrationData[calibrationAxis][1]+" XMin: "+accelCalibrationData[calibrationAxis][0]+" xAmp: "+(accelCalibrationData[calibrationAxis][1] - accelCalibrationData[calibrationAxis][0]));
					System.out.println("YMax: "+accelCalibrationData[calibrationAxis][3]+" YMin: "+accelCalibrationData[calibrationAxis][2]+" yAmp: "+(accelCalibrationData[calibrationAxis][3]-accelCalibrationData[calibrationAxis][2]));
					System.out.println("ZMax: "+accelCalibrationData[calibrationAxis][5]+" ZMin: "+accelCalibrationData[calibrationAxis][4]+" zAmp: "+(accelCalibrationData[calibrationAxis][5]-accelCalibrationData[calibrationAxis][4]));
					calibration = false;
					accelPacketCount = 0;
					calibrationAxis++;
					if(calibrationAxis == axes.length){
						System.out.println("Calibration finished... processing calibration Values");
						
						xAmp = accelCalibrationData[0][1]-accelCalibrationData[0][0];
						yAmp = accelCalibrationData[0][3]-accelCalibrationData[0][2];
						zAmp = accelCalibrationData[1][5]-accelCalibrationData[1][4];
						
						xMin = accelCalibrationData[0][0];
						xGravMin = accelCalibrationData[1][0];
						yMin = accelCalibrationData[0][2];
						yGravMin = accelCalibrationData[2][2];
						zMin = accelCalibrationData[1][4];
						zGravMin = accelCalibrationData[0][4];
						
						System.out.println("XAmp: "+xAmp+" YAmp: "+yAmp+ " ZAmp: "+zAmp);
						System.out.println("xMin: "+xMin+" xGrav: "+xGravMin+" yMin: "+yMin+ " yGrav: "+yGravMin+" zMin: "+zMin+" zGrav: "+zGravMin);
						
						xOffset = xAmp/2.0;
						yOffset = yAmp/2.0;
						zOffset = zAmp/2.0;
						
						xRelOffsetSize = (xAmp+xOffset)/xAmp;
						yRelOffsetSize = (yAmp+yOffset)/yAmp;
						zRelOffsetSize = (zAmp+zOffset)/zAmp;
					}
				}
				
				else if(calibration){
					double[] tempCalibData = accelCalibrationData[calibrationAxis];
					
					tempCalibData[0] = Math.min(tempCalibData[0], accelPacket.getX());
					tempCalibData[1] = Math.max(tempCalibData[1], accelPacket.getX());
					tempCalibData[2] = Math.min(tempCalibData[2], accelPacket.getY());
					tempCalibData[3] = Math.max(tempCalibData[3], accelPacket.getY());
					tempCalibData[4] = Math.min(tempCalibData[4], accelPacket.getZ());
					tempCalibData[5] = Math.max(tempCalibData[5], accelPacket.getZ());
					
				}
				
				return null;
				
				
			} else{
				//Alte Methode, ohne Abstände zwischen den Schwingungen
//				accelPacket.setX(Math.floor((accelPacket.getX()-xMin)/xAmp)*xAmp);
//				accelPacket.setY(Math.floor((accelPacket.getY()-yMin)/yAmp)*yAmp);
//				accelPacket.setZ(Math.floor((accelPacket.getZ()-zMin)/zAmp)*zAmp);
				
				//Es wird ein Bereich zwischen den Bänden bestimmt: ...Offset, der keine Änderung hervorruft
				
				double xValue;
				double yValue;
				double zValue;
				
				//1. Band bestimmen (Offsetbereich liegt jeweils oberhalb des Bandes
				int xBand = (int)Math.floor((accelPacket.getX()-xMin)/(xAmp+xOffset));
				int yBand = (int)Math.floor((accelPacket.getY()-yMin)/(yAmp+yOffset));
				int zBand = (int)Math.floor((accelPacket.getZ()-zMin)/(zAmp+zOffset));
				
				//2. Liegen wir im Offset Bereich des Bandes, wenn nicht, dann band mal Amplitude
				if(xBand!=0){
					if((accelPacket.getX()-xMin)/(xBand*(xAmp+xOffset))>=xRelOffsetSize){
						xValue = xPred;
					}else{
						xValue = xBand * (xAmp + xOffset);
						xPred = xValue;
					}
				}else{
					xValue = 0;
					xPred = 0;
				}
				
				if(yBand!=0){
					if((accelPacket.getY()-yMin)/(yBand*(yAmp+yOffset))>=yRelOffsetSize){
						yValue = yPred;
					}else{
						yValue = yBand * (yAmp + yOffset);
						yPred = yValue;
					}
				}else{
					yValue = 0;
					yPred = 0;
				}
				
				if(zBand!=0){
					if((accelPacket.getZ()-zMin)/(zBand*(zAmp+zOffset))>=zRelOffsetSize){
						zValue = zPred;
					}else{
						zValue = zBand * (zAmp + zOffset);
						zPred = zValue;
					}
				}else{
					zValue = 0;
					zPred = 0;
				}
				
				
				accelPacket.setX(xValue);
				accelPacket.setY(yValue);
				accelPacket.setZ(zValue);
				
				return accelPacket;
			}
			
			
			
		}
		else if (input instanceof MotionPlusPacket){
			MotionPlusPacket motionPlusPacket = (MotionPlusPacket)input;
			if(motionPlusPacketCount <= CALIBRATION_END){
				if(motionPlusPacketCount == CALIBRATION_BEGIN){
					System.out.println("Begin MotionPlus Calibration...");
					resetMotionPlusValues();
				}else if(motionPlusPacketCount < CALIBRATION_END){
					motionPlusCalibrationData[0] = Math.min(motionPlusCalibrationData[0], motionPlusPacket.getPitchDownSpeed());
					motionPlusCalibrationData[1] = Math.max(motionPlusCalibrationData[1], motionPlusPacket.getPitchDownSpeed());
					motionPlusCalibrationData[2] = Math.min(motionPlusCalibrationData[2], motionPlusPacket.getRollLeftSpeed());
					motionPlusCalibrationData[3] = Math.max(motionPlusCalibrationData[3], motionPlusPacket.getRollLeftSpeed());
					motionPlusCalibrationData[4] = Math.min(motionPlusCalibrationData[4], motionPlusPacket.getYawLeftSpeed());
					motionPlusCalibrationData[5] = Math.max(motionPlusCalibrationData[5], motionPlusPacket.getYawLeftSpeed());
				}else if(motionPlusPacketCount == CALIBRATION_END){
					pitchDownMin = motionPlusCalibrationData[0];
					rollLeftMin = motionPlusCalibrationData[2];
					yawLeftMin = motionPlusCalibrationData[4];
					
					pitchDownAmp = motionPlusCalibrationData[1] - motionPlusCalibrationData[0];
					rollLeftAmp = motionPlusCalibrationData[3] - motionPlusCalibrationData[2];
					yawLeftAmp = motionPlusCalibrationData[5] - motionPlusCalibrationData[4];
					
					pitchOffset = pitchDownAmp/2.0;
					rollLeftOffset = rollLeftAmp/2.0;
					yawLeftOffset = yawLeftAmp/2.0;
					
					pitchRelOffsetSize = (pitchDownAmp+pitchOffset)/pitchDownAmp;
					rollLeftOffsetSize = (rollLeftAmp+rollLeftOffset)/rollLeftAmp;
					yawLeftOffsetSize = (yawLeftAmp+yawLeftOffset)/yawLeftAmp;
					
					System.out.println("Pitch Down: min: "+pitchDownMin+" Roll Left: min: "+rollLeftMin+" Yaw Left min: "+yawLeftMin);
				}
				
				motionPlusPacketCount++;
				return null;
			}else{
				//Es wird ein Bereich zwischen den Bänden bestimmt: ...Offset, der keine Änderung hervorruft
				
				double pitchValue;
				double rollValue;
				double yawValue;
				
				//1. Band bestimmen (Offsetbereich liegt jeweils oberhalb des Bandes
				int pitchBand = (int)Math.floor((motionPlusPacket.getPitchDownSpeed()-pitchDownMin)/(pitchDownAmp+pitchOffset));
				int rollBand = (int)Math.floor((motionPlusPacket.getRollLeftSpeed()-rollLeftMin)/(rollLeftAmp+rollLeftOffset));
				int yawBand = (int)Math.floor((motionPlusPacket.getYawLeftSpeed()-yawLeftMin)/(yawLeftAmp+yawLeftOffset));
				
				//2. Liegen wir im Offset Bereich des Bandes, wenn nicht, dann band mal Amplitude
				if(pitchBand!=0){
					if((motionPlusPacket.getPitchDownSpeed()-pitchDownMin)/(pitchBand*(pitchDownAmp+pitchOffset))>=pitchRelOffsetSize){
						pitchValue = pitchDownPred;
					}else{
						pitchValue = pitchBand * (pitchDownAmp + pitchOffset);
						pitchDownPred = pitchValue;
					}
				}else{
					pitchValue = 0;
					pitchDownPred = 0;
				}
				
				if(rollBand!=0){
					if((motionPlusPacket.getRollLeftSpeed()-rollLeftMin)/(rollBand*(rollLeftAmp+rollLeftOffset))>=rollLeftOffsetSize){
						rollValue = rollLeftPred;
					}else{
						rollValue = rollBand * (rollLeftAmp + rollLeftOffset);
						rollLeftPred = rollValue;
					}
				}else{
					rollValue = 0;
					rollLeftPred = 0;
				}
				
				if(yawBand!=0){
					if((motionPlusPacket.getYawLeftSpeed()-yawLeftMin)/(yawBand*(yawLeftAmp+yawLeftOffset))>=yawLeftOffsetSize){
						yawValue = yawLeftPred;
					}else{
						yawValue = yawBand * (yawLeftAmp + yawLeftOffset);
						yawLeftPred = yawValue;
					}
				}else{
					yawValue = 0;
					yawLeftPred = 0;
				}
				
				
				motionPlusPacket.setYawLeftSpeed(yawValue);
				motionPlusPacket.setPitchDownSpeed(pitchValue);
				motionPlusPacket.setRollLeftSpeed(rollValue);
				
				return motionPlusPacket;
			}
		}
		
		
		return input;
	}
	
	/**
	 * Resettet Motion-Plus Werte auf den Ausgangswert
	 */
	private void resetMotionPlusValues(){
		for(int i = 0; i < motionPlusCalibrationData.length; i = i + 2){
			motionPlusCalibrationData[i] = Double.MAX_VALUE;
			motionPlusCalibrationData[i+1] = -Double.MAX_VALUE;
		}
	}
	
	/**
	 * Resettet Max- und Min Werte auf den Ausgangswert der Accelerometer Kalibrierung
	 */
	private void resetAccelValues(){
//		xMax = Double.MIN_VALUE;
//		xMin = Double.MAX_VALUE;
//		yMax = Double.MIN_VALUE;
//		yMin = Double.MAX_VALUE;
//		zMax = Double.MIN_VALUE;
//		zMin = Double.MAX_VALUE;

		
		//Werte initialisieren
		for(double[] calibRun : accelCalibrationData){
			for(int i = 0; i < calibRun.length; i = i + 2){
				calibRun[i] = Double.MAX_VALUE;
				calibRun[i+1] = -Double.MAX_VALUE;
			}
		}
	}

}
