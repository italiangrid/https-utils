package org.italiangrid.utils.examples;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.italiangrid.utils.https.JettyAdminService;
import org.italiangrid.utils.https.JettyRunThread;
import org.italiangrid.utils.https.JettyShutdownTask;
import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.ServerFactory;
import org.italiangrid.utils.voms.VOMSSecurityContextHandler;

/** 
 * A simple test server that demonstrates how to create an SSL-enabled server
 * and a related shutdown service.
 *
 */
public class TestServer {

	
	TestServer(String hostname, 
			int port, 
			String serverCertFile, 
			String serverKeyFile,
			String trustStoreDir) throws Exception {
		
		
		SSLOptions options = new SSLOptions();
		options.setCertificateFile(serverCertFile);
		options.setKeyFile(serverKeyFile);
		options.setTrustStoreDirectory(trustStoreDir);
		
		Server s = ServerFactory.newServer(hostname, port, options);
		HandlerCollection handlers = new HandlerCollection();
		handlers.setHandlers(new Handler[]{ new VOMSSecurityContextHandler(), 
				new PrintAuthenticationInformationHandler()});
		
		s.setHandler(handlers);
		
		JettyRunThread rt = new JettyRunThread(s);
		rt.start();
		
		JettyAdminService shutdownService = new JettyAdminService("localhost", port+1, "admin");
		shutdownService.registerShutdownTask(new JettyShutdownTask(s));
		
		shutdownService.start();
		
	}
	
	public static void main(String[] args) throws Exception {
		
		if (args.length < 5){
			
			System.err.println("Please provide hostname,port,serverCert,serverKey,trustDir as arguments.");
		}
		
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		new TestServer(hostname, port, args[2], args[3], args[4]);
		
	}
}