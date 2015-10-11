/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose Tools | Templates and open the template in
 * the editor.
 */
package de.freese.ejb.embedded.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Thomas Freese
 */
public class HelloWorldTest
{
    /**
     *
     */
    private static Context ctx = null;

    /**
     *
     */
    private static EJBContainer ejbContainer = null;

    /**
     *
     */
    @BeforeClass
    public static void setUpClass()
    {
        Map<String, Object> properties = new HashMap<>();
        properties.put(EJBContainer.MODULES, new File("target/classes"));
        ejbContainer = EJBContainer.createEJBContainer(properties);
        // ejbContainer = EJBContainer.createEJBContainer();
        ctx = ejbContainer.getContext();
    }

    /**
     *
     */
    @AfterClass
    public static void tearDownClass()
    {
        ejbContainer.close();
    }

    /**
     * Erstellt ein neues {@link HelloWorldTest} Object.
     */
    public HelloWorldTest()
    {
        super();
    }

    /**
     *
     */
    @Before
    public void setUp()
    {
    }

    /**
     *
     */
    @After
    public void tearDown()
    {
    }

    /**
     *
     */
    @Test
    public void testHelloWorld()
    {
        try
        {
            IHelloWorldLocal helloWorld = (IHelloWorldLocal) ctx.lookup("java:global/classes/HelloWorldEJB!de.freese.ejb.embedded.bean.IHelloWorldLocal");
            assertNotNull(helloWorld);

            String value = helloWorld.getHelloWorld();
            assertNotNull(value);
            assertEquals("Hello World", value);
        }
        catch (NamingException ex)
        {
            throw new AssertionError(ex);
        }
    }
}
