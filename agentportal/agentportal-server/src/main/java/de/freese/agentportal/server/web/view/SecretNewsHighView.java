/**
 * Created: 14.12.2012
 */

package de.freese.agentportal.server.web.view;

import java.util.List;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.service.ISecretNewsService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 * @author Thomas Freese
 */
@Named
@RequestScoped
public class SecretNewsHighView
{
    /**
     *
     */
    @EJB
    private final ISecretNewsService secretNewsService = null;

    /**
     * @return {@link List}
     */
    public List<SecretNews> getAllNews()
    {
        return this.secretNewsService.getAllSecretNews4High();
    }

    /**
     * @return String
     */
    public String getLevel()
    {
        return "High Security Level";
    }
}
