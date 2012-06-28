package org.italiangrid.utils.https.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.ValidationError;
import eu.emi.security.authn.x509.ValidationErrorListener;
import eu.emi.security.authn.x509.impl.CertificateUtils;
import eu.emi.security.authn.x509.impl.FormatMode;

public class SSLValidationErrorReporter implements ValidationErrorListener {

	public static final Logger log = LoggerFactory.getLogger(SSLValidationErrorReporter.class);
	
	public boolean onValidationError(ValidationError error) {
		
		String certChainInfo = CertificateUtils.format(error.getChain(), FormatMode.COMPACT_ONE_LINE);
		log.warn("Certificate validation error for certificate: {}, {}", new String[]{certChainInfo,error.getMessage()});
		
		return false;
	}

}
