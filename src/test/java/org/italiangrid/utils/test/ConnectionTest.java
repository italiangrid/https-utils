package org.italiangrid.utils.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;

import org.eclipse.jetty.server.Server;
import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.ServerFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.emi.security.authn.x509.NamespaceCheckingMode;
import eu.emi.security.authn.x509.impl.OpensslCertChainValidator;
import eu.emi.security.authn.x509.impl.PEMCredential;
import eu.emi.security.authn.x509.impl.SocketFactoryCreator;

public class ConnectionTest {

	private static Server server = null;

	private static int port = 15000;

	/**
	 * 
	 * Set up the testing environment. Start the server and let it listen
	 * waiting for test calls.
	 * 
	 * @throws Exception
	 */

	@Before
	public void setUp() throws Exception {

		SSLOptions options = new SSLOptions();
		options.setCertificateFile("certs/voms-service-cert.pem");
		options.setKeyFile("certs/voms-service-key.pem");
		options.setTrustStoreDirectory("certs/ca");
		options.setWantClientAuth(true);

		server = ServerFactory.newServer("localhost", port, options);

		server.setHandler(new HelloHandler());

		server.start();
		
	}

	/**
	 * Connect using HTTP throws an ssl handshake exception.
	 * 
	 * @throws IOException
	 */
	@Test(expected = SSLHandshakeException.class)
	public void unsecureConnection() throws IOException {

		URL url = new URL("https://localhost:" + port);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.connect();
		
	}

	/**
	 * Connect using a certificate released from a trusted CA. 
	 * 
	 */
	@Test
	public void trustedCertificateAgain() throws IOException, KeyStoreException, 
		NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException {

		/*
		 * For localhost testing, disable hostname verification
		 * 
		 */
		HttpsURLConnection.setDefaultHostnameVerifier( 
				
				new javax.net.ssl.HostnameVerifier(){
					 
			        public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
			            
			        	if (hostname.equals("localhost")) {
			            
			        		return true;
			            }
			            
			        	return false;
			        }
			    });
		
		
		PEMCredential credential = new PEMCredential("certs/voms-client-key.pem", 
				"certs/voms-client-cert.pem", "pass".toCharArray());
		
		OpensslCertChainValidator validator = new OpensslCertChainValidator("certs/ca",
				NamespaceCheckingMode.EUGRIDPMA_AND_GLOBUS, 60000L);
		
		SSLContext context = SocketFactoryCreator.getSSLContext(credential, validator, null);
		
		URL url = new URL("https://localhost:" + port);

		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

		connection.setSSLSocketFactory(context.getSocketFactory());
		
		connection.connect();

		Assert.assertEquals(200, connection.getResponseCode());

		connection.disconnect();
		
	}
	
	/**
	 * Tear down the testing environment. Stop the server.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {

		server.stop();
		
	}

}
