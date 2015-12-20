// Erzeugt: 12.11.2015
package de.freese.jpa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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

            for (Person person : persons)
            {
                session.save(person);

                for (Address address : person.getAddresses())
                {
                    session.save(address);
                }
            }

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

            Query query = null;
            // Caching aktiviert in Person Definition
            query = session.getNamedQuery("allPersons");
            // Caching muss explizit aktiviert werden
            // query = session.createQuery("from Person order by id asc");
            // query.setCacheable(true).setCacheRegion("person");

            List<Person> persons = query.list();

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

            Query query = null;
            // Caching aktiviert in Person Definition
            query = session.getNamedQuery("personByVorname");
            // Caching muss explizit aktiviert werden
            // query = session.createQuery("from Person where vorname=:vorname order by name asc");
            // query.setCacheable(true).setCacheRegion("person");

            query.setString("vorname", vorname);

            Person person = (Person) query.uniqueResult();

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
            Query query = session.getNamedQuery("allPersons.native");
            // query.addScalar("id", LongType.INSTANCE).addScalar("name", StringType.INSTANCE).addScalar("vorname", StringType.INSTANCE);
            // query.setCacheable(true).setCacheRegion("person");

            List<Object[]> rows = query.list();

            for (Object[] row : rows)
            {
                Person person = new Person((String) row[1], (String) row[2]);
                person.setID(((BigInteger) row[0]).longValue());

                persons.add(person);
            }

            SQLQuery sqlQuery = session.createSQLQuery("select id, street from T_ADDRESS where person_id = :person_id order by street desc");
            sqlQuery.addScalar("id", LongType.INSTANCE).addScalar("street", StringType.INSTANCE);
            sqlQuery.setCacheable(true).setCacheRegion("address");

            for (Person person : persons)
            {
                sqlQuery.setLong("person_id", person.getID());
                rows = sqlQuery.list();

                for (Object[] row : rows)
                {
                    Address address = new Address((String) row[1]);
                    address.setID((long) row[0]);

                    person.addAddress(address);
                }
            }

            validateTest2SelectAll(persons);

            // session.getTransaction().commit();
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test5ImportSQL()
     */
    @SuppressWarnings("unchecked")
    @Override
    @Test
    public void test5ImportSQL()
    {
        try (Session session = SESSIONFACTORY.openSession())
        {
            session.beginTransaction();

            SQLQuery sqlQuery = session.createSQLQuery("select * from roles order by id asc");
            List<Object[]> rows = sqlQuery.list();

            Assert.assertNotNull(rows);
            Assert.assertEquals(2, rows.size());
            Assert.assertEquals(1, rows.get(0)[0]);
            Assert.assertEquals("quickstarts", rows.get(0)[1]);

            session.getTransaction().commit();
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
