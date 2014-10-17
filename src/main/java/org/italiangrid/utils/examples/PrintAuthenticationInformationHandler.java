package org.italiangrid.utils.examples;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.italiangrid.utils.voms.CurrentSecurityContext;
import org.italiangrid.utils.voms.VOMSSecurityContext;
import org.italiangrid.utils.voms.VOMSSecurityContextHandler;
import org.italiangrid.voms.VOMSAttribute;

/**
 * A simple handler which prints authentication information. Use this after the
 * {@link VOMSSecurityContextHandler}.
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
    VOMSSecurityContext sc = (VOMSSecurityContext) CurrentSecurityContext.get();

    response.getWriter().println("<h1>Hello!</h1>");
    BigInteger sn = sc.getClientCert().getSerialNumber();

    String serialNumber = (sn == null) ? "NULL" : sn.toString();

    List<VOMSAttribute> vomsAttributes = sc.getVOMSAttributes();

    String vomsAttrsString = "No valid VOMS attributes found.";

    if (vomsAttributes.size() > 0)
      vomsAttrsString = String.format("VOMS attributes: %s .", vomsAttributes);

    String connectionMessage = String.format(
      "Connection from '%s' by '%s' (issued by '%s') serial: %s. %s",
      request.getRemoteAddr(), sc.getClientX500Name(), sc.getIssuerX500Name(),
      serialNumber, vomsAttrsString);

    response.getWriter().println(connectionMessage);

  }

}
