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
package org.italiangrid.utils.https.impl;

import java.util.Arrays;

import javax.net.ssl.SSLContext;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.italiangrid.utils.https.JettySSLConnectorConfigurator;
import org.italiangrid.utils.https.SSLOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link JettySSLConnectorConfigurator} that configures the connector
 * starting from a given {@link SSLContext}.
 *
 * @author andreaceccanti
 *
 */
public class SSLContextConnectorConfigurator implements
  JettySSLConnectorConfigurator {

  /**
   * The slf4j logger
   */
  public static final Logger log = LoggerFactory
    .getLogger(SSLContextConnectorConfigurator.class);

  /**
   * The SSL context that will be used for the configuration
   */
  protected SSLContext sslContext;

  public SSLContextConnectorConfigurator(SSLContext ctxt) {

    sslContext = ctxt;
  }
  

  public void logSSLContextFactoryConfig(SslContextFactory factory) {

    if (log.isDebugEnabled()) {

      log.debug("## SSL Context factory configuration ##");

      log.debug("provider: {}", factory.getProvider());

      log.debug("protocol: {}", factory.getProtocol());

      log.debug("wantClientAuth: {}", factory.getWantClientAuth());

      log.debug("needClientAuth: {}", factory.getNeedClientAuth());

      // Jetty strangely does not initialize includeProtocols and
      // includeCipherSuites, so we can get a null pointer exception
      // here if we do a get on those fields.
      log.debug("excludeProtocols: {}",
        Arrays.toString(factory.getExcludeProtocols()));

      log.debug("excludeCipherSuites: {}",
        Arrays.toString(factory.getExcludeCipherSuites()));

      log.debug("## End of SSL Context factory configuration ##");
    }

  }

  public Connector configureConnector(String host, int port, SSLOptions options) {

    Connector connector;

    if (sslContext == null) {
      log.error("Cannot initialize SSL out of a null SSL context!");
      return null;
    }

    try {

      SslContextFactory factory = new SslContextFactory();

      factory.setSslContext(sslContext);

      if (options.getExcludeProtocols() != null) {

        factory.setExcludeProtocols(options.getExcludeProtocols());
      }

      if (options.getIncludeProtocols() != null) {

        factory.setIncludeProtocols(options.getIncludeProtocols());
      }

      if (options.getIncludeCipherSuites() != null) {

        factory.setIncludeCipherSuites(options.getIncludeCipherSuites());
      }

      if (options.getExcludeCipherSuites() != null) {

        factory.setExcludeCipherSuites(options.getExcludeCipherSuites());
      }

      factory.setWantClientAuth(options.isWantClientAuth());
      factory.setNeedClientAuth(options.isNeedClientAuth());

      logSSLContextFactoryConfig(factory);

      connector = new SslSelectChannelConnector(factory);
      connector.setHost(host);
      connector.setPort(port);

      return connector;

    } catch (Throwable t) {

      log.error("SSL initialization error!", t);
      return null;
    }

  }

  /**
   * Sets the SSL context for this configurator
   *
   * @param sslContext the ssl context
   */
  public void setSslContext(SSLContext sslContext) {

    this.sslContext = sslContext;
  }

  /**
   * Returns the SSL context defined for this configurator
   *
   * @return the SSL context defined for this configurator
   */
  public SSLContext getSslContext() {

    return sslContext;
  }
}
