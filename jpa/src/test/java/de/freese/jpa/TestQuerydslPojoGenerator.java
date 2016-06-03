/**
 *
 */
package de.freese.jpa;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import com.querydsl.codegen.JavaTypeMappings;
import com.querydsl.sql.codegen.MetaDataExporter;
import de.freese.jpa.jdbc.MyBeanSerializer;

/**
 * Erzeugt aus den DB-Metadaten die Entities.
 * <p>
 *
 * @author Thomas Freese
 *         <p>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestQuerydslPojoGenerator
{
    /**
     *
     */
    private static SingleConnectionDataSource DATASOURCE = null;

    /**
     *
     */
    @AfterClass
    public static void afterClass()
    {
        DATASOURCE.destroy();
    }

    /**
     *
     */
    @BeforeClass
    public static void beforeClass()
    {
        DATASOURCE = new SingleConnectionDataSource();
        DATASOURCE.setDriverClassName("org.hsqldb.jdbcDriver");
        DATASOURCE.setUrl("jdbc:hsqldb:mem:test");
        DATASOURCE.setUsername("sa");
        DATASOURCE.setPassword("");
        DATASOURCE.setSuppressClose(true);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("hsqldb-schema.sql"));
        // populator.addScript(new ClassPathResource("hsqldb-data.sql"));
        populator.execute(DATASOURCE);
    }

    /**
     *
     */
    public TestQuerydslPojoGenerator()
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
