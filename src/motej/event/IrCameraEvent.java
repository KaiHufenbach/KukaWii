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
package motej.event;

import motej.IrCameraMode;
import motej.IrPoint;
import motej.Mote;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class IrCameraEvent {

	private Mote source;
	
	private IrCameraMode mode;

	private IrPoint[] points;
	
	public IrCameraEvent(Mote source, IrCameraMode mode, IrPoint p0, IrPoint p1, IrPoint p2, IrPoint p3) {
		this.source = source;
		this.mode = mode;
		
		points = new IrPoint[4];
		points[0] = p0;
		points[1] = p1;
		points[2] = p2;
		points[3] = p3;
	}

	public Mote getSource() {
		return source;
	}

	public IrCameraMode getMode() {
		return mode;
	}

	public IrPoint getIrPoint(int slot) {
		return points[slot];
	}
}
