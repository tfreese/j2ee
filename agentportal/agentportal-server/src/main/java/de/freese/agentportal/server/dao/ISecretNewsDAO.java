/**
 * Created: 14.12.2012
 */

package de.freese.agentportal.server.dao;

import de.freese.agentportal.common.model.SecretNews;
import java.util.List;
import javax.ejb.Local;

/**
 * @author Thomas Freese
 */
@Local
public interface ISecretNewsDAO
{
	/**
	 * @param id long
	 * @return {@link SecretNews}
	 */
	public SecretNews get(long id);

	/**
	 * @return {@link List}
	 */
	public List<SecretNews> getNews();
}
