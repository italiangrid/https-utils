package org.italiangrid.utils.voms;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.x500.X500Principal;

/**
 * 
 * 
 * @author valerioventuri
 *
 */
public class SecurityContext extends Properties implements SecurityInfo {

  /** UID */
  private static final long serialVersionUID = -7396219279112154202L;

  /** 
   * Thread local storage for locating the active security context. 
   */
  private static ThreadLocal<SecurityContext> theSecurityContexts = new ThreadLocal<SecurityContext>();

  /**
   * The label for the client identity certificate property.
   * 
   */
  public static final String CERT_CHAIN = "certchain";

  /**
   * The label for the client identity certificate property.
   * 
   */
  public static final String CLIENT_CERT = "clientcert";

  /**
   * The label for the client name property.
   * 
   */
  public static final String CLIENT_X500_NAME = "clientX500name";

  /**
   * The label for the client name property.
   * 
   */
  public static final String CLIENT_X500_PRINCIPAL = "clientX500Principal";

  /**
   * The label for the issuer name property.
   * 
   */
  public static final String ISSUER_X500_NAME = "clientX500name";

  /**
   * The label for the issuer name property.
   * 
   */
  public static final String ISSUER_X500_PRINCIPAL = "clientX500Principal";

  /**
   * The label for UnverifiedCertChain property.
   * 
   */
  public static final String UNVERIFIED_CERT_CHAIN = "unverifiedchain";

  /**
   * The label for peer CA Principal list property.
   * 
   * @see #getPeerCas
   * @see #setPeerCas
   */
  public static final String PEER_CAS = "peercas";

  /**
   * The label for the ip address of the other party property.
   */
  public static final String REMOTE_ADDR = "remoteaddr";

  /**
   * The label for the SSL session Id for this connection property.
   */
  public static final String SESSION_ID = "sessionid";

  /**
   * Constructor.
   * 
   * @see java.util.Properties#Properties()
   */
  public SecurityContext() {
    super();
  }

  /**
   * Get the security context associated withv the current thread.
   * 
   * @return SecurityContext the SecurityContext associated with the current thread.
   */
  public static SecurityContext getCurrentContext() {
    return theSecurityContexts.get();
  }

  /**
   * Set the security context associated with the current thread.
   * 
   * @param sc the {@link SecurityContext} associated with the current thread.
   */
  public static void setCurrentContext(SecurityContext sc) {
    theSecurityContexts.set(sc);
  }

  /**
   * Clears any set SecurityContext associated with the current thread. This is identical to
   * <code>SecurityContext.setCurrentContext(null)</code>.
   */
  public static void clearCurrentContext() {
    theSecurityContexts.set(null);
  }

  /**
   * Set the client certificate. This method also automatically sets the client and issuer name.
   * 
   * @param clientCert The identity certificate of the authenticated client
   * 
   */
  public void setClientCert(X509Certificate clientCert) {

    put(CLIENT_CERT, clientCert);

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
    return (X509Certificate) get(CLIENT_CERT);
  }

  /**
   * Set the name of the client, as a {@link String} containing the RFC2253 
   * representation of the name.
   *
   * @param clientName The name of the authenticated client
   */
  public void setClientX500Name(String clientName) {
    put(CLIENT_X500_NAME, clientName);
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
    return getProperty(CLIENT_X500_NAME);
  }

  /**
   * Set the name of the client, as a {@link X500Principal} object.
   * 
   * @param clientPrincipal The name of the authenticated client
   */
  public void setClientX500Principal(X500Principal clientPrincipal) {
    put(CLIENT_X500_PRINCIPAL, clientPrincipal);
  }

  /**
   * Set the name of the client, as a {@link X500Principal} object.
   * 
   * @return X500Principal The Principal of the authenticated client
   */
  public X500Principal getClientX500Principal() {
    return (X500Principal) get(CLIENT_X500_PRINCIPAL);
  }

  /**
   * @param clientName The name of the authenticated client
   */
  public void setIssuerX500Name(String issuerName) {
    put(ISSUER_X500_NAME, issuerName);
  }

  /**
   * @return String The name of the authenticated client
   */
  public String getIssuerX500Name() {
    return getProperty(ISSUER_X500_NAME);
  }

  /**
   * @param issuerPrincipal The name of the authenticated client
   */
  public void setIssuerX500Principal(X500Principal issuerPrincipal) {
    put(ISSUER_X500_PRINCIPAL, issuerPrincipal);
  }

  /**
   * @return X500Principal The Principal of the authenticated client
   */
  public X500Principal getIssuerX500Principal() {
    return (X500Principal) get(ISSUER_X500_PRINCIPAL);
  }

  /**
   * This method also automatically sets the client name, the issuer name, validity period.
   * 
   * @param certChain The client's certificate chain
   */
  public void setClientCertChain(X509Certificate[] certChain) {

    put(CERT_CHAIN, certChain);

    int i;

    // get the index for the first cert that isn't a CA or proxy cert
    for (i = certChain.length - 1; i >= 0; i--) {

      // if constrainCheck = -1 the cert is NOT a CA cert
      if (certChain[i].getBasicConstraints() == -1) {

        // double check, if issuerDN = subjectDN the cert is CA
        if (!certChain[i].getIssuerDN().equals(certChain[i].getSubjectDN())) {
          break;
        }
      }
    }

    setClientCert(certChain[i]);
  }

  /**
   * @return X509Certificate[] The client's certificate chain
   */
  public X509Certificate[] getClientCertChain() {
    return (X509Certificate[]) get(CERT_CHAIN);
  }

  /**
   * @param certChain The unverified certificate chain
   */
  public void setUnverifiedCertChain(X509Certificate[] certChain) {
    put(UNVERIFIED_CERT_CHAIN, certChain);
  }

  /**
   * @return X509Certificate[] The unverified certificate chain
   */
  public X509Certificate[] getUnverifiedCertChain() {
    return (X509Certificate[]) get(UNVERIFIED_CERT_CHAIN);
  }

  /**
   * 
   * @param principals The list of accepted CAs from the peer
   */
  public void setPeerCas(Principal[] principals) {
    put(PEER_CAS, principals);
  }

  /**
   * @return Principal[] The list of accepted CAs from the peer
   */
  public Principal[] getPeerCas() {
    return (Principal[]) get(PEER_CAS);
  }

  /**
   * Sets the IP address of the other party.
   * 
   * @param remoteAddr the IP address of the other party to save
   */
  public void setRemoteAddr(String remoteAddr) {
    put(REMOTE_ADDR, remoteAddr);
  }

  /**
   * @return the IP address of the other party.
   */
  public String getRemoteAddr() {
    return getProperty(REMOTE_ADDR);
  }

  /**
   * @param the SSL session ID used for this connection.
   */
  public void setSessionId(String sessionId) {
    put(SESSION_ID, sessionId);
  }

  /**
   * @return the SSL session ID used for this connection.
   */
  public String getSessionId() {
    return getProperty(SESSION_ID);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Hashtable#toString()
   */
  public synchronized String toString() {
    
    StringBuffer sb = new StringBuffer();
    
    sb.append("SecurityContext:\n");

    for(Map.Entry<Object, Object> entry : entrySet()) {

      sb.append("  " + entry.getKey() + " : " + entry.getValue() + "\n");
    }
    
    return sb.toString();
  }

}
