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

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.italiangrid.utils.https.impl.canl.CANLSSLConnectorConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.X509CertChainValidatorExt;

/**
 * A factory that creates Jetty server object with an HTTPS connector configured
 * according to the given options.
 * 
 * @author andreaceccanti
 *
 */
public class ServerFactory {

  /**
   * Maximum request queue size default value.
   */
  public static final int MAX_REQUEST_QUEUE_SIZE = 50;

  /**
   * Default value for maximum number of concurrent connections.
   */
  public static final int MAX_CONNECTIONS = 50;

  /**
   * The slf4j logger.
   */
  public static final Logger log = LoggerFactory.getLogger(ServerFactory.class);

  /**
   * The configurator used to configure the SSL connector.
   */
  private static final JettySSLConnectorConfigurator configurator = new CANLSSLConnectorConfigurator();

  /**
   * Thread pool server configuration
   * 
   * @param s
   */
  private static void configureThreadPool(Server s) {

    configureThreadPool(s, MAX_CONNECTIONS, MAX_REQUEST_QUEUE_SIZE);
  }

  private static void configureThreadPool(Server s, int maxRequestSize,
    int maxConnections) {

    if (maxRequestSize <= 0)
      maxRequestSize = MAX_REQUEST_QUEUE_SIZE;

    if (maxConnections <= 0)
      maxConnections = MAX_CONNECTIONS;

    QueuedThreadPool tp = new QueuedThreadPool();

    tp.setMinThreads(5);
    tp.setMaxThreads(maxConnections);
    tp.setMaxQueued(maxRequestSize);
    tp.setMaxIdleTimeMs((int) TimeUnit.SECONDS.toMillis(60));

    s.setThreadPool(tp);
  }

  /**
   * Returns a new Jetty server configured to listen on the host:port passed as
   * argument using the SSL default options (see {@link SSLOptions}).
   * 
   * @param host the host
   * @param port the port
   * @return a {@link Server} configured as requested
   */
  public static Server newServer(String host, int port) {

    return newServer(host, port, SSLOptions.DEFAULT_OPTIONS);

  }

  /**
   * 
   * Returns a new Jetty server configured to listen on the host:port passed as
   * argument and according to the SSL configuration options provided.
   * 
   * @param host the host 
   * @param port the port
   * @param options the ssl options
   * @return a {@link Server} configured as requested
   */
  public static Server newServer(String host, int port, SSLOptions options) {

    Server server = new Server();

    server.setSendServerVersion(false);
    server.setSendDateHeader(false);

    configureThreadPool(server);

    Connector connector = configurator.configureConnector(host, port, options);

    if (connector == null)
      throw new RuntimeException("Error creating SSL connector.");

    server.setConnectors(new Connector[] { connector });
    return server;
  }

  /**
   * 
   * Returns a new Jetty server configured to listen on the host:port passed as
   * argument and according to the SSL configuration options provided.
   * 
   * @param host the host 
   * @param port the port
   * @param options the ssl options
   * @param validator the CANL validator
   * @param maxConnections the maximun number of served connections
   * @param maxRequestQueueSize the request backlog size
   * @return a {@link Server} configured as requested
   */
  public static Server newServer(String host, int port, SSLOptions options,
    X509CertChainValidatorExt validator, int maxConnections,
    int maxRequestQueueSize) {

    Server server = new Server();

    server.setSendServerVersion(false);
    server.setSendDateHeader(false);

    configureThreadPool(server, maxConnections, maxRequestQueueSize);

    CANLSSLConnectorConfigurator configurator = new CANLSSLConnectorConfigurator(
      validator);

    Connector connector = configurator.configureConnector(host, port, options);

    if (connector == null)
      throw new RuntimeException("Error creating SSL connector.");

    server.setConnectors(new Connector[] { connector });
    return server;
  }
}
