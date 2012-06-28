package org.italiangrid.utils.https.impl;

import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.italiangrid.utils.https.JettySSLConnectorConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.NamespaceCheckingMode;
import eu.emi.security.authn.x509.impl.OpensslCertChainValidator;
import eu.emi.security.authn.x509.impl.PEMCredential;
import eu.emi.security.authn.x509.impl.SocketFactoryCreator;

public class CANLSSLConnectorConfigurator implements
		JettySSLConnectorConfigurator {
	
	public static final Logger log = LoggerFactory.getLogger(CANLSSLConnectorConfigurator.class);

	public static final String DEFAULT_TRUST_STORE_DIR = "/etc/grid-security/certificates";
	public static final String DEFAULT_CERT = "/etc/grid-security/hostcert.pem";
	public static final String DEFAULT_KEY = "/etc/grid-security/hostkey.pem";
		
	public static final NamespaceCheckingMode DEFAULT_NAMESPACE_CHECKING_MODE = NamespaceCheckingMode.EUGRIDPMA_AND_GLOBUS;
	
	public static final long DEFAULT_TRUST_STORE_REFRESH_INTERVAL_IN_MSECS = 600000L;
	
	public Connector configureConnector(String host, int port,
			Properties options) {
		
		Connector connector;
		
		try {
		
			PEMCredential serviceCredentials = new PEMCredential(DEFAULT_KEY, DEFAULT_CERT, null);
			
			// TODO: add support for custom validator params
			OpensslCertChainValidator validator = new OpensslCertChainValidator(DEFAULT_TRUST_STORE_DIR,
					DEFAULT_NAMESPACE_CHECKING_MODE,
					DEFAULT_TRUST_STORE_REFRESH_INTERVAL_IN_MSECS);
					
			validator.addValidationListener(new SSLValidationErrorReporter());
			
			SSLContext ctxt = SocketFactoryCreator.getSSLContext(serviceCredentials, validator, null);
			
			SslContextFactory factory = new SslContextFactory();
			factory.setSslContext(ctxt);
			factory.setWantClientAuth(true);
			factory.setNeedClientAuth(true);
			
			connector = new SslSelectChannelConnector(factory);
			connector.setHost(host);
			connector.setPort(port);
			
			return connector;
			
		} catch (Throwable t) {
			
			log.error("SSL initialization error!",t);
			return null;
		}
		
	}

}
