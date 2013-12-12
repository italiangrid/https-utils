package org.italiangrid.utils.examples;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.italiangrid.utils.https.JettyRunThread;
import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.ServerFactory;
import org.italiangrid.utils.https.impl.canl.CANLListener;
import org.italiangrid.utils.voms.VOMSSecurityContextHandler;
import org.italiangrid.voms.VOMSValidators;
import org.italiangrid.voms.store.VOMSTrustStore;
import org.italiangrid.voms.store.impl.DefaultVOMSTrustStore;
import org.italiangrid.voms.util.CertificateValidatorBuilder;

import eu.emi.security.authn.x509.X509CertChainValidatorExt;

/** 
 * A simple test server that demonstrates how to create an SSL-enabled server
 * and a related shutdown service.
 *
 */
public class TestServer {

	static class OkHandler extends DefaultHandler{
		@Override
		public void handle(String target, Request baseRequest,
				HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			response.setContentType("text/html;charset=utf-8");
	        response.setStatus(HttpServletResponse.SC_OK);
	        
	        baseRequest.setHandled(true);
	        response.getWriter().println("<h1>Ok!</h1>");
		}
	}
	
	TestServer(String hostname, 
			int port, 
			String serverCertFile, 
			String serverKeyFile,
			String trustStoreDir,
			boolean secure) throws Exception {
		
		
		SSLOptions options = new SSLOptions();
		options.setCertificateFile(serverCertFile);
		options.setKeyFile(serverKeyFile);
		options.setTrustStoreDirectory(trustStoreDir);
		
		
		
		X509CertChainValidatorExt validator = 
			CertificateValidatorBuilder.buildCertificateValidator(trustStoreDir, 
				new CANLListener(), 
				TimeUnit.MINUTES.toMillis(10), 
				false);

		VOMSTrustStore ts = new DefaultVOMSTrustStore();
		
		Server s = ServerFactory.newServer(hostname, port, options, validator, 
			300, 500);
		
		
		HashSessionIdManager idManager = new HashSessionIdManager();
		s.setSessionIdManager(idManager);
		
		HashSessionManager sessionManager = new HashSessionManager();
		SessionHandler sessions = new SessionHandler(sessionManager);
		
		Handler vomsHandler = new VOMSSecurityContextHandler(VOMSValidators.newValidator(ts, validator),
				secure);
		
		HandlerCollection handlers = new HandlerCollection();
		
		handlers.setHandlers(
				new Handler[]{ 
					sessions,
					vomsHandler, 
					new OkHandler()
				});
		
		s.setHandler(handlers);
		
		QueuedThreadPool tp = new QueuedThreadPool(300);
		tp.setMaxQueued(500);
		tp.setMinThreads(10);
		
		s.setThreadPool(tp);
		
		JettyRunThread rt = new JettyRunThread(s);
		rt.start();
		
	}
	
	public static void main(String[] args) throws Exception {
		
		String cert = System.getenv("X509_USER_CERT");
		String key = System.getenv("X509_USER_KEY");
		boolean secure = (System.getenv("HTTPS_UTILS_INSECURE") == null);
		
		if (cert == null)
			cert = "/etc/grid-security/hostcert.pem";
		
		if (key == null)
			key = "/etc/grid-security/hostkey.pem";
		
		String trustDir = "/etc/grid-security/certificates";
		
		String hostname = "localhost";
		int port = 4433;
		
		if (args.length == 2){
			hostname = args[0];
			port = Integer.parseInt(args[1]);
		}
		
		new TestServer(hostname, port, cert, key, trustDir, secure);
		
	}
}