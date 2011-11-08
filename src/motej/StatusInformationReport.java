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

/**
 * Status information as reported by the wiimote. Contains the currently lid
 * LEDs, status of speaker, the battery level, if an extension is connected and
 * if continuous reporting is enabled.
 * <p>
 * 
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class StatusInformationReport {

	private boolean extensionControllerConnected;

	private boolean speakerEnabled;

	private boolean continuousReportingEnabled;

	private boolean[] ledEnabled;

	// fully charged might be 0xc8 (wiili.org)
	private byte batteryLevel;

	public StatusInformationReport(boolean[] ledEnabled,
			boolean speakerEnabled, boolean continuousReportingEnabled,
			boolean extensionControllerConnected, byte batteryLevel) {
		this.ledEnabled = ledEnabled.clone();
		this.speakerEnabled = speakerEnabled;
		this.continuousReportingEnabled = continuousReportingEnabled;
		this.extensionControllerConnected = extensionControllerConnected;
		this.batteryLevel = batteryLevel;
	}

	/**
	 * The current battery level. Fully charged might be 0xc8 (see <a
	 * href="http://www.wiili.org/index.php/Wiimote#Batteries">www.wiili.org</a>).
	 * 
	 * @return the battery level
	 */
	public byte getBatteryLevel() {
		return batteryLevel;
	}

	/**
	 * The currently lid LEDs
	 * 
	 * @return enabled LEDs
	 */
	public boolean[] getLedEnabled() {
		return ledEnabled;
	}

	/**
	 * True, if continuous reporting is enabled.
	 * 
	 * @return continuous reporting enabled
	 */
	public boolean isContinuousReportingEnabled() {
		return continuousReportingEnabled;
	}

	/**
	 * True, if an extension controller is connected.
	 * 
	 * @return extension controller connected
	 */
	public boolean isExtensionControllerConnected() {
		return extensionControllerConnected;
	}

	/**
	 * True, if the speaker is enabled.
	 * 
	 * @return speaker enabled
	 */
	public boolean isSpeakerEnabled() {
		return speakerEnabled;
	}

	@Override
	public String toString() {
		return "StatusInformation[BatteryLevel: " + batteryLevel
				+ ", ExtensionControllerConnected: "
				+ extensionControllerConnected + ", SpeakerEnabled: "
				+ speakerEnabled + ", ContinuousReportingEnabled: "
				+ continuousReportingEnabled + ", LedEnabled: {"
				+ ledEnabled[0] + ", " + ledEnabled[1] + ", " + ledEnabled[2]
				+ ", " + ledEnabled[3] + "}]";
	}
}
