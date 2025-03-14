// Created: 10.05.2013
package de.freese.j2ee.rest;

import java.util.List;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.j2ee.interceptor.logging.MyLogging;
import de.freese.j2ee.model.Kunde;
import de.freese.j2ee.persistence.MyDataSource;
import de.freese.j2ee.persistence.MyEntityManager;

/**
 * @author Thomas Freese
 */
@Stateless
// (name="Example", mappedName="ejb/SimpleBeanJNDI")
@Path("/kunde")
public class RestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);

    @Context
    private SecurityContext context;
    @Inject
    @MyDataSource
    private DataSource dataSource;
    @Inject
    @MyEntityManager
    private EntityManager entityManager;
    @Resource
    private UserTransaction ut;

    @DELETE
    @Path("/{id:\\d+}")
    // Nur Zahlen erlaubt.
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @MyLogging
    public void delete(@PathParam("id") final long id) {
        LOGGER.info("id = {}", id);

        this.entityManager.createQuery("delete from Kunde where id = :id").setParameter("id", id).executeUpdate();
    }

    @GET
    @Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
    @MyLogging
    public List<Kunde> getAsXML() {
        LOGGER.info("");

        return this.entityManager.createQuery("select k from Kunde k", Kunde.class).getResultList();
    }

    @GET
    @Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
    @Path("/{id:\\d+}")
    // Nur Zahlen erlaubt.
    @MyLogging
    public Kunde getByID(@PathParam("id") final long id) {
        LOGGER.info("id = {}", id);

        final List<Kunde> kunden = this.entityManager.createQuery("select k from Kunde k where k.id = :id", Kunde.class).setParameter("id", id).getResultList();

        if (kunden.size() == 1) {
            return kunden.getFirst();
        }

        return null;
    }

    @PUT
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @MyLogging
    public void insertKunde(@QueryParam("name") final String name, @QueryParam("vorname") final String vorname) {
        LOGGER.info("name = {}, vorname = {}", name, vorname);

        final Kunde kunde = new Kunde();
        kunde.setName(name);
        kunde.setVorname(vorname);

        this.entityManager.persist(kunde);
    }

    @POST
    @Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @MyLogging
    public String update(final Kunde kunde) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(kunde.toString());
        }

        this.entityManager.merge(kunde);

        return "OK";
    }
}
