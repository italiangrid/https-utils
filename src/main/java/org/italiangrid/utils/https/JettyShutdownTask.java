/*
 * Copyright (c) Members of the EGEE Collaboration. 2006-2010.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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

/*
 * This code was borrowed from the Argus pdp-pep-common module:
 * See https://github.com/argus-authz/argus-pdp-pep-common
 */
package org.italiangrid.utils.https;


import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * A command that shuts down a Jetty {@link Server} if it's currently running, and calls System.exit() right after.
 * 
 */
public class JettyShutdownTask implements Runnable {

	public static final Logger log = LoggerFactory
			.getLogger(JettyShutdownTask.class);

	private Server httpServer;

	/**
	 * Constructor.
	 * 
	 * @param s
	 *            the server to be shut down
	 */
	public JettyShutdownTask(Server s) {
		httpServer = s;
	}

	public void run() {

		if (httpServer.isRunning()) {
			try {
				httpServer.stop();
				if (httpServer.isStopped()) {
					log.info("Server stopped.");
					System.exit(0);
				}
			} catch (Exception e) {
				log.error("Unable to shutdown HTTP server", e);
				System.exit(1);
			}
		}
	}
}
