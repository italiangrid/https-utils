package org.italiangrid.utils.https.impl.canl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.ValidationError;
import eu.emi.security.authn.x509.ValidationErrorListener;
import eu.emi.security.authn.x509.impl.CertificateUtils;
import eu.emi.security.authn.x509.impl.FormatMode;

/**
 * A {@link ValidationErrorListener} used to log certificate validation errors.
 * 
 * @author andreaceccanti
 *
 */
public class SSLValidationErrorReporter implements ValidationErrorListener {

	public static final Logger log = LoggerFactory.getLogger(SSLValidationErrorReporter.class);
	
	public boolean onValidationError(ValidationError error) {
		
		String certChainInfo = CertificateUtils.format(error.getChain(), FormatMode.COMPACT_ONE_LINE);
		log.warn(certChainInfo);
		log.warn("Validation error: {}", error.getMessage());
		
		return false;
	}

}
