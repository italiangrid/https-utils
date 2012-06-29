package org.italiangrid.utils.https.impl.emi_trustmanager;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Connector;
import org.glite.security.trustmanager.ContextWrapper;
import org.glite.security.util.CaseInsensitiveProperties;
import org.italiangrid.utils.https.JettySSLConnectorConfigurator;
import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.impl.SSLContextConnectorConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link JettySSLConnectorConfigurator} that leverages the EMI trustmanager {@link ContextWrapper}
 * object.
 * 
 * @author andreaceccanti
 *
 */
public class TrustmanagerSSLConnectorConfigurator implements
		JettySSLConnectorConfigurator {

	public static final Logger log = LoggerFactory.getLogger(TrustmanagerSSLConnectorConfigurator.class);
	
	SSLContextConnectorConfigurator configurator;
	
	private CaseInsensitiveProperties buildTMConfiguration(SSLOptions options){
		
		Properties tmProps = new Properties();
		
		tmProps.setProperty("sslCertFile", options.getCertificateFile());
		tmProps.setProperty("sslKey", options.getKeyFile());
		tmProps.setProperty("crlEnabled", "true");
		
		long refreshIntervalInMinutes = TimeUnit.MILLISECONDS.toMinutes(options.getTrustStoreRefreshIntervalInMsec());
		
		tmProps.setProperty("crlUpdateInterval", String.format("%dm",refreshIntervalInMinutes));
		tmProps.setProperty("trustStoreDir", options.getTrustStoreDirectory());
		
		return new CaseInsensitiveProperties(tmProps);
	}
	
	public Connector configureConnector(String host, int port,
			SSLOptions options) {
		
		try{
			
			ContextWrapper context = new ContextWrapper(buildTMConfiguration(options), false);
			configurator = new SSLContextConnectorConfigurator(context.getContext());
			return configurator.configureConnector(host, port, options);
			
		}catch (Throwable t) {
			log.error("SSL initialization error!", t);
			return null;
		}
	}

}
