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

import motej.Extension;
import motej.Mote;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class ExtensionEvent {

	private Mote source;
	
	private Extension extension;
	
	public ExtensionEvent(Mote source) {
		this(source, null);
	}
	
	public ExtensionEvent(Mote source, Extension extension) {
		this.source = source;
		this.extension = extension;
	}

	public Mote getSource() {
		return source;
	}

	public Extension getExtension() {
		return extension;
	}
	
	public boolean isExtensionConnected() {
		return extension != null;
	}
}
