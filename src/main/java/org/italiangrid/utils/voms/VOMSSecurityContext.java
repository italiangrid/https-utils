package org.italiangrid.utils.voms;



import java.security.cert.X509Certificate;

import org.glite.security.SecurityContext;
import org.glite.voms.VOMSValidator;

/**
 * A class representing an X.509 authentication context augmented with VOMS attributes. The context is stored in a {@link ThreadLocal}.
 * The validation and extraction of VOMS attributes is triggered by the {@link #setClientCertChain(X509Certificate[])} method.
 * 
 * Attributes can then be accessed via the {@link #getFQANs()} method. When VOMS validation fails, no attributes are returned. 
 *   
 * @author andreaceccanti
 *
 */
public class VOMSSecurityContext extends SecurityContext implements VOMSSecurityInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static ThreadLocal<VOMSSecurityContext> theSecurityContexts = new ThreadLocal<VOMSSecurityContext>();
	
	private VOMSValidator validator;
	
	
	@Override
	/**
	 * Sets the client cert chain and triggers VOMS attribute extraction and validation
	 */
	public void setClientCertChain(X509Certificate[] certChain) {
		super.setClientCertChain(certChain);
		
		if (validator == null)
			validator = new VOMSValidator(certChain);
		else
			validator.setClientChain(certChain);
		
		validator.validate();
		
	}
	
	/**
	 * Returns the {@link VOMSSecurityContext} associated to the current thread
	 * 
	 * @return the {@link VOMSSecurityContext} associated to the current thread
	 */
	public static VOMSSecurityContext getCurrentContext(){
		return theSecurityContexts.get();
	}
	
	/**
	 * Sets the {@link VOMSSecurityContext} associated to the current thread
	 * 
	 * @param ctxt the {@link VOMSSecurityContext} that will be associated to the current thread
	 */
	public static void setCurrentContext(VOMSSecurityContext ctxt){
		
		theSecurityContexts.set(ctxt);
	}
	
	/**
	 * Clears the {@link VOMSSecurityContext} associated to the current thread
	 */
	public static void clearCurrentContext(){
		theSecurityContexts.set(null);
	}


	/**
	 * Returns the {@link VOMSValidator} used to validate VOMS attributes extracted from client certificate chains
	 * 
	 * @return the VOMS validator used to validate VOMS attributes
	 */
	public VOMSValidator getValidator() {
		return validator;
	}

	/**
	 * Sets the {@link VOMSValidator} used to validate VOMS attributes extracted from client certificate chains
	 * 
	 * @param validator the VOMS validator that will be used to validate VOMS attributes
	 */
	public void setValidator(VOMSValidator validator) {
		this.validator = validator;
	}

	public String[] getFQANs() {
		
		return validator.getAllFullyQualifiedAttributes();
	}
	
}
