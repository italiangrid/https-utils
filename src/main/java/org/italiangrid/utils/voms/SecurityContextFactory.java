package org.italiangrid.utils.voms;

import org.italiangrid.voms.ac.VOMSACValidator;


public class SecurityContextFactory {

	private SecurityContextFactory() {}
	
	public static SecurityContext newSecurityContext(){
		return new SecurityContextImpl();
	}
	
	public static VOMSSecurityContext newVOMSSecurityContext(VOMSACValidator validator){
		return new VOMSSecurityContextImpl(validator);
	}

}
