/**
 *
 */
package de.freese.jpa.jdbc;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.querydsl.codegen.JavaTypeMappings;
import com.querydsl.sql.codegen.MetaDataExporter;

/**
 * Erzeugt aus den DB-Metadaten die Entities.
 * <p>
 *
 * @author Thomas Freese
 *         <p>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMetaDataExporter
{
    /**
     *
     */
    private static DataSource DATASOURCE = null;

    /**
     *
     */
    @AfterClass
    public static void afterClass()
    {
        ((SingleConnectionDataSource) DATASOURCE).destroy();
    }

    /**
     *
     */
    @BeforeClass
    public static void beforeClass()
    {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ds.setDriverClassName("org.hsqldb.jdbcDriver");
        ds.setUrl("jdbc:hsqldb:file:hsqldb/hsqldb;create=false;shutdown=true");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setSuppressClose(true);

        DATASOURCE = ds;
    }

    /**
     *
     */
    public TestMetaDataExporter()
    {
        super();
    }

    /**
     *
     */
    @After
    public void afterMethod()
    {
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @Before
    public void beforeMethod() throws Exception
    {
    }

    /**
     * @throws SQLException Falls was schief geht.
     */
    @Test
    public void createEntities() throws SQLException
    {
        MyBeanSerializer serializer = new MyBeanSerializer();
        serializer.addInterface(Serializable.class);
        serializer.setAddFullConstructor(false);

        MetaDataExporter exporter = new MetaDataExporter();
        // exporter.setConfiguration(configuration);
        // exporter.setNamingStrategy(namingStrategy);
        exporter.setTypeMappings(new JavaTypeMappings());
        exporter.setBeanSerializer(serializer);
        exporter.setPackageName("de.freese.sql.querydsl");
        exporter.setTargetFolder(new File("src/main/generated"));
        exporter.setSchemaPattern("PUBLIC");
        exporter.setTableNamePattern("T_%");
        exporter.setNamePrefix("Q");
        exporter.setColumnAnnotations(true);
        exporter.setValidationAnnotations(true);
        exporter.setExportTables(true);
        exporter.setExportViews(false);

        Connection connection = DataSourceUtils.getConnection(DATASOURCE);
        exporter.export(connection.getMetaData());
    }
}
