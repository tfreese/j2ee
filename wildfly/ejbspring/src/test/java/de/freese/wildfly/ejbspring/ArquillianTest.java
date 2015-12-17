// Erzeugt: 24.11.2015
package de.freese.wildfly.ejbspring;

import java.io.File;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.bridge.SLF4JBridgeHandler;
import de.freese.wildfly.ejbspring.facade.IMoneyTransferFacade;
import de.freese.wildfly.ejbspring.service.IMoneyTransferService;

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
        System.setProperty("org.jboss.logging.provider", "slf4j");
        // Wegen org.jboss.logmanager.LogManager
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        //@formatter:off
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "ejbspring.war")
                .addPackages(true, "de.freese.wildfly.ejbspring")
                .addAsResource("simplelogger.properties")
                .addAsResource("beanRefContext.xml")
                .addAsResource("META-INF/root-context.xml");
        //@formatter:off

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").resolve("org.springframework:spring-context").withTransitivity().asFile();
        archive.addAsLibraries(libs);

        //System.out.println(archive.toString(true));
        // archive.as(ZipExporter.class).exportTo(new File("/PATH/my.jar"), true);
        // new ZipExporterImpl(archive).exportTo(new File(archive.getName()), true);
        // StringAsset manifest=new StringAsset("Main-Class: " + getClass().getName() + "\n");
        // archive.addAsManifestResource(manifest,MANIFEST_FILE_NAME);
        // archive.addClass(getClass());

        return archive;
    }

    /**
     *
     */
    @EJB
    private IMoneyTransferFacade ejbMoneyTransferFacade = null;

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
    public void test01EJBFacadeInjection() throws Exception
    {
        Assert.assertNotNull("Arquillian EJB injection failed", this.ejbMoneyTransferFacade);
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @Test
    public void test02SpringInEJBFacadeInjection() throws Exception
    {
        Assert.assertNotNull("Spring in EJB injection failed", this.ejbMoneyTransferFacade.getTransferService());
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @Test
    public void test03SpringEJBLookup() throws Exception
    {
        Assert.assertNotNull("Static Spring ApplicationContext failed", SpringContext.getApplicationContext());

        IMoneyTransferService ejbTransferService = SpringContext.getBean("ejbMoneyTransferServiceSLSB");

        Assert.assertNotNull("Spring EJB Lookup failed", ejbTransferService);
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @Test
    public void test04EJBInSpringFacadeInjection() throws Exception
    {
        IMoneyTransferFacade springTransferFacade = SpringContext.getBean("springMoneyTransferFacade");

        Assert.assertNotNull("Spring Facade failed", springTransferFacade);
        Assert.assertNotNull("EJB in Spring Facade failed", springTransferFacade.getTransferService());
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @Test
    public void test05EJBTransferRequest() throws Exception
    {
        MoneyTransferRequest transferRequest = new MoneyTransferRequest("test", 99.0D);

        IMoneyTransferResponse transferResponse = this.ejbMoneyTransferFacade.transfer(transferRequest);

        Assert.assertNotNull("EJB TransferResponse failed", transferResponse);
        Assert.assertTrue("EJB Transfer deposit failed", transferResponse.getKontostand() == 1099.00D);
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @Test
    public void test06SpringTransferRequest() throws Exception
    {
        IMoneyTransferFacade springTransferFacade = SpringContext.getBean("springMoneyTransferFacade");

        MoneyTransferRequest transferRequest = new MoneyTransferRequest("test", 99.0D);

        IMoneyTransferResponse transferResponse = springTransferFacade.transfer(transferRequest);

        Assert.assertNotNull("Spring TransferResponse failed", transferResponse);
        Assert.assertTrue("Spring Transfer deposit failed", transferResponse.getKontostand() == 1099.00D);
    }
}
