package de.freese.agentportal.server.service;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.service.ISecretNewsService;
import de.freese.agentportal.server.cdi.AgentPortalEM;
import de.freese.agentportal.server.dao.SecretNewsHighDAO;
import de.freese.agentportal.server.dao.SecretNewsLowDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Stateless
@Remote(ISecretNewsService.class)
// @DeclareRoles(
// @RolesAllowed(
// {
// "AgentPortalRoleHigh", "AgentPortalRoleLow"
// })
public class SecretNewsEJB implements ISecretNewsService
{
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SecretNewsEJB.class);
    /**
     *
     */
    @Resource
    private SessionContext context;
    /**
     *
     */
    // @Inject
    // @Secured
    @EJB
    // (mappedName = "secureNewsDAO")
    private SecretNewsHighDAO daoHigh;
    // private ISecretNewsDAO daoHigh;
    /**
     *
     */
    // @Inject
    // @Unsecured
    // private SecretNewsLowDAO daoLow = null;
    @EJB
    private SecretNewsLowDAO daoLow;
    /**
     *
     */
    @Inject
    @AgentPortalEM
    private EntityManager entityManager1;

    /**
     * @see de.freese.agentportal.common.service.ISecretNewsService#getAllSecretNews4High()
     */
    @Override
    // @RolesAllowed("AgentPortalRoleHigh")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<SecretNews> getAllSecretNews4High()
    {
        LOGGER.info("");
        logCallerInfo();

        List<SecretNews> news = this.daoHigh.getNews();

        return news;
    }

    /**
     * @see de.freese.agentportal.common.service.ISecretNewsService#getAllSecretNews4Low()
     */
    @Override
    // @RolesAllowed(
    // {
    // "AgentPortalRoleHigh", "AgentPortalRoleLow"
    // })
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<SecretNews> getAllSecretNews4Low()
    {
        LOGGER.info("");
        logCallerInfo();

        List<SecretNews> news = this.daoLow.getNews();

        return news;
    }

    /**
     *
     */
    protected void logCallerInfo()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("user=");

        try
        {
            sb.append(this.context.getCallerPrincipal().getName());
            sb.append(", role=");

            if (this.context.isCallerInRole("AgentPortalRoleHigh"))
            {
                sb.append("AgentPortalRoleHigh");
            }
            else if (this.context.isCallerInRole("AgentPortalRoleLow"))
            {
                sb.append("AgentPortalRoleLow");
            }
            else
            {
                sb.append("<unknown>");
            }
        }
        catch (Exception ex)
        {
            sb.append("<unknown>");
        }

        LOGGER.info("called from {}", sb);
    }
}
