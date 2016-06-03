/**
 *
 */
package de.freese.jpa;

import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Database;
import org.jooq.util.jaxb.Generate;
import org.jooq.util.jaxb.Generator;
import org.jooq.util.jaxb.Target;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

/**
 * Erzeugt aus den DB-Metadaten die Entities.
 * <p>
 *
 * @author Thomas Freese
 *         <p>
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
        generator.setName("org.jooq.util.JavaGenerator");

        Database database = new Database();
        generator.setDatabase(database);
        database.setName("org.jooq.util.hsqldb.HSQLDBDatabase");
        database.setIncludes("T_.*");
        database.setExcludes(null);
        database.setInputSchema("PUBLIC");

        Generate generate = new Generate();
        generator.setGenerate(generate);
        generate.setDeprecated(false);
        generate.setRelations(true);
        generate.setGeneratedAnnotation(true);
        generate.setRecords(true);
        generate.setPojos(false);
        generate.setImmutablePojos(false);
        generate.setInterfaces(true);
        generate.setDaos(false);
        generate.setJpaAnnotations(true);
        generate.setValidationAnnotations(true);
        generate.setSpringAnnotations(false);
        generate.setGlobalObjectReferences(true);
        generate.setFluentSetters(true);

        Target target = new Target();
        generator.setTarget(target);
        target.setPackageName("de.freese.sql.jooql");
        target.setDirectory("src/main/generated");

        GenerationTool generationTool = new GenerationTool();
        generationTool.setDataSource(DATASOURCE);
        generationTool.run(configuration);
    }
}
