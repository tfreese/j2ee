/**
 *
 */
package de.freese.jpa;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.querydsl.codegen.JavaTypeMappings;
import com.querydsl.sql.codegen.MetaDataExporter;

/**
 * Erzeugt aus den DB-Metadaten die Entities.
 *
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestQuerydslPojoGenerator
{
    /**
     *
     */
    private static SingleConnectionDataSource DATASOURCE = null;

    /**
     *
     */
    @AfterAll
    static void afterAll()
    {
        DATASOURCE.destroy();
    }

    /**
     *
     */
    @BeforeAll
    static void beforeAll()
    {
        DATASOURCE = new SingleConnectionDataSource();
        DATASOURCE.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        // DATASOURCE.setUrl("jdbc:hsqldb:mem:" + System.currentTimeMillis());
        // DATASOURCE.setUrl("jdbc:hsqldb:file:hsqldb/employee;create=false;readonly=true;shutdown=true");
        DATASOURCE.setUrl("jdbc:hsqldb:file:hsqldb/person;create=false;readonly=true;shutdown=true");
        DATASOURCE.setUsername("sa");
        DATASOURCE.setPassword("");
        DATASOURCE.setSuppressClose(true);

        // ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        // populator.addScript(new ClassPathResource("hsqldb-schema.sql"));
        // // populator.addScript(new ClassPathResource("hsqldb-data.sql"));
        // populator.execute(DATASOURCE);
    }

    /**
     * Erstellt ein neues {@link TestQuerydslPojoGenerator} Object.
     */
    public TestQuerydslPojoGenerator()
    {
        super();
    }

    /**
     * @throws SQLException Falls was schief geht.
     */
    @Test
    public void createEntities() throws SQLException
    {
        PojoBeanSerializer serializer = new PojoBeanSerializer();
        // PojoBeanSerializer serializer = new HibernateBeanSerializer(); // Erzeugt noch NullPointer
        serializer.addInterface(Serializable.class);
        serializer.setAddFullConstructor(false);
        // serializer.setSuperType(Address.class);
        serializer.setIncludeValidationAnnotations(true);

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
        exporter.setExportPrimaryKeys(true);

        try (Connection connection = DataSourceUtils.getConnection(DATASOURCE))
        {
            exporter.export(connection.getMetaData());
        }
    }
}
