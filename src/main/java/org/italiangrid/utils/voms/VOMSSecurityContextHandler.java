/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare, 2006-2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.italiangrid.utils.voms;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.italiangrid.voms.ac.VOMSACValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Jetty Handler that initializes a {@link VOMSSecurityContextImpl} and logs a
 * message about the authenticated connection.
 * 
 * It should be included in front of other handlers to initialize the security
 * context so that handlers down the line can process the X.509 and VOMS
 * attributes and leverage the information to take authorization decisions.
 * 
 * @author andreaceccanti
 * 
 */
public class VOMSSecurityContextHandler extends AbstractHandler implements
  Handler {

  public static final Logger log = LoggerFactory
    .getLogger(VOMSSecurityContextHandler.class);

  public static final String CONTEXT_KEY = "org.italiangrind.SecurityContext";

  private static final long SESSION_LIFETIME_IN_MSECS = TimeUnit.MINUTES
    .toMillis(5);

  private final VOMSACValidator validator;

  private final boolean secure;

  public VOMSSecurityContextHandler(VOMSACValidator validator, boolean secure) {

    this.validator = validator;
    this.secure = secure;
  }

  private HttpSession getSession(HttpServletRequest request) {

    HttpSession session = request.getSession();

    if (!session.isNew()) {
      long creationTime = session.getCreationTime();
      if (System.currentTimeMillis() - creationTime > SESSION_LIFETIME_IN_MSECS) {
        session.invalidate();
        session = request.getSession(true);
      }
    }
    return session;
  }

  private void initSecurityContext(HttpServletRequest request) {

    try {

      VOMSSecurityContext sc = new VOMSSecurityContextImpl(validator, secure);

      X509Certificate[] certChain = (X509Certificate[]) request
        .getAttribute("javax.servlet.request.X509Certificate");

      sc.setClientCertChain(certChain);

      request.getSession().setAttribute(CONTEXT_KEY, sc);

    } catch (Throwable t) {

      log.error(t.getMessage(), t);
    }
  }

  private VOMSSecurityContext getSecurityContext(HttpServletRequest request) {

    HttpSession session = getSession(request);
    VOMSSecurityContext sc = (VOMSSecurityContext) session
      .getAttribute(CONTEXT_KEY);

    if (sc == null) {
      initSecurityContext(request);
    }

    return (VOMSSecurityContext) session.getAttribute(CONTEXT_KEY);
  }

  public void handle(String target, Request baseRequest,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    VOMSSecurityContext sc = getSecurityContext(request);

    if (sc.getClientCertChain() == null) {
      log
        .info("Unauthenticated connection from '{}'.", request.getRemoteAddr());
    } else {

      String connectionMessage = String.format(
        "Connection from '%s' by '%s' (issued by '%s') serial: %s. %s",
        request.getRemoteAddr(), sc.getClientX500Name(),
        sc.getIssuerX500Name(), sc.getClientCert().getSerialNumber(),
        sc.getVOMSAttributes());

      log.debug(connectionMessage);
    }
  }

}
