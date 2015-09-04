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

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.italiangrid.utils.https.impl.SSLContextConnectorConfigurator;
import org.italiangrid.utils.https.impl.canl.CANLListener;
import org.italiangrid.voms.util.CertificateValidatorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.CrlCheckingMode;
import eu.emi.security.authn.x509.OCSPCheckingMode;
import eu.emi.security.authn.x509.X509CertChainValidatorExt;
import eu.emi.security.authn.x509.impl.PEMCredential;
import eu.emi.security.authn.x509.impl.SocketFactoryCreator;

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
  public static final int MAX_REQUEST_QUEUE_SIZE = 200;

  /**
   * Default value for maximum number of concurrent connections.
   */
  public static final int MAX_CONNECTIONS = 50;

  /**
   * The slf4j logger.
   */
  public static final Logger log = LoggerFactory.getLogger(ServerFactory.class);

  /**
   * Returns a new Jetty server configured to listen on the host:port passed as
   * argument using the SSL default options (see {@link SSLOptions}).
   * 
   * @param host
   *          the host
   * @param port
   *          the port
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
   * @param host
   *          the host
   * @param port
   *          the port
   * @param options
   *          the ssl options
   * @return a {@link Server} configured as requested
   */
  public static Server newServer(String host, int port, SSLOptions options) {

    return newServer(host, port, options, null, MAX_CONNECTIONS,
      MAX_REQUEST_QUEUE_SIZE);
  }

  /**
   * 
   * Returns a new Jetty server configured to listen on the host:port passed as
   * argument and according to the SSL configuration options provided.
   * 
   * @param host
   *          the host
   * @param port
   *          the port
   * @param options
   *          the ssl options
   * @param validator
   *          the CANL validator
   * @param maxConnections
   *          the maximun number of served connections
   * @param maxRequestQueueSize
   *          the request backlog size
   * @return a {@link Server} configured as requested
   */
  public static Server newServer(String host, int port, SSLOptions options,
    X509CertChainValidatorExt validator, int maxConnections,
    int maxRequestQueueSize) {

    if (maxRequestQueueSize <= 0) {
      maxRequestQueueSize = MAX_REQUEST_QUEUE_SIZE;
    }

    if (maxConnections <= 0) {
      maxConnections = MAX_CONNECTIONS;
    }

    BlockingQueue<Runnable> requestQueue = new ArrayBlockingQueue<Runnable>(
      maxRequestQueueSize);

    QueuedThreadPool tp = new QueuedThreadPool(maxConnections, 5,
      (int) TimeUnit.MINUTES.toMillis(10), requestQueue);

    Server server = new Server(tp);

    SSLContextConnectorConfigurator configurator;

    try {
      configurator = new SSLContextConnectorConfigurator(configureSSLContext(
        validator, options));
    } catch (Throwable e) {
      log.error("Error configuring SSL connector!", e);
      return null;
    }

    ServerConnector connector = configurator.configureSSLConnector(server,
      host, port, options);

    if (connector == null)
      throw new RuntimeException("Error creating SSL connector.");

    return server;
  }

  private static SSLContext configureSSLContext(
    X509CertChainValidatorExt validator, SSLOptions options)
    throws KeyStoreException, CertificateException, IOException {

    PEMCredential serviceCredentials = new PEMCredential(options.getKeyFile(),
      options.getCertificateFile(), options.getKeyPassword());

    if (validator == null) {
      validator = configureDefaultValidator(options);
    }

    SSLContext sslContext = SocketFactoryCreator.getSSLContext(
      serviceCredentials, validator, null);

    return sslContext;
  }

  private static X509CertChainValidatorExt configureDefaultValidator(
    SSLOptions options) {

    CANLListener l = new CANLListener();

    X509CertChainValidatorExt certChainValidator = new CertificateValidatorBuilder()
      .crlChecks(CrlCheckingMode.IF_VALID).ocspChecks(OCSPCheckingMode.IGNORE)
      .lazyAnchorsLoading(false)
      .trustAnchorsDir(options.getTrustStoreDirectory())
      .trustAnchorsUpdateInterval(options.getTrustStoreRefreshIntervalInMsec())
      .storeUpdateListener(l).validationErrorListener(l).build();

    return certChainValidator;
  }
}
