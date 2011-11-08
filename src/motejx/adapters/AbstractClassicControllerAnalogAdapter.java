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

import motejx.extensions.classic.ClassicControllerAnalogListener;
import motejx.extensions.classic.LeftAnalogStickEvent;
import motejx.extensions.classic.LeftAnalogTriggerEvent;
import motejx.extensions.classic.RightAnalogStickEvent;
import motejx.extensions.classic.RightAnalogTriggerEvent;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public abstract class AbstractClassicControllerAnalogAdapter implements
		ClassicControllerAnalogListener {

	/* (non-Javadoc)
	 * @see motejx.extensions.classic.ClassicControllerAnalogListener#leftAnalogStickChanged(motejx.extensions.classic.LeftAnalogStickEvent)
	 */
	public void leftAnalogStickChanged(LeftAnalogStickEvent evt) {
	}

	/* (non-Javadoc)
	 * @see motejx.extensions.classic.ClassicControllerAnalogListener#leftAnalogTriggerChanged(motejx.extensions.classic.LeftAnalogTriggerEvent)
	 */
	public void leftAnalogTriggerChanged(LeftAnalogTriggerEvent evt) {
	}

	/* (non-Javadoc)
	 * @see motejx.extensions.classic.ClassicControllerAnalogListener#rightAnalogStickChanged(motejx.extensions.classic.RightAnalogStickEvent)
	 */
	public void rightAnalogStickChanged(RightAnalogStickEvent evt) {
	}

	/* (non-Javadoc)
	 * @see motejx.extensions.classic.ClassicControllerAnalogListener#rightAnalogTriggerChanged(motejx.extensions.classic.RightAnalogTriggerEvent)
	 */
	public void rightAnalogTriggerChanged(RightAnalogTriggerEvent evt) {
	}

}
