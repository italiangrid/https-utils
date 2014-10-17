package org.italiangrid.utils.voms;

import java.security.cert.X509Certificate;
import java.util.List;

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.ac.VOMSACValidator;
import org.italiangrid.voms.ac.VOMSValidationResult;

/**
 * A class representing an X.509 authentication context augmented with VOMS
 * attributes.
 * 
 * Attributes can then be accessed via the {@link #getVOMSAttributes()} method.
 * When VOMS validation fails, no attributes are returned.
 * 
 * @author andreaceccanti
 *
 */
public class VOMSSecurityContextImpl extends SecurityContextImpl implements
  VOMSSecurityContext {

  private final VOMSACValidator validator;
  private final boolean secure;
  private List<VOMSAttribute> vomsAttributes;

  public VOMSSecurityContextImpl(VOMSACValidator validator) {

    this.validator = validator;
    secure = true;
  }

  public VOMSSecurityContextImpl(VOMSACValidator validator, boolean secure) {

    this.validator = validator;
    this.secure = secure;
  }

  @Override
  public void setClientCertChain(X509Certificate[] clientCertChain) {

    super.setClientCertChain(clientCertChain);
    if (!secure)
      vomsAttributes = validator.parse(clientCertChain);
    else
      vomsAttributes = validator.validate(clientCertChain);
  }

  @Override
  public VOMSACValidator getValidator() {

    return validator;
  }

  @Override
  public void setValidator(VOMSACValidator validator) {

    throw new UnsupportedOperationException(
      "This implementation does not allow setting a validator after "
        + "the context has been constructed.");
  }

  @Override
  public List<VOMSAttribute> getVOMSAttributes() {

    return vomsAttributes;
  }

  @Override
  public List<VOMSValidationResult> getValidationResults() {

    return validator.validateWithResult(getClientCertChain());
  }

}
