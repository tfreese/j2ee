// Erzeugt: 12.11.2015
package de.freese.jpa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import de.freese.jpa.model.Address;
import de.freese.jpa.model.Person;

/**
 * @author Thomas Freese
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestHibernate extends AbstractTest
{
    /**
     *
     */
    private static SessionFactory SESSIONFACTORY = null;

    /**
     *
     */
    @AfterClass
    public static void afterClass()
    {
        SESSIONFACTORY.close();
    }

    /**
     *
     */
    @BeforeClass
    public static void beforeClass()
    {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        Configuration config = new Configuration();
        config.addProperties(getHibernateProperties());

        config.addAnnotatedClass(Person.class).addAnnotatedClass(Address.class);
        config.addResource("META-INF/orm.xml");

        // configures settings from hibernate.cfg.xml
        // StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();

        try
        {
            SESSIONFACTORY = config.buildSessionFactory(registry);
        }
        catch (Exception ex)
        {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);

            LOGGER.error(null, ex);
        }
    }

    /**
     *
     */
    public TestHibernate()
    {
        super();
    }

    /**
     *
     */
    @After
    public void afterMethod()
    {
        // TODO
    }

    /**
     *
     */
    @Before
    public void beforeMethod()
    {
        // TODO
    }

    /**
     * @see de.freese.jpa.AbstractTest#test1Insert()
     */
    @Override
    @Test
    public void test1Insert()
    {
        try (Session session = SESSIONFACTORY.openSession())
        {
            session.beginTransaction();

            List<Person> persons = createPersons();

            persons.stream().map(person -> {
                session.save(person);
                return person;
            }).forEach(person -> {
                person.getAddresses().forEach(address -> {
                    session.save(address);
                });
            });

            validateTest1Insert(persons);

            session.getTransaction().commit();
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test2SelectAll()
     */
    @SuppressWarnings("unchecked")
    @Override
    @Test
    public void test2SelectAll()
    {
        try (Session session = SESSIONFACTORY.openSession())
        {
            // session.beginTransaction();

            Query<Person> query = null;
            // Caching aktiviert in Person Definition
            query = session.getNamedQuery("allPersons");
            // Caching muss explizit aktiviert werden
            // query = session.createQuery("from Person order by id asc");
            // query.setCacheable(true).setCacheRegion("person");

            List<Person> persons = query.getResultList();

            validateTest2SelectAll(persons);

            // session.getTransaction().commit();
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test3SelectVorname()
     */
    @Override
    @Test
    public void test3SelectVorname()
    {
        String vorname = "Vorname1";

        try (Session session = SESSIONFACTORY.openSession())
        {
            // session.beginTransaction();

            // Caching aktiviert in Person Definition
            Query<?> query = null;
            query = session.getNamedQuery("personByVorname");
            // Caching muss explizit aktiviert werden
            // query = session.createQuery("from Person where vorname=:vorname order by name asc");
            // query.setCacheable(true).setCacheRegion("person");

            query.setParameter("vorname", vorname);

            Person person = (Person) query.getSingleResult();

            validateTest3SelectVorname(Arrays.asList(person), vorname);

            // session.getTransaction().commit();
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test4NativeQuery()
     */
    @SuppressWarnings("unchecked")
    @Override
    @Test
    public void test4NativeQuery()
    {
        List<Person> persons = new ArrayList<>();

        try (Session session = SESSIONFACTORY.openSession())
        {
            // session.beginTransaction();

            // !!! Aliase funktionieren bei Native-Queries ohne Mappingobjekt nicht !!!
            // !!! Kein Caching bei Named-Queries !!!
            Query<Object[]> query = session.getNamedQuery("allPersons.native");
            // query.addScalar("id", LongType.INSTANCE).addScalar("name", StringType.INSTANCE).addScalar("vorname", StringType.INSTANCE);
            // query.setCacheable(true).setCacheRegion("person");

            List<Object[]> rows = query.getResultList();
            rows.forEach(row -> {
                Person person = new Person((String) row[1], (String) row[2]);
                person.setID(((BigInteger) row[0]).longValue());

                persons.add(person);
            });

            NativeQuery<Object[]> nativeQuery = session.createNativeQuery("select id, street from T_ADDRESS where person_id = :person_id order by street desc");
            nativeQuery.addScalar("id", LongType.INSTANCE).addScalar("street", StringType.INSTANCE);
            nativeQuery.setCacheable(true).setCacheRegion("address");

            persons.forEach(person -> {
                nativeQuery.setParameter("person_id", person.getID());
                List<Object[]> addresses = nativeQuery.getResultList();

                addresses.forEach(value -> {
                    Address address = new Address((String) value[1]);
                    address.setID((long) value[0]);

                    person.addAddress(address);
                });
            });

            validateTest2SelectAll(persons);

            // session.getTransaction().commit();
        }
    }

    /**
     *
     */
    @Test
    public void test99Statistics()
    {
        dumpStatistics(System.out, SESSIONFACTORY);
    }
}
