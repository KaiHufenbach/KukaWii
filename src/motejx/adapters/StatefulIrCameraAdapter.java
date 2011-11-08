/*
 * Copyright 2007-2008 motej
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

import motej.IrPoint;
import motej.event.IrCameraEvent;
import motej.event.IrCameraListener;

/**
 * StatefullIrCameraAdapter implements IrCameraListener to give additional
 * information about infrared points in the WiiMotes camera sight.
 *
 * @author djuxen
 */
public abstract class StatefulIrCameraAdapter implements IrCameraListener {

	private IrCameraEvent lastEvent;

	public void irImageChanged(IrCameraEvent evt) {
		if (lastEvent != null) {
			for (int i = 0; i < 4; i++) {
				if (!evt.getIrPoint(i).equals(lastEvent.getIrPoint(i))) {
					IrPoint currentPoint = evt.getIrPoint(i);
					IrPoint lastPoint = lastEvent.getIrPoint(i);

					if (isValid(currentPoint) && !isValid(lastPoint)) {
						pointAppeared(i, currentPoint);
					} else if (!isValid(currentPoint) && isValid(lastPoint)) {
						pointDisappeared(i, lastPoint);
					} else {
						pointChanged(i, currentPoint);
					}
				}
			}
		}

		lastEvent = evt;
	}

	/**
	 * Implement this method to get notified when an infrared point
	 * appears in the camera's view.
	 * @param slot The slot on which the point appeared
	 * @param p The point which appeared
	 */
	public abstract void pointAppeared(int slot, IrPoint p);

	/**
	 * Implement this method to get notified when an infrared point
	 * moves in the camera's view.
	 * @param slot The slot on which the point moved
	 * @param p The point which changed
	 */
	public abstract void pointChanged(int slot, IrPoint p);

	/**
	 * Implement this method to get notified when an infrared point
	 * disappears from the camera's view.
	 * @param slot The slot on which the point disappeared
	 * @param p The point which disappeared
	 */
	public abstract void pointDisappeared(int slot, IrPoint p);

	private boolean isValid(IrPoint irPoint) {
		return irPoint.x != 1023 || irPoint.y != 1023;
	}
}
