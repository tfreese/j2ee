/**
 * Created: 14.12.2012
 */

package de.freese.agentportal.server.web.view;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.service.ISecretNewsService;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * @author Thomas Freese
 */
@Named
@RequestScoped
public class SecretNewsLowView
{
	/**
	 *  
	 */
	@EJB
	private ISecretNewsService secretNewsService = null;

	/**
	 * Erstellt ein neues {@link SecretNewsLowView} Object.
	 */
	public SecretNewsLowView()
	{
		super();
	}

	/**
	 * @return {@link List}
	 */
	public List<SecretNews> getAllNews()
	{
		return this.secretNewsService.getAllSecretNews4Low();
	}

	/**
	 * @return String
	 */
	public String getLevel()
	{
		return "Low Security Level";
	}
}
