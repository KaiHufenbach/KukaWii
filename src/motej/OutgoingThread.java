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
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.bluetooth.L2CAPConnection;
import javax.microedition.io.Connector;

import motej.request.MoteRequest;
import motej.request.PlayerLedRequest;
import motej.request.RumbleRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
class OutgoingThread extends Thread {

		private static final long THREAD_SLEEP = 10l;
		
		private Logger log = LoggerFactory.getLogger(OutgoingThread.class);

		private volatile boolean active;

		private L2CAPConnection outgoing;

		private volatile ConcurrentLinkedQueue<MoteRequest> requestQueue;
		
		private byte ledByte;
		
		private long rumbleMillis = Long.MIN_VALUE;

		private Mote source;

		protected OutgoingThread(Mote source, String btaddress) throws IOException, InterruptedException {
			super("out:" + btaddress);

			this.source = source;

			String l2cap = "btl2cap://"
				+ btaddress
				+ ":11;authenticate=false;encrypt=false;master=false";
			
			if (log.isDebugEnabled()) {
				log.debug("Opening outgoing connection to " + l2cap);
			}
			
			outgoing = (L2CAPConnection) Connector.open(l2cap, Connector.WRITE, true);

			if (log.isDebugEnabled()) {
				log.debug("Outgoing connection is " + outgoing.toString());
			}
			
			requestQueue = new ConcurrentLinkedQueue<MoteRequest>();
			Thread.sleep(THREAD_SLEEP);
			active = true;
		}

		public void disconnect() {
			active = false;
		}

		public void run() {
			while (active || !requestQueue.isEmpty()) {
				try {
					if (rumbleMillis > 0) {
						rumbleMillis -= THREAD_SLEEP;
					}
					if (rumbleMillis == 0) {
						rumbleMillis = Long.MIN_VALUE;
						outgoing.send(RumbleRequest.getStopRumbleBytes(ledByte));
						Thread.sleep(THREAD_SLEEP);
						continue;
					}
					if (!requestQueue.isEmpty()) {
						MoteRequest request = requestQueue.poll();
						if (request instanceof PlayerLedRequest) {
							ledByte = ((PlayerLedRequest) request).getLedByte();
						}
						if (request instanceof RumbleRequest) {
							((RumbleRequest)request).setLedByte(ledByte);
							rumbleMillis = ((RumbleRequest) request).getMillis();
						}
						
						if (log.isTraceEnabled()) {
							byte[] buf = request.getBytes();
							StringBuffer sb = new StringBuffer();
							log.trace("sending:");
							for (int i = 0; i < buf.length; i++) {
								String hex = Integer.toHexString(buf[i] & 0xff);
								sb.append(hex.length() == 1 ? "0x0" : "0x").append(hex).append(" ");
								if ((i + 1) % 8 == 0) {
									log.trace(sb.toString());
									sb.delete(0, sb.length());
								}
							}
							if (sb.length() > 0) {
								log.trace(sb.toString());
							}
						}
						
						outgoing.send(request.getBytes());
					}
					Thread.sleep(THREAD_SLEEP);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					log.error("connection closed?", ex);
					active = false;
					source.fireMoteDisconnectedEvent();
				}
			}
			try {
				outgoing.close();
			} catch (IOException ex) {
				log.error(ex.getMessage(), ex);
			}
		}

		public void sendRequest(MoteRequest request) {
			requestQueue.add(request);
		}

	}