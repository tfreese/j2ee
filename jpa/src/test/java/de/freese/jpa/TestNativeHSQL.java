/**
 *
 */
package de.freese.jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import de.freese.jpa.model.Address;
import de.freese.jpa.model.Person;

/**
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestNativeHSQL extends AbstractTest
{
    /**
     *
     */
    private static Connection CONNECTION = null;

    // /**
    // *
    // */
    // private final static LinkedList<Long> ID_BLOCK = new LinkedList<>();

    /**
     * @throws Exception Falls was schief geht.
     */
    @AfterAll
    static void afterAll() throws Exception
    {
        try (Statement statement = CONNECTION.createStatement())
        {
            statement.execute("SHUTDOWN COMPACT");
        }

        CONNECTION.close();
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @BeforeAll
    static void beforeAll() throws Exception
    {
        Class.forName("org.hsqldb.jdbcDriver");

        // Verbindung mit der Datenbank aufnehmen
        CONNECTION = DriverManager.getConnection("jdbc:hsqldb:mem:" + System.currentTimeMillis(), "sa", "");
        CONNECTION.setAutoCommit(false);

        ScriptUtils.executeSqlScript(CONNECTION, new ClassPathResource("hsqldb-schema.sql"));
        ScriptUtils.executeSqlScript(CONNECTION, new ClassPathResource("hsqldb-data.sql"));

        try (Statement statement = CONNECTION.createStatement())
        {
            statement.execute("DROP table if exists T_ADDRESS CASCADE");
            statement.execute("DROP table if exists T_PERSON CASCADE");
            statement.execute("DROP sequence if exists PERSON_SEQ");
            statement.execute("DROP sequence if exists ADDRESS_SEQ");

            statement.execute("CREATE sequence PERSON_SEQ AS BIGINT start with 1 increment by 1");
            statement.execute("CREATE sequence ADDRESS_SEQ AS BIGINT start with 1 increment by 1");
            statement.execute("CREATE MEMORY table T_ADDRESS (ID bigint not null, STREET varchar(50) not null, PERSON_ID bigint not null, primary key (ID))");
            statement.execute("CREATE MEMORY table T_PERSON  (ID bigint not null, NAME varchar(50) not null, VORNAME varchar(50) not null, primary key (ID))");
            statement.execute("ALTER table T_ADDRESS add constraint UNQ_ADDRESS_PERSON_STREET unique (PERSON_ID, STREET)");
            statement.execute("ALTER table T_ADDRESS add constraint FK_PERSON foreign key (PERSON_ID) references T_PERSON");
            statement.execute("ALTER table T_PERSON add constraint UNQ_PERSON_NAME_VORNAME unique (NAME, VORNAME)");
        }

        // SingleConnectionDataSource ds = new SingleConnectionDataSource();
        // ds.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        // ds.setUrl("jdbc:hsqldb:file:hsqldb/employee;create=true;shutdown=true");
        // ds.setUsername("sa");
        // ds.setPassword("");

        // try (Connection con = ds.getConnection();
        // Statement stmt = con.createStatement())
        // {
        // stmt.execute("DROP TABLE IF EXISTS T_EMPLOYEE CASCADE");
        // stmt.execute("CREATE TABLE T_EMPLOYEE(MY_ID BIGINT, NAME VARCHAR(25), VORNAME VARCHAR(25))");
        // stmt.execute("ALTER TABLE T_EMPLOYEE ADD CONSTRAINT EMPLOYEE_PK PRIMARY KEY (MY_ID)");
        // stmt.execute("ALTER TABLE T_EMPLOYEE ALTER COLUMN MY_ID NOT NULL");
        // stmt.execute("INSERT INTO T_EMPLOYEE VALUES(1, 'Freese', 'Thomas')");
        // con.commit();
        // }
        //
        // ds.destroy();
    }

    /**
     * Creates a new {@link TestNativeHSQL} object.
     */
    public TestNativeHSQL()
    {
        super();
    }

    /**
     * @return long
     * @throws Exception Falls was schief geht.
     */
    private long getNextAddressID() throws Exception
    {
        return getNextID("ADDRESS_SEQ");
    }

    /**
     * @param sequence String
     * @return long
     * @throws Exception Falls was schief geht.
     */
    private long getNextID(final String sequence) throws Exception
    {
        long id = -1;

        try (Statement statement = CONNECTION.createStatement();
             ResultSet resultSet = statement.executeQuery("call next value for " + sequence))
        {
            resultSet.next();
            id = resultSet.getInt(1);
        }

        return id;

        // if (ID_BLOCK.isEmpty())
        // {
        // try (Statement statement = CONNECTION.createStatement())
        // {
        // try (ResultSet resultSet = statement.executeQuery("call next value for sequence"))
        // {
        // while (resultSet.next())
        // {
        // ID_BLOCK.add(Long.valueOf(resultSet.getInt(1)));
        // }
        //
        // LOGGER.info(resultSet.getMetaData().toString());
        // }
        // }
        // }
        //
        // return ID_BLOCK.removeFirst();
    }

    /**
     * @return long
     * @throws Exception Falls was schief geht.
     */
    private long getNextPersonID() throws Exception
    {
        return getNextID("PERSON_SEQ");
    }

    /**
     * @param persons {@link List}
     * @throws Exception Falls was schief geht.
     */
    private void selectAddresses(final List<Person> persons) throws Exception
    {
        try (PreparedStatement statement = CONNECTION.prepareStatement("select ID, STREET from T_ADDRESS where PERSON_ID = ? order by street desc"))
        {
            for (Person person : persons)
            {
                statement.setLong(1, person.getID());

                try (ResultSet resultSet = statement.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Address address = new Address(resultSet.getString("STREET"));
                        address.setID(resultSet.getLong("ID"));
                        person.addAddress(address);
                    }
                }

                statement.clearParameters();
            }
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test1Insert()
     */
    @Override
    @Test
    public void test1Insert()
    {
        List<Person> persons = createPersons();

        try (PreparedStatement statement = CONNECTION.prepareStatement("insert into T_PERSON (NAME, VORNAME, ID) values (?, ?, ?)"))
        {
            for (Person person : persons)
            {
                long id = getNextPersonID();

                statement.setString(1, person.getName());
                statement.setString(2, person.getVorname());
                statement.setLong(3, id);
                person.setID(id);

                statement.execute();
                statement.clearParameters();
            }
        }
        catch (Exception ex)
        {
            try
            {
                CONNECTION.rollback();
            }
            catch (Exception ex2)
            {
                // Ignore
            }

            throw new RuntimeException(ex);
        }

        try (PreparedStatement statement = CONNECTION.prepareStatement("insert into T_ADDRESS (PERSON_ID, STREET, ID) values (?, ?, ?)"))
        {
            for (Person person : persons)
            {
                for (Address address : person.getAddresses())
                {
                    long id = getNextAddressID();

                    statement.setLong(1, address.getPerson().getID());
                    statement.setString(2, address.getStreet());
                    statement.setLong(3, id);
                    address.setID(id);

                    statement.execute();
                    statement.clearParameters();
                }
            }
        }
        catch (Exception ex)
        {
            try
            {
                CONNECTION.rollback();
            }
            catch (Exception ex2)
            {
                // Ignore
            }

            throw new RuntimeException(ex);
        }

        validateTest1Insert(persons);

        try
        {
            CONNECTION.commit();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @see de.freese.jpa.AbstractTest#test2SelectAll()
     */
    @Override
    @Test
    public void test2SelectAll()
    {
        List<Person> persons = new ArrayList<>();

        try (Statement statement = CONNECTION.createStatement();
             ResultSet resultSet = statement.executeQuery("select * from T_PERSON order by id asc"))
        {
            while (resultSet.next())
            {
                Person person = new Person(resultSet.getString("NAME"), resultSet.getString("VORNAME"));
                person.setID(resultSet.getLong("ID"));

                persons.add(person);
            }

            selectAddresses(persons);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        validateTest2SelectAll(persons);
    }

    /**
     * @see de.freese.jpa.AbstractTest#test3SelectVorname()
     */
    @Override
    @Test
    public void test3SelectVorname()
    {
        String vorname = "Vorname1";
        List<Person> persons = new ArrayList<>();

        try (PreparedStatement statement = CONNECTION.prepareStatement("select * from T_PERSON where vorname = ? order by name asc"))
        {
            statement.setString(1, vorname);

            try (ResultSet resultSet = statement.executeQuery())
            {
                while (resultSet.next())
                {
                    Person person = new Person(resultSet.getString("NAME"), resultSet.getString("VORNAME"));
                    person.setID(resultSet.getLong("ID"));

                    persons.add(person);
                }
            }

            selectAddresses(persons);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        validateTest3SelectVorname(persons, vorname);
    }

    /**
     * @see de.freese.jpa.AbstractTest#test4NativeQuery()
     */
    @Override
    @Test
    public void test4NativeQuery()
    {
        // Nur für Hibernate- und JPA-Tests relevant.
    }
}
