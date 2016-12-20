/**
 * Created: 11.12.2011
 */
package de.freese.spring.config.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Thomas Freese
 */
public class TestDAO implements ITestDAO// , InitializingBean
{
    /**
     *
     */
    @SuppressWarnings("unused")
    private final String dbName;

    /**
     *
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Erstellt ein neues {@link TestDAO} Object.
     *
     * @param dataSource {@link DataSource}
     * @param dbName String
     */
    public TestDAO(final DataSource dataSource, final String dbName)
    {
        super();

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dbName = dbName;
    }

    /**
     * @see de.freese.spring.config.dao.ITestDAO#loadData()
     */
    @Override
    public List<String> loadData()
    {
        return this.jdbcTemplate.queryForList("select NAME from NAMEN", String.class);
    }

    // /**
    // * @throws Exception Falls was schief geht.
    // */
    // public void shutdown() throws Exception
    // {
    // boolean gotSQLExc = false;
    //
    // try
    // {
    // // Alle DBs runterfahren
    // DriverManager.getConnection("jdbc:derby:;shutdown=true");
    //
    // // Spezifische DB runterfahren
    // // DriverManager.getConnection("jdbc:derby:" + this.dbName + ";shutdown=true");
    // }
    // catch (SQLException ex)
    // {
    // if (ex.getSQLState().equals("XJ015"))
    // {
    // gotSQLExc = true;
    // }
    // else
    // {
    // throw ex;
    // }
    // }
    //
    // if (!gotSQLExc)
    // {
    // this.logger.warn("Database did not shut down normally");
    // }
    // else
    // {
    // this.logger.info("Database shut down normally");
    // }
    // }
    //
    // /**
    // * @throws SQLException Falls was schief geht.
    // */
    // public void startup() throws SQLException
    // {
    // // Prüfen ob Tabellen existieren.
    // DataSource dataSource = this.jdbcTemplate.getDataSource();
    //
    // boolean exist = false;
    //
    // try (Connection connection = dataSource.getConnection())
    // {
    // DatabaseMetaData metaData = connection.getMetaData();
    // ResultSet resultSet = metaData.getTables(null, null, "NAMEN", new String[]
    // {
    // "TABLE"
    // });
    //
    // while (resultSet.next())
    // {
    // // String tableName = resultSet.getString("TABLE_NAME");
    // exist = true;
    // }
    // }
    //
    // if (!exist)
    // {
    // this.logger.info("Create Database Tables");
    //
    // // Keine Tabellen vorhanden.
    // this.jdbcTemplate
    // .execute("CREATE TABLE NAMEN (ID INTEGER PRIMARY KEY generated by default as identity (START WITH 1, INCREMENT BY 1), NAME VARCHAR(12))");
    // this.jdbcTemplate.execute("INSERT INTO NAMEN (NAME) VALUES ('FREESE')");
    // }
    // }
}
