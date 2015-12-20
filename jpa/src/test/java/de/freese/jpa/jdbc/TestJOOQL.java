// Erzeugt: 09.12.2015
package de.freese.jpa.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.conf.ParamType;
import org.jooq.conf.RenderKeywordStyle;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import de.freese.sql.jooql.tables.TPerson;
import de.freese.sql.jooql.tables.records.TPersonRecord;

/**
 * Testcaste für JOOQL.
 * <p>
 *
 * @author Thomas Freese
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJOOQL
{
    /**
     *
     */
    private static SingleConnectionDataSource DATA_SOURCE = null;

    /**
     *
     */
    private static DSLContext DSL_CONTEXT = null;

    /**
     *
     */
    private static JdbcTemplate JDBC_TEMPLATE = null;

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
        DATA_SOURCE.setDriverClassName("org.hsqldb.jdbcDriver");
        DATA_SOURCE.setUrl("jdbc:hsqldb:file:hsqldb/hsqldb;create=false;shutdown=true");
        DATA_SOURCE.setUsername("sa");
        DATA_SOURCE.setPassword("");
        DATA_SOURCE.setSuppressClose(true);

        JDBC_TEMPLATE = new JdbcTemplate(DATA_SOURCE);

        Settings settings = new Settings();
        settings.setRenderFormatted(Boolean.TRUE);
        settings.setRenderNameStyle(RenderNameStyle.UPPER);
        settings.setRenderKeywordStyle(RenderKeywordStyle.UPPER);
        settings.setStatementType(StatementType.PREPARED_STATEMENT);
        settings.setRenderSchema(Boolean.FALSE);
        settings.setParamType(ParamType.INLINED);
        settings.setExecuteLogging(Boolean.TRUE);

        Configuration configuration = new DefaultConfiguration();
        configuration.set(settings);
        configuration.set(DATA_SOURCE);
        configuration.set(SQLDialect.HSQLDB);

        DSL_CONTEXT = DSL.using(configuration);
    }

    /**
     *
     */
    public TestJOOQL()
    {
        super();
    }

    /**
     *
     */
    @Test
    public void test01Select()
    {
        TPerson tPerson = TPerson.T_PERSON;

        Select<TPersonRecord> select = DSL_CONTEXT.selectFrom(tPerson).where(tPerson.NAME.contains("ee"));
        // System.out.println(select.getSQL(ParamType.INDEXED));
        Result<TPersonRecord> result = select.fetch();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() >= 1);
        Assert.assertEquals(1L, result.get(0).getMyId().longValue());
        // Assert.assertEquals(1L, result.get(0).getValue(tPerson.MY_ID).longValue());
    }

    /**
     *
     */
    @Test
    public void test01SelectSpring()
    {
        TPerson tPerson = TPerson.T_PERSON;

        Select<TPersonRecord> select = DSL_CONTEXT.selectFrom(tPerson).where(tPerson.NAME.contains("ee"));
        String sql = select.getSQL(ParamType.INLINED); // StaticStatement

        List<de.freese.sql.querydsl.TPerson> springResult = JDBC_TEMPLATE.query(sql, new PersonRowMapper());

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
        TPerson tPerson = TPerson.T_PERSON;

        Select<TPersonRecord> select = DSL_CONTEXT.selectFrom(tPerson).where(tPerson.MY_ID.equal(1L));
        TPersonRecord result = select.fetchOne();

        Assert.assertNotNull(result);
        Assert.assertEquals(1L, result.getMyId().longValue());
    }

    /**
     *
     */
    @Test
    public void test02FindOneSpring()
    {
        TPerson tPerson = TPerson.T_PERSON;

        Select<TPersonRecord> select = DSL_CONTEXT.selectFrom(tPerson).where(tPerson.MY_ID.equal(1L));
        String sql = select.getSQL(ParamType.INDEXED); // PreparedStatement

        List<de.freese.sql.querydsl.TPerson> springResult = JDBC_TEMPLATE.query(sql, new PersonRowMapper(), 1L);

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
        TPerson tPerson = TPerson.T_PERSON;

        Select<TPersonRecord> select = DSL_CONTEXT.selectFrom(tPerson).where(tPerson.NAME.contains("ee"));
        ResultSet resultSet = select.fetchResultSet();

        ResultSetExtractor<List<de.freese.sql.querydsl.TPerson>> resultSetExtractor = new RowMapperResultSetExtractor<>(new PersonRowMapper());
        List<de.freese.sql.querydsl.TPerson> result = resultSetExtractor.extractData(resultSet);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() >= 1);
        Assert.assertEquals(1L, result.get(0).getMyId().longValue());
    }
}
