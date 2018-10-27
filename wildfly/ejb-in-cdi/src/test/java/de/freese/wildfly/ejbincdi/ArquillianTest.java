// Erzeugt: 24.11.2015
package de.freese.wildfly.ejbincdi;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.bridge.SLF4JBridgeHandler;
import de.freese.wildfly.ejbincdi.cdi.NamedBean;
import de.freese.wildfly.ejbincdi.ejb.ITestServiceBeanLocal;
import de.freese.wildfly.ejbincdi.ejb.TestServiceBean;

/**
 * @author Thomas Freese
 */
@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class ArquillianTest
{
    /**
     * Erstellt ein Archiv mit den Klassen, die getestet werden sollen.
     *
     * @return {@link WebArchive}
     */
    @Deployment
    public static WebArchive deploy()
    {
        System.setProperty("org.jboss.logging.provider", "slf4j");
        // Wegen org.jboss.logmanager.LogManager
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "ejbincdi.war").addClasses(ITestServiceBeanLocal.class, TestServiceBean.class, NamedBean.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addAsResource("simplelogger.properties");
        // WebArchive archive
        // = ShrinkWrap.create(WebArchive.class, "ejbincdi.war").addClasses(ITestServiceBeanLocal.class, TestServiceBean.class, NamedBean.class)
        // .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
        // .addAsResource("simplelogger.properties");

        return archive;
    }

    /**
     *
     */
    @Inject()
    @Named("myNamedBean")
    private NamedBean namedBean = null;

    /**
     *
     */
    @EJB
    private ITestServiceBeanLocal serviceBean = null;

    /**
     *
     */
    public ArquillianTest()
    {
        super();
    }

    /**
     * Testet, ob die NamedBean null ist.
     */
    @Test
    public void testCDIInjection()
    {
        Assert.assertNotNull(this.namedBean);
        this.namedBean.log("test");
    }

    /**
     * Testet, ob die TestServiceBean in der NamedBean null ist.
     */
    @Test
    public void testeEJBinCDI()
    {
        Assert.assertNotNull(this.namedBean.getServiceBean());
    }

    /**
     * Testet Methodenaufruf auf TestServiceBeanLocal.
     */
    @Test
    public void testEJBGetString()
    {
        final String s = "test";
        Assert.assertEquals(s, this.serviceBean.getString(s));
    }

    /**
     * Testet, ob TestServiceBeanLocal null ist.
     */
    @Test
    public void testEJBInjection()
    {
        Assert.assertNotNull(this.serviceBean);
    }
}
