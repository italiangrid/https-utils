package org.italiangrid.utils.examples;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.italiangrid.utils.voms.VOMSSecurityContext;
import org.italiangrid.utils.voms.VOMSSecurityContextHandler;

/**
 * A simple handler which prints authentication information.
 * Use this after the {@link VOMSSecurityContextHandler}.
 *  
 * @author andreaceccanti
 *
 */
public class PrintAuthenticationInformationHandler extends AbstractHandler {

	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        VOMSSecurityContext sc = VOMSSecurityContext.getCurrentContext();
        
        
        response.getWriter().println("<h1>Hello!</h1>");
        BigInteger sn = sc.getClientCert().getSerialNumber();
		
		String serialNumber = (sn == null) ? "NULL" : sn.toString();
		
		String[] fqans = sc.getFQANs();
		
		String vomsFQANString = "No VOMS attributes found.";
		
		if (fqans.length > 0)
			vomsFQANString = String.format("VOMS attributes: %s .",StringUtils.join(fqans,","));
		
		
		String connectionMessage = String.format("<p>You connected from '%s', as '%s' (issued by '%s') serial: %s.</p><p>%s</p>", 
				request.getRemoteAddr(),
				sc.getClientDN().getRFCDNv2(),
				sc.getIssuerDN().getRFCDNv2(),
				serialNumber,
				vomsFQANString);
        
		response.getWriter().println(connectionMessage);

	}

}
