/**
 *
 */
package de.freese.jpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.HSQLDBTemplates;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import de.freese.jpa.jdbc.PersonRowMapper;
import de.freese.jpa.jdbc.SpringExceptionTranslator;
import de.freese.sql.querydsl.QTPerson;
import de.freese.sql.querydsl.TPerson;

/**
 * Testcaste für QueryDSL.
 * <p>
 * org.springframework.data.jdbc.query.QueryDslJdbcTemplate ist deprecated!
 * <p>
 *
 * @author Thomas Freese
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestQueryDSL
{
    /**
     *
     */
    private static SingleConnectionDataSource DATA_SOURCE = null;

    /**
     *
     */
    private static JdbcTemplate JDBC_TEMPLATE = null;

    /**
     *
     */
    private static SQLQueryFactory QUERY_FACTORY = null;

    /**
     *
     */
    private static PlatformTransactionManager TX_MANAGER = null;

    /**
     *
     */
    @AfterClass
    public static void afterClass()
    {
        DATA_SOURCE.destroy();
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @BeforeClass
    public static void beforeClass() throws Exception
    {
        DATA_SOURCE = new SingleConnectionDataSource();
        DATA_SOURCE.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        DATA_SOURCE.setUrl("jdbc:hsqldb:mem:test");
        DATA_SOURCE.setUsername("sa");
        DATA_SOURCE.setPassword("");
        DATA_SOURCE.setSuppressClose(true);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("hsqldb-schema.sql"));
        populator.addScript(new ClassPathResource("hsqldb-data.sql"));
        populator.execute(DATA_SOURCE);

        JDBC_TEMPLATE = new JdbcTemplate(DATA_SOURCE);
        TX_MANAGER = new DataSourceTransactionManager(DATA_SOURCE);

        // SQLTemplates sQLTemplates = H2Templates.builder()
        // .printSchema() // to include the schema in the output
        // .quote() // to quote names
        // .newLineToSingleSpace() // to replace new lines with single space in
        // the output
        // .escape('\'') // to set the escape char
        // .build(); // to get the customized SQLTemplates instance
        Configuration configuration = new Configuration(new HSQLDBTemplates());
        configuration.setExceptionTranslator(new SpringExceptionTranslator());

        QUERY_FACTORY = new SQLQueryFactory(configuration, DATA_SOURCE);

        // SpringConnectionProvider erfordert aktive Transaction.
        // Provider<Connection> provider = new SpringConnectionProvider(DATA_SOURCE);
        // QUERY_FACTORY = new SQLQueryFactory(configuration, provider);
    }

    /**
     *
     */
    public TestQueryDSL()
    {
        super();

        // siehe test/resources/simplelogger.properties
        // System.setProperty("org.slf4j.simpleLogger.log.org.springframework.jdbc.core.JdbcTemplate",
        // "DEBUG");
        // System.setProperty("org.slf4j.simpleLogger.log.com.mysema.query.sql.AbstractSQLQuery",
        // "DEBUG");
    }

    /**
     *
     */
    @After
    public void afterMethod()
    {
    }

    /**
     *
     */
    @Before
    public void beforeMethod()
    {
    }

    /**
     *
     */
    @Test
    public void test01Select()
    {
        QTPerson p = QTPerson.tPerson;

        // SQLQuery<String> query = QUERY_FACTORY.select(p.name).from(p).where(p.name.contains("ee"));
        SQLQuery<TPerson> query = QUERY_FACTORY.select(p).from(p).where(p.name.contains("ee"));
        List<TPerson> result = query.fetch();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() >= 1);
        Assert.assertEquals(1L, result.get(0).getMyId().longValue());
    }

    /**
     *
     */
    @Test
    public void test01SelectSpring()
    {
        QTPerson p = QTPerson.tPerson;

        SQLQuery<TPerson> query = QUERY_FACTORY.select(p).from(p).where(p.name.contains("ee"));
        SQLBindings bindings = query.getSQL(); // PreparedStatement
        // String normalizedQuery = bindings.getSQL().replace('\n', ' ');
        // System.out.println(normalizedQuery);

        List<TPerson> springResult = JDBC_TEMPLATE.query(bindings.getSQL(), new PersonRowMapper(), "%ee%");

        Assert.assertNotNull(springResult);
        Assert.assertTrue(springResult.size() >= 1);
        Assert.assertEquals(1L, springResult.get(0).getMyId().longValue());
    }

    /**
     *
     */
    @Test
    public void test02FindOne()
    {
        QTPerson p = QTPerson.tPerson;

        // QueryDSL mit SpringConnectionProvider erfordert aktive Transaction.
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = TX_MANAGER.getTransaction(transactionDefinition);

        SQLQuery<TPerson> query = QUERY_FACTORY.select(p).from(p).where(p.myId.eq(1L));

        TPerson result = query.fetchOne();
        TX_MANAGER.commit(transactionStatus);

        Assert.assertNotNull(result);
        Assert.assertEquals(1L, result.getMyId().longValue());
    }

    /**
     *
     */
    @Test
    public void test02FindOneSpring()
    {
        QTPerson p = QTPerson.tPerson;

        SQLQuery<TPerson> query = QUERY_FACTORY.select(p).from(p).where(p.myId.eq(1L));
        SQLBindings bindings = query.getSQL(); // PreparedStatement
        // String normalizedQuery = bindings.getSQL().replace('\n', ' ');
        // System.out.println(normalizedQuery);

        List<TPerson> springResult = JDBC_TEMPLATE.query(bindings.getSQL(), new PersonRowMapper(), 1L);

        Assert.assertNotNull(springResult);
        Assert.assertTrue(springResult.size() >= 1);
        Assert.assertEquals(1L, springResult.get(0).getMyId().longValue());
    }

    /**
     * @throws SQLException Falls was schief geht.
     */
    @Test
    public void test03SelectRowMapper() throws SQLException
    {
        QTPerson p = QTPerson.tPerson;

        SQLQuery<TPerson> query = QUERY_FACTORY.select(p).from(p).where(p.name.contains("ee"));
        ResultSet resultSet = query.getResults();
        // ResultSet resultSet = query.clone(DATA_SOURCE.getConnection()).getResults(p);

        ResultSetExtractor<List<TPerson>> resultSetExtractor = new RowMapperResultSetExtractor<>(new PersonRowMapper());
        List<TPerson> result = resultSetExtractor.extractData(resultSet);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() >= 1);
        Assert.assertEquals(1L, result.get(0).getMyId().longValue());
    }
}
