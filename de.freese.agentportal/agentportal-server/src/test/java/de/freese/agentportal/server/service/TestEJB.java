/**
 * Created: 25.05.2013
 */

package de.freese.agentportal.server.service;

import de.freese.agentportal.common.service.ISecretNewsService;
import java.util.Properties;
import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Thomas Freese
 */
public class TestEJB
{
	/**
	 * 
	 */
	private static EJBContainer container;

	/**
	 * @throws Exception Falls was schief geht.
	 */
	@BeforeClass
	public static void setupClass() throws Exception
	{
		Properties props = new Properties();
		// props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
		props.setProperty("jdbc/agentPortal", "new://Resource?type=DataSource");
		props.setProperty("jdbc/agentPortal.JdbcDriver", "org.hsqldb.jdbcDriver");
		props.setProperty("jdbc/agentPortal.JdbcUrl", "jdbc:hsqldb:mem:agentPortal");
		props.setProperty("jdbc/agentPortal.UserName", "SA");
		props.setProperty("jdbc/agentPortal.Password", "");

		// TODO container = EJBContainer.createEJBContainer(props);
	}

	/**
	 * 
	 */
	@AfterClass
	public static void shutdownClass()
	{
		if (container != null)
		{
			container.close();
		}
	}

	/**
	 * 
	 */
	@EJB
	private ISecretNewsService service;

	/**
	 * Erstellt ein neues {@link TestEJB} Object.
	 */
	public TestEJB()
	{
		super();
	}

	/**
	 * @throws Exception Falls was schief geht.
	 */
	@Before
	public void setupMethod() throws Exception
	{
		// TODO container.getContext().bind("inject", this);
	}

	/**
	 * 
	 */
	@Test
	public void testService()
	{
		// Was the EJB injected?
		// TODO Assert.assertTrue(this.service != null);
	}
}
