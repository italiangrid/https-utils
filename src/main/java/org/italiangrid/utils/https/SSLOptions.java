package org.italiangrid.utils.https;

/**
 * This class provides a container for a set of commonly used SSL connector configuration options.
 * 
 * @author andreaceccanti
 *
 */
public final class SSLOptions {
	
	/**
	 * The default options object
	 */
	public static final SSLOptions DEFAULT_OPTIONS = new SSLOptions();
	
	/**
	 * Default trust store directory
	 */
	public static final String DEFAULT_TRUST_STORE_DIR = "/etc/grid-security/certificates";
	
	/**
	 * Default certificate location
	 */
	public static final String DEFAULT_CERT = "/etc/grid-security/hostcert.pem";
	
	/**
	 * Default private key location
	 */
	public static final String DEFAULT_KEY = "/etc/grid-security/hostkey.pem";
	
	/**
	 * Default trust store directory refresh interval. Sets how often the trust
	 * store directory is searched for new certificates or updated CRLs
	 */
	public static final long DEFAULT_TRUST_STORE_REFRESH_INTERVAL_IN_MSECS = 600000L;
	
	private String certificateFile = DEFAULT_CERT;
	private String keyFile = DEFAULT_KEY;
	private char[] keyPassword = null;
	private String trustStoreDirectory = DEFAULT_TRUST_STORE_DIR;
	
	private long trustStoreRefreshIntervalInMsec = DEFAULT_TRUST_STORE_REFRESH_INTERVAL_IN_MSECS; 
	private boolean wantClientAuth = true;
	private boolean needClientAuth = true;
	
	
	public SSLOptions() {}
		
	
	public String getCertificateFile() {
		return certificateFile;
	}
	
	public void setCertificateFile(String certificateFile) {
		this.certificateFile = certificateFile;
	}
	
	public String getKeyFile() {
		return keyFile;
	}
	
	public void setKeyFile(String keyFile) {
		this.keyFile = keyFile;
	}
	
	public String getTrustStoreDirectory() {
		return trustStoreDirectory;
	}
	
	public void setTrustStoreDirectory(String trustStoreDirectory) {
		this.trustStoreDirectory = trustStoreDirectory;
	}
	
	public long getTrustStoreRefreshIntervalInMsec() {
		return trustStoreRefreshIntervalInMsec;
	}
	
	public void setTrustStoreRefreshIntervalInMsec(
			long trustStoreRefreshIntervalInMsec) {
		this.trustStoreRefreshIntervalInMsec = trustStoreRefreshIntervalInMsec;
	}
	
	public boolean isWantClientAuth() {
		return wantClientAuth;
	}
	
	public void setWantClientAuth(boolean wantClientAuth) {
		this.wantClientAuth = wantClientAuth;
	}
	
	public boolean isNeedClientAuth() {
		return needClientAuth;
	}
	
	public void setNeedClientAuth(boolean needClientAuth) {
		this.needClientAuth = needClientAuth;
	}

	public char[] getKeyPassword() {
		return keyPassword;
	}

	public void setKeyPassword(char[] keyPassword) {
		this.keyPassword = keyPassword;
	}	
}
