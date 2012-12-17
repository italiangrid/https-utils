package org.italiangrid.utils.voms;

import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

/**
 * 
 * 
 * @author valerioventuri
 *
 */
public class SecurityContextImpl implements SecurityContext {

  /**
   * The client certificate.
   */
  private X509Certificate clientCert;
  
  /**
   * The client certificate chain.
   */
  private X509Certificate[] clientCertChain;
  
  /**
   * The client name, in a string formatted according to RFC2253.
   */
  private String clientX500Name;
  
  /**
   * The client {@link X500Principal}.
   */
  private X500Principal clientX500Principal;
  

  /**
   * The client's certificate issuer name, in a string formatted according to RFC2253.
   */
  private String issuerX500Name;
  
  /**
   * The client's certificate issuer {@link X500Principal}.
   */
  private X500Principal issuerX500Principal;
  
  /**
   * The ip address of the client.
   */
  private String remoteAddr;
  
  /**
   * The identified of the ssl session.
   */
  private String sessionId;
  
  /** 
   * Thread local storage for locating the active security context. 
   */
  private static ThreadLocal<SecurityContextImpl> theSecurityContext = new ThreadLocal<SecurityContextImpl>();

  /**
   * Get the security context associated with the current thread.
   * 
   * @return SecurityContext the SecurityContext associated with the current thread.
   */
  public static SecurityContextImpl getCurrentContext() {
    return theSecurityContext.get();
  }

  /**
   * Set the security context associated with the current thread.
   * 
   * @param sc the {@link SecurityContextImpl} associated with the current thread.
   */
  public static void setCurrentContext(SecurityContextImpl sc) {
    theSecurityContext.set(sc);
  }

  /**
   * Clears any set SecurityContext associated with the current thread. This is identical to
   * <code>SecurityContext.setCurrentContext(null)</code>.
   */
  public static void clearCurrentContext() {
    theSecurityContext.set(null);
  }

  /**
   * Set the client certificate. This method also automatically sets the client and issuer name.
   * 
   * @param clientCert The identity certificate of the authenticated client
   * 
   */
  public void setClientCert(X509Certificate clientCert) {

    this.clientCert = clientCert;
    
    X500Principal subject = clientCert.getSubjectX500Principal();
    setClientX500Principal(subject);
    setClientX500Name(subject.getName());

    X500Principal issuer = clientCert.getIssuerX500Principal();
    setIssuerX500Principal(issuer);
  }

  /**
   * Get the client certificate.
   * 
   * @return X509Certificate The identity certificate of the authenticated client
   * @see #CLIENT_NAME
   * @see #setClientCert(X509Certificate)
   */
  public X509Certificate getClientCert() {
    return clientCert;
  }

  /**
   * Set the name of the client, as a {@link String} containing the RFC2253 
   * representation of the name.
   *
   * @param clientX500Name The name of the authenticated client
   */
  public void setClientX500Name(String clientX500Name) {
    this.clientX500Name = clientX500Name;
  }

  /**
   * Get the name of the client, as a {@link String} containing the RFC2253 
   * representation of the name.
   * 
   * @return String The name of the authenticated client
   * @see #CLIENT_X500_NAME
   * @see #setClientX500Name(String)
   */
  public String getClientX500Name() {
    return clientX500Name;
  }

  /**
   * Set the name of the client, as a {@link X500Principal} object.
   * 
   * @param clientX500Principal The name of the authenticated client
   */
  public void setClientX500Principal(X500Principal clientX500Principal) {
    this.clientX500Principal = clientX500Principal;
  }

  /**
   * Set the name of the client, as a {@link X500Principal} object.
   * 
   * @return X500Principal The Principal of the authenticated client
   */
  public X500Principal getClientX500Principal() {
    return clientX500Principal;
  }

  /**
   * @param clientName The name of the authenticated client
   */
  public void setIssuerX500Name(String issuerName) {
    this.issuerX500Name = issuerName;
  }

  /**
   * @return String The name of the authenticated client
   */
  public String getIssuerX500Name() {
    return issuerX500Name;
  }

  /**
   * @param issuerPrincipal The name of the authenticated client
   */
  public void setIssuerX500Principal(X500Principal issuerPrincipal) {
    this.issuerX500Principal = issuerPrincipal;
  }

  /**
   * @return X500Principal The Principal of the authenticated client
   */
  public X500Principal getIssuerX500Principal() {
    return issuerX500Principal;
  }

  /**
   * This method also automatically sets the client certificate.
   * 
   * @param clientCertChain The client's certificate chain
   */
  public void setClientCertChain(X509Certificate[] clientCertChain) {

    this.clientCertChain = clientCertChain;

    int i;

    // get the index for the first cert that isn't a CA or proxy cert
    for (i = clientCertChain.length - 1; i >= 0; i--) {

      // if constrainCheck = -1 the cert is NOT a CA cert
      if (clientCertChain[i].getBasicConstraints() == -1) {

        // double check, if issuerDN = subjectDN the cert is CA
        if (!clientCertChain[i].getIssuerDN().equals(clientCertChain[i].getSubjectDN())) {
          break;
        }
      }
    }

    setClientCert(clientCertChain[i]);
  }

  /**
   * @return X509Certificate[] The client's certificate chain
   */
  public X509Certificate[] getClientCertChain() {
    return clientCertChain;
  }

  /**
   * Sets the IP address of the other party.
   * 
   * @param remoteAddr the IP address of the other party to save
   */
  public void setRemoteAddr(String remoteAddr) {
    this.remoteAddr = remoteAddr;
  }

  /**
   * @return the IP address of the other party.
   */
  public String getRemoteAddr() {
    return remoteAddr;
  }

  /**
   * @param the SSL session ID used for this connection.
   */
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  /**
   * @return the SSL session ID used for this connection.
   */
  public String getSessionId() {
    return sessionId;
  }

}
