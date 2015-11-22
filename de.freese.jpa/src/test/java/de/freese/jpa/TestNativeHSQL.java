/**
 *
 */
package de.freese.jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.AfterClass;
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
public class TestNativeHSQL extends AbstractTest
{
    /**
     *
     */
    private static Connection CONNECTION = null;

    /**
     *
     */
    private final static LinkedList<Long> ID_BLOCK = new LinkedList<>();

    /**
     * @throws Exception Falls was schief geht.
     */
    @AfterClass
    public static void afterClass() throws Exception
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
    @BeforeClass
    public static void beforeClass() throws Exception
    {
        Class.forName("org.hsqldb.jdbcDriver");

        // Verbindung mit der Datenbank aufnehmen
        CONNECTION = DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", "");
        CONNECTION.setAutoCommit(false);

        try (Statement statement = CONNECTION.createStatement())
        {
            try
            {
                statement.execute("alter table T_ADDRESS drop constraint FK_PERSON");
                statement.execute("drop table T_ADDRESS if exists");
                statement.execute("drop table T_PERSON if exists");
                statement.execute("drop sequence OBJECT_SEQ");
            }
            catch (SQLSyntaxErrorException ex)
            {
                // Ohne Objekte gibs Fehler, was im Memory-DB Modus immer passiert.
            }

            statement.execute("create sequence OBJECT_SEQ AS BIGINT start with 10 increment by 1");
            statement.execute("create table T_ADDRESS (ID bigint not null, STREET varchar(50) not null, PERSON_ID bigint not null, primary key (ID))");
            statement.execute("create table T_PERSON  (ID bigint not null, NAME varchar(50) not null, VORNAME varchar(50) not null, primary key (ID))");
            statement.execute("alter table T_ADDRESS add constraint UNQ_ADDRESS_PERSON_STREET unique (PERSON_ID, STREET)");
            statement.execute("alter table T_ADDRESS add constraint FK_PERSON foreign key (PERSON_ID) references T_PERSON");
            statement.execute("alter table T_PERSON add constraint UNQ_PERSON_NAME_VORNAME unique (NAME, VORNAME)");
        }
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
    private long getNextID() throws Exception
    {
        long id = -1;

        try (Statement statement = CONNECTION.createStatement())
        {
            try (ResultSet resultSet = statement.executeQuery("call next value for OBJECT_SEQ"))
            {
                resultSet.next();
                id = resultSet.getInt(1);
            }
        }

        return id;

        // if (ID_BLOCK.isEmpty())
        // {
        // try (Statement statement = CONNECTION.createStatement())
        // {
        // try (ResultSet resultSet = statement.executeQuery("call next value for OBJECT_SEQ"))
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
                long id = getNextID();

                statement.setString(1, person.getName());
                statement.setString(2, person.getVorName());
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
                    long id = getNextID();

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

        try (Statement statement = CONNECTION.createStatement())
        {
            try (ResultSet resultSet = statement.executeQuery("select * from T_PERSON order by id asc"))
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
}
