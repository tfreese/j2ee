/**
 *
 */
package de.freese.jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Thomas Freese
 */
public class TestHSQL
{
	/**
	 * @param args String[]
	 */
	public static void main(final String[] args)
	{
		// Hier wird ein laufender Server benoetigt
		// String URL = "jdbc:hsqldb:hsql://localhost:1234/fileDB";
		// String URL = "jdbc:hsqldb:hsql://localhost:1234/memoryDB";

		// Hier wird der Server automatisch gestartet.
		// String URL_FILE = "jdbc:hsqldb:file:D:/hsqldb/fileDB/fileDB";
		// String URL = "jdbc:hsqldb:file:hsqldb/hibernateJPA";

		//
		// Hier wird er Server automatisch gestartet.
		// Es erfolgt keine Festplattenzugriff, alle Daten sind im Speicher.
		String URL = "jdbc:hsqldb:mem:memoryDB";

		TestHSQL test = new TestHSQL();

		test.connect("org.hsqldb.jdbcDriver", URL, "sa", "");

		// test.clear();
		test.populate();
		test.insert("Freese");
		test.insert("Freese2");
		test.select();

		test.disconnect();
	}

	/**
     *
     */
	private Connection connection = null;

	/**
     * 
     */
	private Statement statement = null;

	/**
	 * Creates a new {@link TestHSQL} object.
	 */
	public TestHSQL()
	{
		super();
	}

	/**
	 * 
	 */
	public void clear()
	{
		try
		{
			this.statement = this.connection.createStatement();
			this.statement.execute("DROP SEQUENCE TEST_SEQ");
			this.statement.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}

		try
		{
			this.statement = this.connection.createStatement();
			this.statement.execute("DROP TABLE TEST");
			this.statement.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @param driverName String
	 * @param url String
	 * @param user String
	 * @param psw String
	 */
	public void connect(final String driverName, final String url, final String user, final String psw)
	{
		// Abfragen der Verbindungsparameter
		System.out.println("Datenbankzugriff mit JDBC");
		System.out.println("=========================");

		// Treiber laden und Verbindung herstellen
		try
		{
			Class.forName(driverName);

			// Verbindung mit der Datenbank aufnehmen
			this.connection = DriverManager.getConnection(url, user, psw);
			System.out.println("\nVerbinden ...");
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			System.out.println("Fehler beim Verbindungsaufbau!");
			System.exit(0);
		}
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
			System.out.println("JDBC Treiber nicht gefunden!");
			System.exit(0);
		}

		System.out.println("Verbindungsaufbau erfolgreich\n");
	}

	/**
	 *
	 */
	public void disconnect()
	{
		try
		{
			this.statement = this.connection.createStatement();
			this.statement.execute("SHUTDOWN COMPACT");
			this.statement.close();
			this.connection.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @param name String
	 */
	public void insert(final String name)
	{
		try
		{
			this.statement = this.connection.createStatement();

			int pk = 0;

			try (ResultSet result = this.statement.executeQuery("call next value for test_seq"))
			{
				while (result.next())
				{
					pk = result.getInt(1);
				}
			}

			this.statement.execute("INSERT INTO TEST (ID,NAME) VALUES(" + pk + ",'" + name + "')");
			this.statement.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 *
	 */
	public void populate()
	{
		try
		{
			this.statement = this.connection.createStatement();
			this.statement.execute("CREATE SEQUENCE TEST_SEQ AS BIGINT START WITH 1 INCREMENT BY 1");
			this.statement
					.execute("CREATE MEMORY TABLE TEST(ID BIGINT NOT NULL PRIMARY KEY, NAME VARCHAR(50) NOT NULL CHECK (NAME <> ''), CONSTRAINT TEST_UNQ UNIQUE(NAME))");
			this.statement.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void select()
	{
		try
		{
			this.statement = this.connection.createStatement();

			ResultSet result = this.statement.executeQuery("SELECT * FROM Test");

			while (result.next())
			{
				System.out.print(result.getInt(1));
				System.out.println("; " + result.getString(2));
			}

			this.statement.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}
