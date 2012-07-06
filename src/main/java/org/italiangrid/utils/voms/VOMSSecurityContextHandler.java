package org.italiangrid.utils.voms;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.X509Certificate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Jetty Handler that initializes a {@link VOMSSecurityContext} and logs a message about
 * the authenticated connection.
 * 
 * It should be included in front of other handlers to initialize the security context so that handlers
 * down the line can process the X.509 and VOMS attributes and leverage the information to take
 * authorization decisions.
 * 
 * @author andreaceccanti
 *
 */
public class VOMSSecurityContextHandler extends AbstractHandler implements Handler {

	public static final Logger log = LoggerFactory.getLogger(VOMSSecurityContextHandler.class);

	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		VOMSSecurityContext.clearCurrentContext();
		
		VOMSSecurityContext sc = new VOMSSecurityContext();
		VOMSSecurityContext.setCurrentContext(sc);
		
		X509Certificate[] certChain = null;
		
		try {
			
			certChain = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
		
		} catch (Exception e) {
			log.warn("Error fetching certificate from http request: {}", e.getMessage(), e);
			// We swallow the exception and continue processing.
		}
		
		if (certChain == null){
			log.info("Unauthenticated connection from {}", request.getRemoteAddr());
			return;
		}
		
		sc.setClientCertChain(certChain);
		BigInteger sn = sc.getClientCert().getSerialNumber();
		
		String serialNumber = (sn == null) ? "NULL" : sn.toString();
		
		String[] fqans = sc.getFQANs();
		
		String vomsFQANString = "No VOMS attributes found.";
		
		if (fqans.length > 0)
			vomsFQANString = String.format("VOMS attributes: %s .",StringUtils.join(fqans,","));
		
		
		String connectionMessage = String.format("Connection from '%s' by '%s' (issued by '%s') serial: %s. %s", 
				request.getRemoteAddr(),
				sc.getClientDN().getRFCDNv2(),
				sc.getIssuerDN().getRFCDNv2(),
				serialNumber,
				vomsFQANString);
		
		log.info(connectionMessage);
	}

	

}
