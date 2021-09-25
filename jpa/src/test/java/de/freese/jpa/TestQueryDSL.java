package de.freese.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

import de.freese.TEmployeeRowMapper;
import de.freese.jpa.jdbc.SpringExceptionTranslator;
import de.freese.sql.querydsl.QTEmployee;
import de.freese.sql.querydsl.TEmployee;

/**
 * Testcaste für QueryDSL.
 * <p>
 * org.springframework.data.jdbc.query.QueryDslJdbcTemplate ist deprecated!
 * <p>
 *
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestQueryDSL
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
    @AfterAll
    static void afterAll()
    {
        DATA_SOURCE.destroy();
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @BeforeAll
    static void beforeAll() throws Exception
    {
        DATA_SOURCE = new SingleConnectionDataSource();
        DATA_SOURCE.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        DATA_SOURCE.setUrl("jdbc:hsqldb:mem:" + System.currentTimeMillis());
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
    @Test
    void test01Select()
    {
        QTEmployee e = QTEmployee.tEmployee;

        // SQLQuery<String> query = QUERY_FACTORY.select(e.name).from(e).where(e.name.contains("ee"));
        SQLQuery<TEmployee> query = QUERY_FACTORY.select(e).from(e).where(e.name.contains("ee"));
        List<TEmployee> result = query.fetch();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getMyId());
    }

    /**
     *
     */
    @Test
    void test01SelectSpring()
    {
        QTEmployee e = QTEmployee.tEmployee;

        SQLQuery<TEmployee> query = QUERY_FACTORY.select(e).from(e).where(e.name.contains("ee"));
        SQLBindings bindings = query.getSQL(); // PreparedStatement
        // String normalizedQuery = bindings.getSQL().replace('\n', ' ');
        // System.out.println(normalizedQuery);

        List<TEmployee> springResult = JDBC_TEMPLATE.query(bindings.getSQL(), new TEmployeeRowMapper(), "%ee%");

        assertNotNull(springResult);
        assertTrue(springResult.size() >= 1);
        assertEquals(1L, springResult.get(0).getMyId());
    }

    /**
     *
     */
    @Test
    void test02FindOne()
    {
        QTEmployee e = QTEmployee.tEmployee;

        // QueryDSL mit SpringConnectionProvider erfordert aktive Transaction.
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = TX_MANAGER.getTransaction(transactionDefinition);

        SQLQuery<TEmployee> query = QUERY_FACTORY.select(e).from(e).where(e.myId.eq(1L));

        TEmployee result = query.fetchOne();
        TX_MANAGER.commit(transactionStatus);

        assertNotNull(result);
        assertEquals(1L, result.getMyId());
    }

    /**
     *
     */
    @Test
    void test02FindOneSpring()
    {
        QTEmployee e = QTEmployee.tEmployee;

        SQLQuery<TEmployee> query = QUERY_FACTORY.select(e).from(e).where(e.myId.eq(1L));
        SQLBindings bindings = query.getSQL(); // PreparedStatement
        // String normalizedQuery = bindings.getSQL().replace('\n', ' ');
        // System.out.println(normalizedQuery);

        List<TEmployee> springResult = JDBC_TEMPLATE.query(bindings.getSQL(), new TEmployeeRowMapper(), 1L);

        assertNotNull(springResult);
        assertTrue(springResult.size() >= 1);
        assertEquals(1L, springResult.get(0).getMyId());
    }

    /**
     * @throws SQLException Falls was schief geht.
     */
    @Test
    void test03SelectRowMapper() throws SQLException
    {
        QTEmployee e = QTEmployee.tEmployee;

        SQLQuery<TEmployee> query = QUERY_FACTORY.select(e).from(e).where(e.name.contains("ee"));

        // ResultSet resultSet = query.clone(DATA_SOURCE.getConnection()).getResults(p);
        try (ResultSet resultSet = query.getResults())
        {
            ResultSetExtractor<List<TEmployee>> resultSetExtractor = new RowMapperResultSetExtractor<>(new TEmployeeRowMapper());
            List<TEmployee> result = resultSetExtractor.extractData(resultSet);

            assertNotNull(result);
            assertTrue(result.size() >= 1);
            assertEquals(1L, result.get(0).getMyId());
        }
    }
}
