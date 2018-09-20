/**
 *
 */
package de.freese.jpa;

import org.jooq.codegen.GenerationTool;
import org.jooq.codegen.JavaGenerator;
import org.jooq.meta.hsqldb.HSQLDBDatabase;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Target;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * Erzeugt aus den DB-Metadaten die Entities.
 *
 * @author Thomas Freese
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJOOQLPojoGenerator
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
        DATASOURCE.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        // DATASOURCE.setUrl("jdbc:hsqldb:mem:" + System.currentTimeMillis());
        DATASOURCE.setUrl("jdbc:hsqldb:file:hsqldb/employee;create=false;readonly=true;shutdown=true");
        DATASOURCE.setUsername("sa");
        DATASOURCE.setPassword("");
        DATASOURCE.setSuppressClose(true);

        // ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        // populator.addScript(new ClassPathResource("hsqldb-schema.sql"));
        // // populator.addScript(new ClassPathResource("hsqldb-data.sql"));
        // populator.execute(DATASOURCE);
    }

    /**
     * Erstellt ein neues {@link TestJOOQLPojoGenerator} Object.
     */
    public TestJOOQLPojoGenerator()
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
     * @throws Exception Falls was schief geht.
     */
    @Test
    public void createEntities() throws Exception
    {
        Configuration configuration = new Configuration();

        // Jdbc jdbc = new Jdbc();
        // configuration.setJdbc(jdbc);
        // jdbc.setDriver("org.hsqldb.jdbcDriver");
        // jdbc.setUrl("jdbc:hsqldb:mem:test");
        // jdbc.setUser("sa");

        Generator generator = new Generator();
        configuration.setGenerator(generator);
        generator.setName(JavaGenerator.class.getName());

        Database database = new Database();
        generator.setDatabase(database);
        database.setName(HSQLDBDatabase.class.getName());
        database.setIncludes("T_.*");
        database.setExcludes(null);
        database.setInputSchema("PUBLIC");

        Generate generate = new Generate();
        generator.setGenerate(generate);

        generate.setDaos(false);
        generate.setDeprecated(false);
        generate.setFluentSetters(true);
        generate.setGeneratedAnnotation(true);
        generate.setGlobalObjectReferences(true);
        generate.setIndexes(false);
        generate.setImmutablePojos(false);
        generate.setInterfaces(true);
        generate.setJpaAnnotations(true);
        generate.setPojos(false);
        generate.setRecords(true);
        generate.setRelations(true);
        generate.setValidationAnnotations(true);
        generate.setSpringAnnotations(false);
        generate.setTables(false);

        Target target = new Target();
        generator.setTarget(target);
        target.setPackageName("de.freese.sql.jooql");
        target.setDirectory("src/main/generated");

        GenerationTool generationTool = new GenerationTool();
        generationTool.setDataSource(DATASOURCE);
        generationTool.run(configuration);
    }
}
