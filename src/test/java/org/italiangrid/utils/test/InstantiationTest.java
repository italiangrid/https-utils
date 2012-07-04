package org.italiangrid.utils.test;

import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.ServerFactory;
import org.junit.Test;

/**
 * Tests the server factory newserver methods
 * 
 * @author valerioventuri
 * 
 */
public class InstantiationTest {

	/**
	 * Calling newServer with wrong certificate location. 
	 *  
	 */
	@Test(expected=RuntimeException.class)
	public void wrongCertificateLocation() {
		
		SSLOptions options = new SSLOptions();
		
		options.setCertificateFile("certs/voms-service-cert.pe");
		options.setKeyFile("certs/voms-service-key.pem");
		options.setTrustStoreDirectory("certs/ca");
		
		ServerFactory.newServer("localhost", 443, options);

	}

	
	/**
	 * Calling newServer with wrong key location. 
	 * 
	 */
	@Test(expected=RuntimeException.class)
	public void wrongKeyLocation() {
		
		SSLOptions options = new SSLOptions();
		
		options.setCertificateFile("certs/voms-service-cert.pem");
		options.setKeyFile("certs/voms-service-key.pe");
		options.setTrustStoreDirectory("certs/ca");
		
		ServerFactory.newServer("localhost", 443, options);
		
	}

	/**
	 * Calling newServer with certificate location pointing to a non certificate file. 
	 * 
	 */
	@Test(expected=RuntimeException.class)
	public void wrongCertificate() {
		
		SSLOptions options = new SSLOptions();
		
		options.setCertificateFile("certs/README");
		options.setKeyFile("certs/voms-service-key.pem");
		options.setTrustStoreDirectory("certs/ca");
		
		ServerFactory.newServer("localhost", 443, options);
		
	}
	
	
}
