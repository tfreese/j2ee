/**
 * Created: 14.12.2012
 */

package de.freese.agentportal.server.web.view;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.service.ISecretNewsService;

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
