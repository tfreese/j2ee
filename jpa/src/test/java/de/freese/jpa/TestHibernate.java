// Created: 12.11.2015
package de.freese.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
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
import org.hibernate.query.TupleTransformer;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.freese.jpa.model.Address;
import de.freese.jpa.model.MyProjectionVo;
import de.freese.jpa.model.Person;

/**
 * @author Thomas Freese
 */
class TestHibernate extends AbstractTest {
    private static SessionFactory sessionFactory;

    @AfterAll
    static void afterAll() {
        sessionFactory.close();
    }

    @BeforeAll
    static void beforeAll() {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        final Map<String, Object> config = getHibernateConfig();
        final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config).build();

        final MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addPackage("de.freese.jpa.model") // for package-info.java
                .addAnnotatedClass(Person.class).addAnnotatedClass(Address.class);
        //        .addResource("META-INF/orm.xml");

        try {
            final Metadata metadata = metadataSources.buildMetadata();
            sessionFactory = metadata.buildSessionFactory();
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

            final List<Person> persons = createPersons();

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
            // final List<Person> persons = session.createQuery("from Person order by id asc")
            // .setCacheable(true).setCacheRegion("person").getResultList();

            // Caching is enabled in Mapping.
            final List<Person> persons = session.createNamedQuery("allPersons", Person.class).getResultList();

            validateTest2SelectAll(persons);

            // session.getTransaction().commit();
        }
    }

    @Override
    @Test
    public void test030SelectVorname() {
        final String vorname = "Vorname1";

        try (Session session = sessionFactory.openSession()) {
            // session.beginTransaction();

            // Caching must be enabled explicitly.
            // final Person person = session.createQuery("from Person where vorname=:vorname order by name asc")
            // .setCacheable(true).setCacheRegion("person").getSingleResult();

            // Caching is enabled in Mapping.
            final Person person = session.createNamedQuery("personByVorname", Person.class).setParameter("vorname", vorname).getSingleResult();

            validateTest3SelectVorname(Arrays.asList(person), vorname);

            // session.getTransaction().commit();
        }
    }

    @Override
    @Test
    public void test040NativeQuery() {
        try (Session session = sessionFactory.openSession()) {
            // session.beginTransaction();

            // !!! Aliases won't work in Native-Queries without Mapping object !!!
            // !!! No Caching for Named-Queries !!!
            // query.setCacheable(true).setCacheRegion("person");
            final Query<Person> query = session.createNamedQuery("allPersons.native", Object[].class).setTupleTransformer((tuple, aliases) -> {
                final Person person = Person.of((String) tuple[1], (String) tuple[2]);
                person.setID((long) tuple[0]);

                return person;
            });

            // Force flush on T_PERSON, so the NativeQuery can access the cached Data from the Session.
            final List<Person> persons = query.getResultList();
            // final List<Person> persons = query.unwrap(NativeQuery.class).addSynchronizedQuerySpace("T_PERSON").getResultList();

            assertNotNull(persons);
            assertFalse(persons.isEmpty());

            final String sql = "select id, street from T_ADDRESS where person_id = :personId order by street desc";

            final TupleTransformer<Address> addressTupleTransformer = (tuple, aliases) -> {
                final Address address = Address.of((String) tuple[1]);
                address.setID((long) tuple[0]);

                return address;
            };

            // nativeQuery2.addScalar("id", StandardBasicTypes.LONG).addScalar("street", StandardBasicTypes.STRING);
            // nativeQuery2.setCacheable(true).setCacheRegion("address");
            final NativeQuery<Object[]> nativeQuery2 = session.createNativeQuery(sql, Object[].class);

            persons.forEach(person -> {
                // nativeQuery2.setParameter("personId", person.getID())
                //         .setTupleTransformer(addressTupleTransformer)
                //         .getResultStream()
                //         // .map(row -> {
                //         //     final Address address = new Address((String) row[1]);
                //         //     address.setID((long) row[0]);
                //         //     return address;
                //         // })
                //         .forEach(person::addAddress);

                final List<Address> addresses = nativeQuery2.setParameter("personId", person.getID()).setTupleTransformer(addressTupleTransformer).getResultList();

                assertNotNull(addresses);
                assertFalse(addresses.isEmpty());

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
            final String hql = """
                    select
                        new de.freese.jpa.model.MyProjectionVo(
                        p.id,
                        p.name
                        )
                    from
                        Person p
                    order by p.name asc
                    """;

            final List<MyProjectionVo> result = session.createQuery(hql, MyProjectionVo.class).getResultList();

            assertNotNull(result);
            assertFalse(result.isEmpty());

            for (int i = 1; i <= result.size(); i++) {
                final MyProjectionVo dto = result.get(i - 1);

                assertEquals("Name" + i, dto.getName());
            }
        }
    }

    @Test
    void test099Statistics() {
        dumpStatistics(new PrintWriter(System.out), sessionFactory);

        assertTrue(true);
    }
}
