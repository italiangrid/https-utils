package org.italiangrid.utils.voms;

/**
 * Thread local storage utility class for security contexts.
 * 
 * @author andreaceccanti
 *
 */
public class CurrentSecurityContext {

  /**
   * The thread local storage
   */
  private static ThreadLocal<SecurityContext> currentContext = new ThreadLocal<SecurityContext>();

  /**
   * Returns the current thread security context.
   * 
   * @return the current thread security context
   */
  public static SecurityContext get() {

    return currentContext.get();
  }

  /**
   * Sets the security context associated to the current thread.
   * 
   * @param ctxt
   *          the {@link SecurityContext}
   */
  public static void set(SecurityContext ctxt) {

    currentContext.set(ctxt);
  }

  /**
   * Clears the security context associated to the current thread.
   */
  public static void clear() {

    currentContext.set(null);
  }
}
