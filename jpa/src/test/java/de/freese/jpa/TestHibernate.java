// Created: 12.11.2015
package de.freese.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

import de.freese.jpa.model.Address;
import de.freese.jpa.model.MyProjectionVo;
import de.freese.jpa.model.Person;

/**
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestHibernate extends AbstractTest {
    private static SessionFactory sessionFactory;

    @AfterAll
    static void afterAll() {
        sessionFactory.close();
    }

    @BeforeAll
    static void beforeAll() {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        Map<String, Object> config = getHibernateConfig();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config).build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addPackage("de.freese.jpa.model") // for package-info.java
                .addAnnotatedClass(Person.class).addAnnotatedClass(Address.class);
        //        .addResource("META-INF/orm.xml");

        try {
            Metadata metadata = metadataSources.buildMetadata();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        }
        catch (Exception ex) {
            // The registry would be destroyed by the SessionFactory, but we have trouble building the SessionFactory so destroy it manually.
            StandardServiceRegistryBuilder.destroy(serviceRegistry);

            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @Override
    @Test
    public void test010Insert() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Person> persons = createPersons();

            persons.stream().map(person -> {
                session.persist(person);
                return person;
            }).forEach(person -> {
                // person.getAddresses().forEach(address -> {
                // session.persist(address);
                // });
            });

            validateTest1Insert(persons);

            //            session.flush(); // without no flush -> no insert
            session.getTransaction().commit();
        }
    }

    @Override
    @Test
    public void test020SelectAll() {
        try (Session session = sessionFactory.openSession()) {
            // session.beginTransaction();

            // Caching must be enabled explicitly.
            // List<Person> persons = session.createQuery("from Person order by id asc")
            // .setCacheable(true).setCacheRegion("person").getResultList();

            // Caching is enabled in Mapping.
            List<Person> persons = session.createNamedQuery("allPersons", Person.class).getResultList();

            validateTest2SelectAll(persons);

            // session.getTransaction().commit();
        }
    }

    @Override
    @Test
    public void test030SelectVorname() {
        String vorname = "Vorname1";

        try (Session session = sessionFactory.openSession()) {
            // session.beginTransaction();

            // Caching must be enabled explicitly.
            // Person person = session.createQuery("from Person where vorname=:vorname order by name asc")
            // .setCacheable(true).setCacheRegion("person").getSingleResult();

            // Caching is enabled in Mapping.
            Person person = session.createNamedQuery("personByVorname", Person.class).setParameter("vorname", vorname).getSingleResult();

            validateTest3SelectVorname(Arrays.asList(person), vorname);

            // session.getTransaction().commit();
        }
    }

    @Override
    @Test
    @Disabled("Strange Error Message in Address call: 'Unable to find column position by name: PERSON_ID'")
    public void test040NativeQuery() {
        try (Session session = sessionFactory.openSession()) {
            // session.beginTransaction();

            // !!! Aliases won't work in Native-Queries without Mapping object !!!
            // !!! No Caching for Named-Queries !!!
            // query.setCacheable(true).setCacheRegion("person");
            Query<Person> query = session.createNamedQuery("allPersons.native", Object[].class).setTupleTransformer((tuple, aliases) -> {
                Person person = new Person((String) tuple[1], (String) tuple[2]);
                person.setID((long) tuple[0]);

                return person;
            });

            // Force flush on T_PERSON, so the NativeQuery can access the cached Data from the Session.
            List<Person> persons = query.getResultList();
            //            List<Person> persons = query.unwrap(NativeQuery.class).addSynchronizedQuerySpace("T_PERSON").getResultList();

            assertNotNull(persons);
            assertEquals(3, persons.size());

            //            nativeQuery2.addScalar("id", StandardBasicTypes.LONG).addScalar("street", StandardBasicTypes.STRING);
            // nativeQuery2.setCacheable(true).setCacheRegion("address");
            String sql = "select id, street from T_ADDRESS where person_id = :personId order by street desc";
            NativeQuery<Address> nativeQuery2 = session.createNativeQuery(sql, Address.class).setTupleTransformer((tuple, aliases) -> {
                Address address = new Address((String) tuple[1]);
                address.setID((long) tuple[0]);

                return address;
            });

            persons.forEach(person -> {
                List<Address> addresses = nativeQuery2.setParameter("personId", person.getID(), Long.class).getResultList();

                addresses.forEach(address -> {
                    person.addAddress(address);
                });
            });

            validateTest2SelectAll(persons);

            // session.getTransaction().commit();
        }
    }

    @Test
    void test060Projection() {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder();
            hql.append("select");
            hql.append(" new de.freese.jpa.model.MyProjectionVo");
            hql.append(" (");
            hql.append("p.id");
            hql.append(", p.name");
            hql.append(")");
            hql.append(" from Person p");
            hql.append(" order by p.name asc");

            List<MyProjectionVo> result = session.createQuery(hql.toString(), MyProjectionVo.class).getResultList();

            assertNotNull(result);
            assertFalse(result.isEmpty());

            for (int i = 1; i <= result.size(); i++) {
                MyProjectionVo dto = result.get(i - 1);

                assertEquals("Name" + i, dto.getName());
            }
        }
    }

    @Test
    void test099Statistics() {
        dumpStatistics(System.out, sessionFactory);

        assertTrue(true);
    }
}
