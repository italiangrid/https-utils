package org.italiangrid.utils.https;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.italiangrid.utils.https.impl.CANLSSLConnectorConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerFactory {

	public static final int MAX_REQUEST_QUEUE_SIZE = 50;
	public static final int MAX_CONNECTIONS = 50;
	
	private static final JettySSLConnectorConfigurator configurator = new CANLSSLConnectorConfigurator();
	
	public static final Logger log = LoggerFactory.getLogger(ServerFactory.class);
	
	private static void configureThreadPool(Server s){
		BlockingQueue<Runnable> requestQueue;

		requestQueue = new ArrayBlockingQueue<Runnable>(
				MAX_REQUEST_QUEUE_SIZE);

		s.setThreadPool(new ExecutorThreadPool(5, MAX_CONNECTIONS, 60,
				TimeUnit.SECONDS, requestQueue));
		
	}
	
	public static Server newServer(String host, int port) {
		
		Server server = new Server();
		
		server.setSendServerVersion(false);
		server.setSendDateHeader(false);
		
		configureThreadPool(server);
		
		Connector connector = configurator.configureConnector(host, port, null);
		
		if (connector == null)
			throw new RuntimeException("Error creating SSL connector.");
		
		server.setConnectors(new Connector[] {connector});
		return server;
		
	}

}
