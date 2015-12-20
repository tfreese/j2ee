/**
 * 
 */
package javaee.demo;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * Es wird der gesamte Request als Principal genommen.
 * 
 * @author Thomas Freese
 */
public class MyPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter
{
	/**
	 * 
	 */
	public MyPreAuthenticatedProcessingFilter()
	{
		super();
	}

	/**
	 * @see org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter#getPreAuthenticatedCredentials(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object getPreAuthenticatedCredentials(final HttpServletRequest request)
	{
		return "";
	}

	/**
	 * @see org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter#getPreAuthenticatedPrincipal(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object getPreAuthenticatedPrincipal(final HttpServletRequest request)
	{
		return request;
	}
}
