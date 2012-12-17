package org.italiangrid.utils.voms;

import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

/**
 * Interface to security information for an authenticated user.
 * 
 * @author valerioventuri
 *
 */
public interface SecurityContext {

  /**
   * Returns the client certificate.
   * 
   * @return X509Certificate The authenticated client's certificate
   */
  public X509Certificate getClientCert();

  /**
   * Returns the client certificate chain.
   * 
   * @return X509Certificate[] The authenticated client's certificate chain
   */
  public X509Certificate[] getClientCertChain();

  /**
   * Returns the name of the authenticated client in 
   * RFC2253 string format for distinguished names.
   *
   * @return String The name of the authenticated client.
   */
  public String getClientX500Name();
  
  /**
   * Returns the {@link X500Principal} of the authenticated client's
   * certificate.
   *
   * @return X500Principal The Principal of the authenticated client.
   */
  public X500Principal getClientX500Principal();
  
  /**
   * Returns the name of the issuer of the authenticated client's
   * certificate in RFC 2253 string format for distinguished names.
   *
   * @return String The name of the authenticated client.
   */
  public String getIssuerX500Name();
  
  /**
   * Returns the {@link X500Principal} of the issuer of the authenticated client's
   * certificate.
   *
   * @return X500Principal The Principal of the authenticated client.
   */
  public X500Principal getIssuerX500Principal();
  
  /**
   * Returns the IP address of the other party.
   * 
   * @return The remote address as a String.
   */
  public String getRemoteAddr();
  
  /**
   * Returns the SSL session ID used for this connection.
   * 
   * @return The session id as a String.
   */
  public String getSessionId();
  
}
