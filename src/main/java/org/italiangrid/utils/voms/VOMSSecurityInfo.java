package org.italiangrid.utils.voms;


/**
 * An interface providing access to VOMS attributes.
 * 
 * @author andreaceccanti
 *
 */
public interface VOMSSecurityInfo {
	
	/**
	 * Returns an array of string representation of valid VOMS Fully Qualified Attribute Names.
	 * 
	 * @return a possibly empty array of VOMS FQANs 
	 */
	String[] getFQANs();

}
