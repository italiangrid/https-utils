package org.italiangrid.utils.https;

import java.util.Properties;

import org.eclipse.jetty.server.Connector;

public interface JettySSLConnectorConfigurator {

	public Connector configureConnector(String host, int port, Properties options);
}
