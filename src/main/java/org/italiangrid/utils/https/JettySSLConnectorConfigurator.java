/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare, 2006-2014.
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
package org.italiangrid.utils.https;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

/**
 * A {@link JettySSLConnectorConfigurator} is responsible for configuring an SSL
 * connector according to a specific set of configuration parameters.
 *
 */
public interface JettySSLConnectorConfigurator {

  /**
   * Configure an SSL connector
   * 
   * @param server
   *          the jetty server
   * @param host
   *          the host the connector will bind to
   * @param port
   *          the port the connector will bind to
   * @param options
   *          the SSL configuration options
   * 
   * @return a {@link ServerConnector} configured as requested
   * 
   */
  public ServerConnector configureSSLConnector(Server server, String host,
    int port, SSLOptions options);
}
