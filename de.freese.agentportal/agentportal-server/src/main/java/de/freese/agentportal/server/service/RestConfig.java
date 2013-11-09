/**
 * 
 */
package de.freese.agentportal.server.service;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author Thomas Freese
 */
@ApplicationPath("/rest")
public class RestConfig extends Application
{
	/**
	 * 
	 */
	public RestConfig()
	{
		super();
	}
}
