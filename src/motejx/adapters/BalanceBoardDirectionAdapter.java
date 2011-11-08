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
package motejx.adapters;

import motejx.extensions.balanceboard.BalanceBoardEvent;
import motejx.extensions.balanceboard.BalanceBoardListener;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public abstract class BalanceBoardDirectionAdapter implements BalanceBoardListener {

	/* (non-Javadoc)
	 * @see motejx.extensions.balanceboard.BalanceBoardListener#balanceBoardChanged(motejx.extensions.balanceboard.BalanceBoardEvent)
	 */
	public void balanceBoardChanged(BalanceBoardEvent evt) {
		float left = evt.getTopLeftInterpolated() + evt.getBottomLeftInterpolated();
		float right = evt.getTopRightInterpolated() + evt.getBottomRightInterpolated();
		float top = evt.getTopLeftInterpolated() + evt.getTopRightInterpolated();
		float bottom = evt.getBottomLeftInterpolated() + evt.getBottomRightInterpolated();
		float total = top + bottom;
		if (total != 0) {		
			float x = (right - left) / total;
			float y = (top - bottom) / total;
			directionChanged(x,y);
		}
	}

	public abstract void directionChanged(float x, float y);
	
}
