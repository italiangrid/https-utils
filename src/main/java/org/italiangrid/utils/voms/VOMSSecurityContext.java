package org.italiangrid.utils.voms;

import java.util.List;

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.ac.VOMSACValidator;
import org.italiangrid.voms.ac.VOMSValidationResult;

/**
 * Provides access to VOMS attributes.
 * 
 * @author andreaceccanti
 *
 */
public interface VOMSSecurityContext extends SecurityContext {

  /**
   * Returns a possibly empty list of VOMS attributes extracted from the cert
   * chain set with the
   * {@link #setClientCertChain(java.security.cert.X509Certificate[])} method.
   * 
   * The attributes are validated using the validator specified with the
   * {@link #setValidator(VOMSACValidator)} method, or be provided by the
   * implementation in other ways.
   * 
   * @return a possibly empty list of {@link VOMSAttribute}
   * 
   */
  public List<VOMSAttribute> getVOMSAttributes();

  /**
   * Returns a possibly empty list of {@link VOMSValidationResult} objects
   * describing the outcome of VOMS validations on the attributes extracted from
   * the cert chain set with the
   * {@link #setClientCertChain(java.security.cert.X509Certificate[])} method.
   * 
   * The attributes are validated using the validator specified with the
   * {@link #setValidator(VOMSACValidator)} method, or provided by the
   * implementation in other ways.
   * 
   * @return a possibly empty list of {@link VOMSValidationResult}
   */
  public List<VOMSValidationResult> getValidationResults();

  /**
   * Sets the validator to be used for extracting and validating VOMS attributes
   * from the client cert chain.
   * 
   * @param validator
   *          the {@link VOMSACValidator}
   */
  public void setValidator(VOMSACValidator validator);

  /**
   * Returns the validator used for extracting and validating VOMS Attributes
   * from the client cert chain.
   * 
   * @return
   */
  public VOMSACValidator getValidator();

}
