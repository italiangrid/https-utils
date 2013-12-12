package org.italiangrid.utils.voms;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.ac.VOMSACValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Jetty Handler that initializes a {@link VOMSSecurityContextImpl} and logs a message about
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

	private final VOMSACValidator validator;
	
	private final boolean secure;
	
	public VOMSSecurityContextHandler(VOMSACValidator validator, boolean secure) {
		this.validator = validator;
		this.secure = secure;
	}
	
	
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		CurrentSecurityContext.clear();
		VOMSSecurityContext sc = new VOMSSecurityContextImpl(validator, secure);
		CurrentSecurityContext.set(sc);
		
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
		
		List<VOMSAttribute> vomsAttributes = sc.getVOMSAttributes();
		
		String vomsAttrsString = "No valid VOMS attributes found.";
		
		vomsAttrsString = String.format("VOMS attributes: %s .",vomsAttributes);
		
		
		String connectionMessage = String.format("Connection from '%s' by '%s' (issued by '%s') serial: %s. %s", 
				request.getRemoteAddr(),
				sc.getClientX500Name(),
				sc.getIssuerX500Name(),
				serialNumber,
				vomsAttrsString);
		
		log.info(connectionMessage);
	}

	

}
