package de.freese.agentportal.server.service;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.model.SecretNewsList;
import de.freese.agentportal.server.cdi.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Stateless
@Path("/news")
// (name="Example", mappedName="ejb/SimpleBeanJNDI")
// @SecurityDomain("AgentPortalDomain")
// @RolesAllowed(
// {
// "AgentPortalRoleHigh", "AgentPortalRoleLow"
// })
public class SecretNewsRest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SecretNewsRest.class);

    @Context
    private SecurityContext context;

    @PersistenceContext(unitName = Resources.EM_UNIT)
    private EntityManager em;

    /**
     * curl -X DELETE -H "Accept: application/json" localhost:8080/secretnews/rest/news/3
     */
    @DELETE
    @Path("/{id:\\d+}")
    // Nur Zahlen erlaubt.
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(@PathParam("id") final long id)
    {
        LOGGER.info("");
        logCallerInfo();

        Query query = this.em.createQuery("delete from SecretNews where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    /**
     * curl -X PUT -H "Accept: application/json" localhost:8080/secretnews/rest/news?title=" + title + "&text=" + text
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insert(@QueryParam("title") final String title, @QueryParam("text") final String text)
    {
        LOGGER.info("");
        logCallerInfo();

        SecretNews entity = new SecretNews();
        entity.setSecurityLevel(SecretNews.SECURITY_LEVEL_LOW);
        entity.setTitle(title);
        entity.setTimestamp(new Date());
        entity.setText(text);

        this.em.persist(entity);
    }

    /**
     * curl -X GET -H "Accept: text/plain" localhost:8080/secretnews/rest/news
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    // @RolesAllowed("AgentPortalRoleHigh")
    public String newsAsString()
    {
        LOGGER.info("");
        logCallerInfo();

        List<SecretNews> news = newsAsXML().getNews();

        StringBuilder sb = new StringBuilder();

        for (SecretNews entity : news)
        {
            sb.append(entity);
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * curl -X GET -H "Accept: application/json" localhost:8080/secretnews/rest/news
     */
    @SuppressWarnings("unchecked")
    @GET
    @Produces(
            {
                    MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
            })
    // @RolesAllowed("AgentPortalRoleHigh")
    public SecretNewsList newsAsXML()
    {
        LOGGER.info("");
        logCallerInfo();

        Query query = this.em.createQuery("select sn from SecretNews sn");

        List<SecretNews> news = query.getResultList();

        SecretNewsList secretNewsList = new SecretNewsList();
        secretNewsList.setNews(news);

        return secretNewsList;
    }

    /**
     * curl -X GET -H "Accept: application/json" localhost:8080/secretnews/rest/news/3
     */
    @GET
    @Produces(
            {
                    MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
            })
    @Path("/{id:\\d+}")
    // Nur Zahlen erlaubt.
    // @RolesAllowed("AgentPortalRoleHigh")
    public SecretNews newsAsXML(@PathParam("id") final long id)
    {
        LOGGER.info("");
        logCallerInfo();

        return this.em.find(SecretNews.class, id);
    }

    /**
     * curl -X POST -H "Accept: application/json" localhost:8080/secretnews/rest/news/<br>
     * In den OutputStream: <secretNews id=\"1\"><title>CCC</title><text>DDD</text></secretNews>
     */
    @POST
    @Consumes(
            {
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
            })
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String update(final SecretNews news)
    {
        LOGGER.info("");
        logCallerInfo();

        if (news.getTimestamp() != null)
        {
            news.setTimestamp(new Date());
        }

        this.em.merge(news);

        return "OK";
    }

    private void logCallerInfo()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("user=");

        try
        {
            sb.append(this.context.getUserPrincipal().getName());
            sb.append(", role=");

            if (this.context.isUserInRole("AgentPortalRoleHigh"))
            {
                sb.append("AgentPortalRoleHigh");
            }
            else if (this.context.isUserInRole("AgentPortalRoleLow"))
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
