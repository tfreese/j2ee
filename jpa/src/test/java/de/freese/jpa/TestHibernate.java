// Created: 12.11.2015
package de.freese.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.freese.jpa.model.Address;
import de.freese.jpa.model.MyProjectionDTO;
import de.freese.jpa.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestHibernate extends AbstractTest
{
    private static SessionFactory sessionFactory;

    @AfterAll
    static void afterAll()
    {
        sessionFactory.close();
    }

    @BeforeAll
    static void beforeAll()
    {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        Map<String, Object> config = getHibernateConfig();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config).build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addAnnotatedClass(Person.class).addAnnotatedClass(Address.class);
        //        metadataSources.addResource("META-INF/orm.xml");

        try
        {
            Metadata metadata = metadataSources.buildMetadata();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        }
        catch (Exception ex)
        {
            // The registry would be destroyed by the SessionFactory, but we have trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(serviceRegistry);

            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test010Insert()
     */
    @Override
    @Test
    public void test010Insert()
    {
        try (Session session = sessionFactory.openSession())
        {
            session.beginTransaction();

            List<Person> persons = createPersons();

            persons.stream().map(person ->
            {
                session.persist(person);
                return person;
            }).forEach(person ->
            {
                // person.getAddresses().forEach(address -> {
                // session.persist(address);
                // });
            });

            validateTest1Insert(persons);

            session.flush(); // ohne flush kein insert
            session.getTransaction().commit();
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test020SelectAll()
     */
    @Override
    @Test
    public void test020SelectAll()
    {
        try (Session session = sessionFactory.openSession())
        {
            // session.beginTransaction();

            Query<Person> query;
            // Caching aktiviert in Person Definition
            query = session.createNamedQuery("allPersons", Person.class);
            // Caching muss explizit aktiviert werden
            // query = session.createQuery("from Person order by id asc");
            // query.setCacheable(true).setCacheRegion("person");

            List<Person> persons = query.getResultList();

            validateTest2SelectAll(persons);

            // session.getTransaction().commit();
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test030SelectVorname()
     */
    @Override
    @Test
    public void test030SelectVorname()
    {
        String vorname = "Vorname1";

        try (Session session = sessionFactory.openSession())
        {
            // session.beginTransaction();

            // Caching aktiviert in Person Definition
            Query<Person> query;
            query = session.createNamedQuery("personByVorname", Person.class);
            // Caching muss explizit aktiviert werden
            // query = session.createQuery("from Person where vorname=:vorname order by name asc");
            // query.setCacheable(true).setCacheRegion("person");

            query.setParameter("vorname", vorname);

            Person person = query.getSingleResult();

            validateTest3SelectVorname(Arrays.asList(person), vorname);

            // session.getTransaction().commit();
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test040NativeQuery()
     */
    @Override
    @Test
    @Disabled("Strange Error Message in Address call: 'Unable to find column position by name: PERSON_ID'")
    public void test040NativeQuery()
    {
        try (Session session = sessionFactory.openSession())
        {
            // session.beginTransaction();

            // !!! Aliase funktionieren bei Native-Queries ohne Mappingobjekt nicht !!!
            // !!! Kein Caching bei Named-Queries !!!
            // query.setCacheable(true).setCacheRegion("person");
            Query<Person> query = session.createNamedQuery("allPersons.native", Object[].class)
                    .setTupleTransformer((tuple, aliases) ->
                    {
                        Person person = new Person((String) tuple[1], (String) tuple[2]);
                        person.setID((long) tuple[0]);

                        return person;
                    });

            // Flush auf T_PERSON erzwingen, damit NativeQuery auch Daten aus der Session erwischt.
            NativeQuery<Person> nativeQuery1 = query.unwrap(NativeQuery.class);
            nativeQuery1.addSynchronizedQuerySpace("T_PERSON");

            List<Person> persons = nativeQuery1.getResultList();
            assertNotNull(persons);
            assertEquals(3, persons.size());

            //            nativeQuery2.addScalar("id", StandardBasicTypes.LONG).addScalar("street", StandardBasicTypes.STRING);
            // nativeQuery2.setCacheable(true).setCacheRegion("address");
            NativeQuery<Address> nativeQuery2 = session.createNativeQuery("select id, street from T_ADDRESS where person_id = :personId order by street desc", Address.class)
                    .setTupleTransformer((tuple, aliases) ->
                    {
                        Address address = new Address((String) tuple[1]);
                        address.setID((long) tuple[0]);

                        return address;
                    });

            persons.forEach(person ->
            {
                nativeQuery2.setParameter("personId", person.getID(), Long.class);

                List<Address> addresses = nativeQuery2.getResultList();

                addresses.forEach(address ->
                {
                    person.addAddress(address);
                });
            });

            validateTest2SelectAll(persons);

            // session.getTransaction().commit();
        }
    }

    @Test
    void test060Projection()
    {
        try (Session session = sessionFactory.openSession())
        {
            StringBuilder hql = new StringBuilder();
            hql.append("select");
            hql.append(" new de.freese.jpa.model.MyProjectionDTO(");
            hql.append("p.id");
            hql.append(", p.name");
            hql.append(")");
            hql.append(" from Person p");
            hql.append(" order by p.name asc");

            Query<MyProjectionDTO> query = session.createQuery(hql.toString(), MyProjectionDTO.class);
            List<MyProjectionDTO> result = query.getResultList();

            assertNotNull(result);
            assertFalse(result.isEmpty());

            for (int i = 1; i <= result.size(); i++)
            {
                MyProjectionDTO dto = result.get(i - 1);

                assertEquals("Name" + i, dto.getName());
            }
        }
    }

    @Test
    void test099Statistics()
    {
        dumpStatistics(System.out, sessionFactory);

        assertTrue(true);
    }
}
