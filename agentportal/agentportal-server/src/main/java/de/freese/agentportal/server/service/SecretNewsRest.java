package de.freese.agentportal.server.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.model.SecretNewsList;
import de.freese.agentportal.server.cdi.Resources;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
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
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SecretNewsRest.class);
    /**
     *
     */
    @Context
    private SecurityContext context;
    /**
     *
     */
    @PersistenceContext(unitName = Resources.EM_UNIT)
    private EntityManager em;

    /**
     * curl -X DELETE -H "Accept: application/json" localhost:8080/secretnews/rest/news/3
     *
     * @param id long
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
     *
     * @param title String
     * @param text String
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
     *
     * @return {@link List}
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
     *
     * @return {@link List}
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
     *
     * @param id long
     *
     * @return {@link SecretNews}
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
     *
     * @param news {@link SecretNews}
     *
     * @return String
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

    /**
     *
     */
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
