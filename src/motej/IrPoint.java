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

import java.awt.Point;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class IrPoint extends Point {

	private static final long serialVersionUID = -7473956039643032186L;

	public int size;
	
	public Point max;
	
	public Point min;
	
	public int intensity;

	public IrPoint() {
		this(0, 0);
	}

	public IrPoint(int x, int y) {
		this(x, y, 1);
	}

	public IrPoint(int x, int y, int size) {
		this(x, y, size, x, y, x, y, 1);
	}
	
	public IrPoint(int x, int y, int size, int xmin, int ymin, int xmax, int ymax, int intensity) {
		this(x, y, size, new Point(xmin, ymin), new Point(xmax, ymax), intensity);
	}
	
	public IrPoint(int x, int y, int size, Point min, Point max, int intensity) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.max = max;
		this.min = min;
		this.intensity = intensity;
	}

	public IrPoint(IrPoint p) {
		this.x = p.x;
		this.y = p.y;
		this.size = p.size;
		this.max = p.max;
		this.min = p.min;
		this.intensity = p.intensity;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof IrPoint) {
			IrPoint p = (IrPoint) obj;
			return (x == p.x) && (y == p.y) && (size == p.size) && 
				(max.x == p.max.x) && (max.y == p.max.y) &&
				(min.x == p.min.x) && (min.y == p.min.y) &&
				(intensity == p.intensity);
		}
		return super.equals(obj);
	}

	public int getSize() {
		return size;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + x;
		hash = hash * 31 + y;
		hash = hash * 31 + size;
		hash = hash * 31 + max.x;
		hash = hash * 31 + max.y;
		hash = hash * 31 + min.x;
		hash = hash * 31 + min.y;
		hash = hash * 31 + intensity;
		return hash;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Point getMax() {
		return max;
	}

	public void setMax(Point max) {
		this.max = max;
	}

	public Point getMin() {
		return min;
	}

	public void setMin(Point min) {
		this.min = min;
	}

	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

}
