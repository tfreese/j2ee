// Created: 12.11.2015
package de.freese.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.freese.jpa.model.Address;
import de.freese.jpa.model.MyProjectionDTO;
import de.freese.jpa.model.Person;
import de.freese.sql.querydsl.TEmployee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestHibernate extends AbstractTest
{
    /**
     *
     */
    private static SessionFactory SESSIONFACTORY;

    /**
     *
     */
    @AfterAll
    static void afterAll()
    {
        SESSIONFACTORY.close();
    }

    /**
     *
     */
    @BeforeAll
    static void beforeAll()
    {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        Configuration config = new Configuration();
        config.addProperties(getHibernateProperties());

        config.addAnnotatedClass(Person.class).addAnnotatedClass(Address.class);
        // config.addResource("META-INF/orm.xml");

        // configures settings from hibernate.cfg.xml
        // StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();

        try
        {
            SESSIONFACTORY = config.buildSessionFactory(registry);
        }
        catch (Exception ex)
        {
            // The registry would be destroyed by the SessionFactory, but we have trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);

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
        try (Session session = SESSIONFACTORY.openSession())
        {
            session.beginTransaction();

            List<Person> persons = createPersons();

            persons.stream().map(person ->
            {
                session.save(person);
                return person;
            }).forEach(person ->
            {
                // person.getAddresses().forEach(address -> {
                // session.save(address);
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
    @SuppressWarnings("unchecked")
    @Override
    @Test
    public void test020SelectAll()
    {
        try (Session session = SESSIONFACTORY.openSession())
        {
            // session.beginTransaction();

            Query<Person> query;
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
     * @see de.freese.jpa.AbstractTest#test030SelectVorname()
     */
    @Override
    @Test
    public void test030SelectVorname()
    {
        String vorname = "Vorname1";

        try (Session session = SESSIONFACTORY.openSession())
        {
            // session.beginTransaction();

            // Caching aktiviert in Person Definition
            Query<?> query;
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
     * @see de.freese.jpa.AbstractTest#test040NativeQuery()
     */
    @SuppressWarnings("unchecked")
    @Override
    @Test
    public void test040NativeQuery()
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

            // Flush auf T_PERSON erzwingen, damit NativeQuery auch Daten aus der Session erwischt.
            NativeQuery<Object[]> nativeQuery1 = query.unwrap(NativeQuery.class);
            nativeQuery1.addSynchronizedQuerySpace("T_PERSON");

            List<Object[]> rows = nativeQuery1.getResultList();
            rows.forEach(row ->
            {
                Person person = new Person((String) row[1], (String) row[2]);
                person.setID(((BigInteger) row[0]).longValue());

                persons.add(person);
            });

            NativeQuery<Object[]> nativeQuery2 =
                    session.createNativeQuery("select id, street from T_ADDRESS where person_id = :person_id order by street desc");
            nativeQuery2.addScalar("id", LongType.INSTANCE).addScalar("street", StringType.INSTANCE);
            // nativeQuery2.setCacheable(true).setCacheRegion("address");

            persons.forEach(person ->
            {
                nativeQuery2.setParameter("person_id", person.getID());
                List<Object[]> addresses = nativeQuery2.getResultList();

                addresses.forEach(value ->
                {
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
    void test060Projection()
    {
        try (Session session = SESSIONFACTORY.openSession())
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

    /**
     *
     */
    @Test
    @SuppressWarnings(
            {
                    "deprecation", "unchecked", "serial"
            })
    void test080Transformer()
    {
        try (Session session = SESSIONFACTORY.openSession())
        {
            Query<TEmployee> query = session.createNativeQuery("select ID, NAME, VORNAME from T_PERSON order by NAME asc");
            query = query.setResultTransformer(new ResultTransformer()
            {
                /**
                 */
                @SuppressWarnings("rawtypes")
                @Override
                public List transformList(final List collection)
                {
                    return collection;
                }

                /**
                 *
                 */
                @Override
                public Object transformTuple(final Object[] tuple, final String[] aliases)
                {
                    TEmployee employee = new TEmployee();
                    employee.setMyId(((Number) tuple[0]).longValue());
                    employee.setName((String) tuple[1]);
                    employee.setVorname((String) tuple[2]);

                    return employee;
                }
            });
            // query = query.setResultTransformer(Transformers.aliasToBean(TEmployee.class));
            // query.addScalar("name").addScalar("vorName")

            List<TEmployee> result = query.getResultList();

            assertNotNull(result);
            assertFalse(result.isEmpty());

            for (int i = 1; i <= result.size(); i++)
            {
                TEmployee employee = result.get(i - 1);

                assertEquals("Name" + i, employee.getName());
                assertEquals("Vorname" + i, employee.getVorname());
            }
        }
    }

    /**
     *
     */
    @Test
    void test099Statistics()
    {
        dumpStatistics(System.out, SESSIONFACTORY);

        assertTrue(true);
    }
}
