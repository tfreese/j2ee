/**
 * Created: 09.06.2012
 */

package de.freese.spring.server.ws;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

/**
 * @author Thomas Freese
 */
public class HolidayEndpointInterceptor implements EndpointInterceptor
{
	/**
	 * Erstellt ein neues {@link HolidayEndpointInterceptor} Object.
	 */
	public HolidayEndpointInterceptor()
	{
		super();
	}

	/**
	 * @see org.springframework.ws.server.EndpointInterceptor#afterCompletion(org.springframework.ws.context.MessageContext,
	 *      java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(final MessageContext messageContext, final Object endpoint,
								final Exception ex) throws Exception
	{
		// Empty
	}

	/**
	 * @see org.springframework.ws.server.EndpointInterceptor#handleFault(org.springframework.ws.context.MessageContext,
	 *      java.lang.Object)
	 */
	@Override
	public boolean handleFault(final MessageContext messageContext, final Object endpoint)
		throws Exception
	{
		return true;
	}

	/**
	 * @see org.springframework.ws.server.EndpointInterceptor#handleRequest(org.springframework.ws.context.MessageContext,
	 *      java.lang.Object)
	 */
	@Override
	public boolean handleRequest(final MessageContext messageContext, final Object endpoint)
		throws Exception
	{
		return true;
	}

	/**
	 * @see org.springframework.ws.server.EndpointInterceptor#handleResponse(org.springframework.ws.context.MessageContext,
	 *      java.lang.Object)
	 */
	@Override
	public boolean handleResponse(final MessageContext messageContext, final Object endpoint)
		throws Exception
	{
		return true;
	}
}
