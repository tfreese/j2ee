// Created: 10.05.2013
package de.freese.j2ee.rest;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.j2ee.interceptor.MyLogging;
import de.freese.j2ee.model.Kunde;
import de.freese.j2ee.persistence.MyDataSource;
import de.freese.j2ee.persistence.MyEntityManager;

/**
 * @author Thomas Freese
 */
@Stateless
// (name="Example", mappedName="ejb/SimpleBeanJNDI")
@Path("/kunde")
public class RestService
{
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);
    /**
     *
     */
    @Context
    private SecurityContext context;
    /**
     *
     */
    @Inject
    @MyDataSource
    private DataSource dataSource;
    /**
     *
     */
    @Inject
    @MyEntityManager
    private EntityManager entityManager;
    /**
     *
     */
    @Resource
    private UserTransaction ut;

    /**
     * @param id long
     */
    @DELETE
    @Path("/{id:\\d+}")
    // Nur Zahlen erlaubt.
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @MyLogging
    public void delete(@PathParam("id") final long id)
    {
        LOGGER.info("id = {}", id);

        Query query = this.entityManager.createQuery("delete from Kunde where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    /**
     * @return {@link List}
     */
    @SuppressWarnings("unchecked")
    @GET
    @Produces(
    {
            MediaType.TEXT_XML, MediaType.APPLICATION_JSON
    })
    @MyLogging
    public List<Kunde> getAsXML()
    {
        LOGGER.info("");

        Query query = this.entityManager.createQuery("select k from Kunde k");
        List<Kunde> kunden = query.getResultList();

        return kunden;
    }

    /**
     * @param id long
     *
     * @return {@link Kunde}
     */
    @SuppressWarnings("unchecked")
    @GET
    @Produces(
    {
            MediaType.TEXT_XML, MediaType.APPLICATION_JSON
    })
    @Path("/{id:\\d+}")
    // Nur Zahlen erlaubt.
    @MyLogging
    public Kunde getByID(@PathParam("id") final long id)
    {
        LOGGER.info("id = {}", id);

        Query query = this.entityManager.createQuery("select k from Kunde k where k.id = :id");
        query.setParameter("id", id);

        List<Kunde> kunden = query.getResultList();

        if (kunden.size() == 1)
        {
            return kunden.get(0);
        }

        return null;
    }

    /**
     * @param name String
     * @param vorname String
     */
    @PUT
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @MyLogging
    public void insertKunde(@QueryParam("name") final String name, @QueryParam("vorname") final String vorname)
    {
        LOGGER.info("name = {}, vorname = {}", name, vorname);

        Kunde kunde = new Kunde();
        kunde.setName(name);
        kunde.setVorname(vorname);

        this.entityManager.persist(kunde);
    }

    /**
     * @param kunde {@link Kunde}
     *
     * @return String
     */
    @POST
    @Consumes(
    {
            MediaType.TEXT_XML, MediaType.APPLICATION_JSON
    })
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @MyLogging
    public String update(final Kunde kunde)
    {
        if (LOGGER.isInfoEnabled())
        {
            LOGGER.info(kunde.toString());
        }

        this.entityManager.merge(kunde);

        return "OK";
    }
}
