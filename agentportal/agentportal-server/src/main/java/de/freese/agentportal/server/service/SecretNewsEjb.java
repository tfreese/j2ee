package de.freese.agentportal.server.service;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.service.ISecretNewsService;
import de.freese.agentportal.server.cdi.AgentPortalEm;
import de.freese.agentportal.server.dao.SecretNewsHighDao;
import de.freese.agentportal.server.dao.SecretNewsLowDao;
import jakarta.ejb.EJB;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
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
public class SecretNewsEjb implements ISecretNewsService
{
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SecretNewsEjb.class);
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
    private SecretNewsHighDao daoHigh;
    // private ISecretNewsDao daoHigh;
    /**
     *
     */
    // @Inject
    // @Unsecured
    // private SecretNewsLowDao daoLow = null;
    @EJB
    private SecretNewsLowDao daoLow;
    /**
     *
     */
    @Inject
    @AgentPortalEm
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

        return this.daoHigh.getNews();
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

        return this.daoLow.getNews();
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
