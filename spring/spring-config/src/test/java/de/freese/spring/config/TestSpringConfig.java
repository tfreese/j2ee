/**
 * Created: 11.12.2011
 */
package de.freese.spring.config;

import java.util.List;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import de.freese.spring.config.dao.ITestDAO;

/**
 * @author Thomas Freese
 */
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = ApplicationConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSpringConfig
{
    // /**
    // *
    // */
    // @Resource
    // private ITestDAO testDAO = null;

    /**
     * Erstellt ein neues {@link TestSpringConfig} Object.
     */
    public TestSpringConfig()
    {
        super();
    }

    /**
     *
     */
    @Test
    public void test01EnvTest()
    {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext())
        {
            ctx.getEnvironment().setActiveProfiles("test");

            ctx.register(ApplicationConfig.class);
            ctx.refresh();
            ctx.registerShutdownHook();

            ITestDAO testDAO = ctx.getBean(ITestDAO.class);
            List<String> data = testDAO.loadData();

            Assert.assertNotNull(data);
            Assert.assertEquals(1, data.size());
            Assert.assertEquals("TEST", data.get(0));
        }
    }

    /**
     *
     */
    @Test
    public void test02EnvProd()
    {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext())
        {
            ctx.getEnvironment().setActiveProfiles("prod");

            ctx.register(ApplicationConfig.class);
            ctx.refresh();
            ctx.registerShutdownHook();

            ITestDAO testDAO = ctx.getBean(ITestDAO.class);
            List<String> data = testDAO.loadData();

            Assert.assertNotNull(data);
            Assert.assertEquals(1, data.size());
            Assert.assertEquals("PROD", data.get(0));
        }
    }
}
