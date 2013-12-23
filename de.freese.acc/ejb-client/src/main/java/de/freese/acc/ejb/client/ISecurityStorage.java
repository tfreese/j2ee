package de.freese.acc.ejb.client;

import de.freese.acc.model.Security;
import java.util.List;

/**
 * @author Thomas Freese
 */
public interface ISecurityStorage
{
	/**
	 * @return {@link List}
	 */
	public List<Security> getAllSecurities();
}
