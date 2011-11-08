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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class ExtensionProvider {
	
	private static Map<String, Class<? extends Extension>> lookup;
	
	private Logger log = LoggerFactory.getLogger(ExtensionProvider.class);
	
	@SuppressWarnings("unchecked")
	public ExtensionProvider() {
		synchronized(ExtensionProvider.class) {
			if (lookup == null) {
				log.debug("Initializing lookup.");
				lookup = new HashMap<String, Class<? extends Extension>>();
				InputStream in = ExtensionProvider.class.getResourceAsStream("extensions.properties");
				Properties props = new Properties();
				
				if (in == null) {
					log.info("no extensions.properties found. as a result, no extensions will be available.");
					return;
				}
				
				try {
					props.load(in); 
					for (Object o : props.keySet()) {
						String key = (String) o;
						String value = props.getProperty(key);
						
						if (log.isDebugEnabled()) {
							log.debug("Adding extension (" + key + " / " + value + ").");
						}
						
						Class<? extends Extension> clazz = (Class<? extends Extension>) Class.forName(value);
						lookup.put(key, clazz);
					}
				} catch (IOException ex) {
					log.warn(ex.getMessage(), ex);
				} catch (ClassNotFoundException ex) {
					log.warn(ex.getMessage(), ex);
				}
			}
		}
		log.debug("Lookup initialized.");
	}
	
	public Extension getExtension(byte[] id) {
		String id0 = Integer.toHexString(id[0] & 0xff);
		if (id0.length() == 1) {
			id0 = "0" + id0;
		}
		String id1 = Integer.toHexString(id[1] & 0xff);
		if (id1.length() == 1) {
			id1 = "0" + id1;
		}
		String key = id0 + id1;
		Class<? extends Extension> clazz = lookup.get(key);
		
		if (clazz == null) {
			log.warn("No matching extension found for key: " + key);
			return null;
		}
		
		Extension extension = null;
		try {
			extension = clazz.newInstance();
		} catch (InstantiationException ex) {
			log.error(ex.getMessage(), ex);
		} catch (IllegalAccessException ex) {
			log.error(ex.getMessage(), ex);
		}
		return extension;
	}
}
