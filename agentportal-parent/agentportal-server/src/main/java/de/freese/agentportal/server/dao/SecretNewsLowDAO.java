/**
 * Created: 14.12.2012
 */

package de.freese.agentportal.server.dao;

import de.freese.agentportal.common.model.SecretNews;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * @author Thomas Freese
 */
@Stateless
@LocalBean
// @Unsecured
public class SecretNewsLowDAO extends AbstractSecretNewsDAO
{
	/**
	 * Erstellt ein neues {@link SecretNewsLowDAO} Object.
	 */
	public SecretNewsLowDAO()
	{
		super();
	}

	/**
	 * @see de.freese.agentportal.server.dao.AbstractSecretNewsDAO#getSecurityLevel()
	 */
	@Override
	protected int getSecurityLevel()
	{
		return SecretNews.SECURITY_LEVEL_LOW;
	}
}
