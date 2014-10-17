package org.italiangrid.utils.voms;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.security.auth.x500.X500Principal;

import eu.emi.security.authn.x509.helpers.CertificateHelpers;
import eu.emi.security.authn.x509.impl.OpensslNameUtils;
import eu.emi.security.authn.x509.impl.X500NameUtils;
import eu.emi.security.authn.x509.proxy.ProxyUtils;

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
   * The client's certificate issuer name, in a string formatted according to
   * RFC2253.
   */
  private String issuerX500Name;

  /**
   * The client's certificate subject in OpenSSL /-separated format.
   */
  private String clientName;

  /**
   * The client's certificate issuer in OpenSSL /-separated format.
   */
  private String issuerName;

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
   * Set the client certificate. This method also automatically sets the client
   * and issuer name.
   * 
   * @param clientCert
   *          The identity certificate of the authenticated client
   * 
   */
  public void setClientCert(X509Certificate clientCert) {

    this.clientCert = clientCert;

    X500Principal subject = clientCert.getSubjectX500Principal();
    setClientX500Principal(subject);
    setClientX500Name(X500NameUtils.getReadableForm(subject));

    setClientName(OpensslNameUtils.convertFromRfc2253(getClientX500Name(),
      false));

    X500Principal issuer = clientCert.getIssuerX500Principal();

    setIssuerX500Principal(issuer);
    setIssuerX500Name(X500NameUtils.getReadableForm(issuer));
    setIssuerName(OpensslNameUtils.convertFromRfc2253(getIssuerX500Name(),
      false));
  }

  /**
   * Get the client certificate.
   * 
   * @return X509Certificate The identity certificate of the authenticated
   *         client
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
   * @param clientX500Name
   *          The name of the authenticated client
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
   * @param clientX500Principal
   *          The name of the authenticated client
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
   * @param clientName
   *          The name of the authenticated client
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
   * @param issuerPrincipal
   *          The name of the authenticated client
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
   * This method sets the client cert chain for this context and initializes the
   * client certificate as well.
   * 
   * If the chain passed as argument is a proxy certificate chain, the client
   * certificate is set from the user certificate present in the chain.
   * 
   * @param clientCertChain
   *          The client's certificate chain
   */
  public void setClientCertChain(X509Certificate[] clientCertChain) {

    X509Certificate[] orderedClientCertChain = null;

    try {

      orderedClientCertChain = CertificateHelpers.sortChain(Arrays
        .asList(clientCertChain));

    } catch (IOException e) {

      throw new RuntimeException(e.getMessage(), e);
    }

    this.clientCertChain = orderedClientCertChain;

    if (ProxyUtils.isProxy(clientCertChain)) {
      setClientCert(ProxyUtils.getEndUserCertificate(clientCertChain));
    } else {
      setClientCert(clientCertChain[0]);
    }

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
   * @param remoteAddr
   *          the IP address of the other party to save
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
   * @param the
   *          SSL session ID used for this connection.
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

  /**
   * @return the clientName
   */
  public String getClientName() {

    return clientName;
  }

  /**
   * @param clientName
   *          the clientName to set
   */
  public void setClientName(String clientName) {

    this.clientName = clientName;
  }

  /**
   * @return the issuerName
   */
  public String getIssuerName() {

    return issuerName;
  }

  /**
   * @param issuerName
   *          the issuerName to set
   */
  public void setIssuerName(String issuerName) {

    this.issuerName = issuerName;
  }

}
