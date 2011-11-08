/*
 * Copyright 2009 Jan Loesbrock
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

package motej.demos.motionplus;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;


public class GraphPanel extends Canvas{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Vector<Double> yawList;
	Vector<Double> rollList;
	Vector<Double> pitchList;
	
	private int rangeMax;
	private int rangeMin;

	public GraphPanel() {
		this.setSize(400,200); // 1. width , 2. height
		
		yawList = new Vector<Double>(this.getWidth());
		rollList = new Vector<Double>(this.getWidth());
		pitchList = new Vector<Double>(this.getWidth());
				
		// The range between the values will be drawed
		this.rangeMin = -500;
		this.rangeMax = 500;
		
		this.setBackground(Color.WHITE);
		
	}

	public synchronized void addValue(double yaw, double roll,  double pitch) {

		yawList.add(yaw);
		if (yawList.size() > this.getWidth()) {
			yawList.clear();
		}
		rollList.add(roll);
		if (rollList.size() > this.getWidth()) {
			rollList.clear();
		}
		pitchList.add(pitch);
		if (pitchList.size() > this.getWidth()) {
			pitchList.clear();
		}
		this.repaint();
	}

	public void setDimension(Dimension dim)
	{
		this.setSize(dim);
	}
	
	
	public void repaint() {
		this.paint(this.getGraphics());
	}

	@Override
	public void paint(Graphics g) {
		double range = Math.abs(rangeMin) + Math.abs(rangeMax);
		double scale = ((double)this.getHeight()) / range;
		int n = 1;
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		double value;
		double last_value = 0;
		double height = (double) this.getHeight();
		
		g.setColor(Color.RED);
		for (int x = 0; x < this.getWidth()-1 && x < yawList.size(); x += n) {
			value = ((double)yawList.get(x)) * scale;
			g.drawLine(x, (int) (last_value + (height/2.0)), x+1, (int) (value + (height/2.0)));
			last_value = value;
		}
		
		g.setColor(Color.GREEN);
		for (int x = 0; x < this.getWidth()-1 && x < rollList.size(); x += n) {
			value = rollList.get(x);
			g.drawLine(x, (int) (last_value + (height/2.0)), x+1, (int) (value + (height/2.0)));
			last_value = value;
		}
		
		g.setColor(Color.BLUE);
		for (int x = 0; x < this.getWidth()-1 && x < pitchList.size(); x +=n ) {
			value = pitchList.get(x);
			g.drawLine(x, (int) (last_value + (height/2.0)), x+1, (int) (value + (height/2.0)));
			last_value = value;
		}
	}

}
