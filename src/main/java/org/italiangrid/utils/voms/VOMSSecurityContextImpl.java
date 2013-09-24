package org.italiangrid.utils.voms;

import java.util.List;

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.ac.VOMSACValidator;
import org.italiangrid.voms.ac.VOMSValidationResult;

/**
 * A class representing an X.509 authentication context augmented with VOMS attributes.
 * 
 * Attributes can then be accessed via the {@link #getVOMSAttributes()} method. 
 * When VOMS validation fails, no attributes are returned. 
 *   
 * @author andreaceccanti
 *
 */
public class VOMSSecurityContextImpl extends SecurityContextImpl 
implements VOMSSecurityContext{
	
	private VOMSACValidator validator;
	
	public VOMSSecurityContextImpl(VOMSACValidator validator) {
		this.validator = validator;
	}
	
	
	@Override
	public VOMSACValidator getValidator() {
		return validator;
	}

	@Override
	public void setValidator(VOMSACValidator validator) {
		this.validator = validator;
	}

	@Override
	public List<VOMSAttribute> getVOMSAttributes() {
		return validator.validate(getClientCertChain());
	}

	@Override
	public List<VOMSValidationResult> getValidationResults() {
		return validator.validateWithResult(getClientCertChain());
	}
	
}
