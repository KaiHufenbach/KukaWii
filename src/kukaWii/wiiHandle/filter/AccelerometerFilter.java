package kukaWii.wiiHandle.filter;

import kukaWii.wiiHandle.packet.AbstractPacket;
import kukaWii.wiiHandle.packet.AccelerometerPacket;

/**
 * Test-Implementation eines Filters, um Rauschen zu verringern, sowie die Erdbeschleunigung zu herauszufiltern
 * @author InternetMini
 *
 */
public class AccelerometerFilter extends AbstractPacketFilter{

	private static final int CALIBRATION_BEGIN = 100;
	private static final int CALIBRATION_END = 1000;
	
	private int packet = 0;
	
	private boolean calibration = false;
	
	private double xMax = Double.MIN_VALUE;
	private double xMin = Double.MAX_VALUE;
	private double xAmp;
	private double xPred = 0;
	private double xOffset;
	private double xRelOffsetSize;
	
	private double yMax = Double.MIN_VALUE;
	private double yMin = Double.MAX_VALUE;;
	private double yAmp;
	private double yPred = 0;
	private double yOffset;
	private double yRelOffsetSize;
	
	private double zMin = Double.MAX_VALUE;
	private double zMax= Double.MIN_VALUE;
	private double zAmp;
	private double zPred = 0;
	private double zOffset;
	private double zRelOffsetSize;
	
	
	@Override
	public AbstractPacket compute(AbstractPacket input) {
		
		if(input instanceof AccelerometerPacket){
			AccelerometerPacket accelPacket = (AccelerometerPacket)input;
			
			if(packet < CALIBRATION_END){
				packet++;
				if(packet == CALIBRATION_BEGIN){
					System.out.println("Begin Calibration");
					System.out.println("Please do not move Wii!");
					calibration = true;
				} else if (packet == CALIBRATION_END){
					System.out.println("End Calibration: Now forwarding Packets...");
					
					xAmp = xMax - xMin;
					yAmp = yMax - yMin;
					zAmp = zMax - zMin;
					
					xOffset = xAmp/2.0;
					yOffset = yAmp/2.0;
					zOffset = zAmp/2.0;
					
					xRelOffsetSize = (xAmp+xOffset)/xAmp;
					yRelOffsetSize = (yAmp+yOffset)/yAmp;
					zRelOffsetSize = (zAmp+zOffset)/zAmp;
					
					System.out.println("XMax: "+xMax+" XMin: "+xMin+" xAmp: "+xAmp);
					System.out.println("YMax: "+yMax+" YMin: "+yMin+" yAmp: "+yAmp);
					System.out.println("ZMax: "+zMax+" ZMin: "+zMin+" zAmp: "+zAmp);
					calibration = false;
				}
				
				if(calibration){
					if(accelPacket.getX() < xMin){
						xMin = accelPacket.getX();
					}else if(accelPacket.getX() > xMax){
						xMax = accelPacket.getX();
					}
					
					if(accelPacket.getY() < yMin){
						yMin = accelPacket.getY();
					}else if(accelPacket.getY()> yMax){
						yMax = accelPacket.getY();
					}
					
					if(accelPacket.getZ() < zMin){
						zMin = accelPacket.getZ();
					}else if(accelPacket.getZ()> zMax){
						zMax = accelPacket.getZ();
					}
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
		
		
		return input;
	}
	

}
