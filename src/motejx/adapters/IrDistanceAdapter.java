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
package motejx.adapters;

import java.awt.geom.Point2D;

import motej.IrPoint;
import motej.event.IrCameraEvent;
import motej.event.IrCameraListener;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public abstract class IrDistanceAdapter implements IrCameraListener {

	private double radiansPerPixel = Math.toRadians(45) / Point2D.distance(1024d, 768d, 0d, 0d);
	
	private double distanceBetweenIrLightSourcesInMillimeter = 150d;
	
	public IrDistanceAdapter(double distanceBetweenIrLightSourcesInMillimeter) {
		this.distanceBetweenIrLightSourcesInMillimeter = distanceBetweenIrLightSourcesInMillimeter;
	}
	
	public void irImageChanged(IrCameraEvent evt) {
		IrPoint p0 = null;
		IrPoint p1 = null;
		
		if (evt.getIrPoint(0).x != 1023) {
			p0 = evt.getIrPoint(0);
		}
		
		if (evt.getIrPoint(1).x != 1023) {
			if (p0 == null)
				p0 = evt.getIrPoint(1);
			else
				p1 = evt.getIrPoint(1);
		}
		
		if (evt.getIrPoint(2).x != 1023) {
			if (p0 == null)
				p0 = evt.getIrPoint(2);
			else if (p1 == null)
				p1 = evt.getIrPoint(2);
		}
		
		if (evt.getIrPoint(3).x != 1023) {
			if (p0 == null)
				p0 = evt.getIrPoint(3);
			else if (p1 == null)
				p1 = evt.getIrPoint(3);
			
		}

		if (p0 == null || p1 == null)
			return;
		
		double avgX = (p0.x + p1.x) / 2;
		double avgY = (p0.y + p1.y) / 2;
		double distanceBetweenIrLights = p0.distance(p1);
		
		// distance calculation
		double radiansBetweenIrDots = radiansPerPixel * distanceBetweenIrLights;
		double z = (distanceBetweenIrLightSourcesInMillimeter / 2d) / Math.tan(radiansBetweenIrDots / 2d);// / screenWidth;

		// calculation of x and y position (relative to 512, 384)
		double x = Math.sin(radiansPerPixel * (avgX - 512)) * z;
		double y = Math.sin(radiansPerPixel * (avgY - 384)) * z;
		
		positionChanged(x,y,z);
	}
	
	public abstract void positionChanged(final double x, final double y, final double z);

}
