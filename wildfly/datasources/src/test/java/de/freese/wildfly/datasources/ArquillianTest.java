// Erzeugt: 24.11.2015
package de.freese.wildfly.datasources;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArquillianTest
{
    /**
     * Erstellt ein Archiv mit den Klassen, die getestet werden sollen.
     *
     * @return {@link JavaArchive}
     */
    @Deployment
    public static WebArchive deploy()
    {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "datasources.war")
                // .addPackages(true, "de.freese.wildfly.datasources")
                // enable CDI and passing empty beans.xml descriptor to war archive
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Deploy test datasource
                .addAsWebInfResource("datasource-ds.xml");

        // JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "datasources.war")
        // // .addPackages(true, "de.freese.wildfly.datasources")
        // // enable CDI and passing empty beans.xml descriptor to war archive
        // .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
        // // Deploy test datasource
        // .addAsResource("datasource-ds.xml");
        return archive;
    }

    /**
     *
     */
    @Resource(lookup = "java:jboss/datasources/ExampleDS")
    protected DataSource dataSourceDefault = null;

    /**
     * Siehe datasource-ds.xml
     */
    @Resource(lookup = "java:jboss/datasources/datasources")
    protected DataSource dataSourceXML = null;

    /**
     *
     */
    public ArquillianTest()
    {
        super();
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @Test
    public void testDatasourceDefault() throws Exception
    {
        Assert.assertNotNull(this.dataSourceDefault);

        int value = 0;

        try (Connection con = this.dataSourceDefault.getConnection())
        {
            try (Statement stmt = con.createStatement())
            {
                try (ResultSet rs = stmt.executeQuery("SELECT 1"))
                {
                    rs.next();
                    value = rs.getInt(1);
                }
            }
        }

        Assert.assertEquals(1, value);
        LoggerFactory.getLogger(getClass()).info("{}", value);
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @Test
    public void testDatasourceXML() throws Exception
    {
        Assert.assertNotNull(this.dataSourceXML);

        int value = 0;

        try (Connection con = this.dataSourceXML.getConnection())
        {
            try (Statement stmt = con.createStatement())
            {
                try (ResultSet rs = stmt.executeQuery("SELECT 1"))
                {
                    rs.next();
                    value = rs.getInt(1);
                }
            }
        }

        Assert.assertEquals(1, value);
        LoggerFactory.getLogger(getClass()).info("{}", value);
    }
}
