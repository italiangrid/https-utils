package org.italiangrid.utils.test;

import org.eclipse.jetty.server.Server;
import org.italiangrid.utils.https.ServerFactory;

public class TestServer {

	
	TestServer() throws Exception{
		
		Server s = ServerFactory.newServer("wilco.cnaf.infn.it", 15000);
		s.start();
			
		
		
	}
	
	public static void main(String[] args) throws Exception {
		new TestServer();
	}
}
