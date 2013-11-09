/**
 * Created: 02.06.2012
 */

package de.freese.agentportal.client;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Thomas Freese
 */
public class AllClientTests
{
	/**
	 * In der Methode werden alle Testklassen registriert die durch JUnit aufgerufen werden sollen.
	 * 
	 * @return {@link Test}
	 */
	public static Test suite()
	{
		TestSuite suite = new TestSuite("de.freese.agentportal.client");

		// suite.addTest(new JUnit4TestAdapter(EJBSecureClient.class));
		// suite.addTest(new JUnit4TestAdapter(EJBUnsecureClient.class));
		// suite.addTest(new JUnit4TestAdapter(RESTUnsecureClient.class));
		// suite.addTest(new JUnit4TestAdapter(WSUnsecureClient.class));

		return suite;
	}

	/**
	 * Erstellt ein neues {@link AllClientTests} Object.
	 */
	public AllClientTests()
	{
		super();
	}
}
