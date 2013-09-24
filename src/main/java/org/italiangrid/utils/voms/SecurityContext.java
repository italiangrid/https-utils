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
   * Sets the client end-entity certificate.
   * 
   * @param cert The authenticated client's certificate
   */
  public void setClientCert(X509Certificate cert);
  
  /**
   * Returns the client certificate chain.
   * 
   * @return X509Certificate[] The authenticated client's certificate chain
   */
  public X509Certificate[] getClientCertChain();

  /**
   * Sets the client certificate chain
   * 
   * @param chain The authenticated client's certificate chain
   */
  public void setClientCertChain(X509Certificate[] chain);
  
  /**
   * Returns the name of the authenticated client in 
   * RFC2253 string format for distinguished names.
   *
   * @return String The name of the authenticated client.
   */
  public String getClientX500Name();
  
  /**
   * Sets the authenticated client name in RFC2253 format
   * 
   * @param clientName the name of the authenticated client
   */
  public void setClientX500Name(String clientName);
  
  /**
   * Returns the {@link X500Principal} of the authenticated client's
   * certificate.
   *
   * @return X500Principal The Principal of the authenticated client.
   */
  public X500Principal getClientX500Principal();
  
  /**
   * Sets the {@link X500Principal} of the authenticated client's
   * certificate.
   * 
   * @param principal The Principal of the authenticated client.
   */
  public void setClientX500Principal(X500Principal principal);
  
  /**
   * Returns the name of the authenticated client's certificate in OpenSSL /-separated format
   * for distinguished names.
   * 
   * @return the client name in OpenSSL /-separated format
   */
  public String getClientName();
  
  /**
   * Sets the name of the authenticated client's certificate in OpenSSL /-separated format
   * for distinguished names.
   * 
   * @param clientName the client name in OpenSSL /-separated format
   */
  public void setClientName(String clientName);
  
  /**
   * Returns the name of the issuer of the authenticated client's certificate in OpenSSL /-separated
   * format for distinguished names.
   * @return the issuer name in OpenSSL /-separated format
   */
  public String getIssuerName();
  
  /**
   * Sets the name of the issuer of the authenticated client's certificate in OpenSSL /-separated
   * format for distinguished names.
   * @param issuerName the issuer name in OpenSSL /-separated format
   */
  public void setIssuerName(String issuerName);
  
  
  /**
   * Returns the name of the issuer of the authenticated client's
   * certificate in RFC 2253 string format for distinguished names.
   *
   * @return String The name of the authenticated client.
   */
  public String getIssuerX500Name();
  
  /**
   * Sets the name of the issuer of the authenticated client's
   * certificate in RFC 2253 string format for distinguished names.
   * @param issuerX500Name The name of the authenticated client.
   */
  public void setIssuerX500Name(String issuerX500Name);
  
  /**
   * Returns the {@link X500Principal} of the issuer of the authenticated client's
   * certificate.
   *
   * @return X500Principal The Principal of the authenticated client.
   */
  public X500Principal getIssuerX500Principal();
  
  /**
   * Sets the {@link X500Principal} of the issuer of the authenticated client's
   * certificate.
   * 
   * @param principal the issuer x500 principal 
   */
  public void setIssuerX500Principal(X500Principal principal);
  
  /**
   * Returns the IP address of the other party.
   * 
   * @return The remote address as a String.
   */
  public String getRemoteAddr();
  
  /**
   * Sets the IP address of the other party.
   * @param remoteIpAddress The remote address as a String.
   */
  public void setRemoteAddr(String remoteIpAddress);
  
  
  /**
   * Returns the SSL session ID used for this context.
   * 
   * @return The session id as a String.
   */
  public String getSessionId();
  
  /**
   * Sets the SSL session ID for this context.
   * @param sessionId the session id
   */
  public void setSessionId(String sessionId);
  
}
