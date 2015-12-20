/**
 * Created: 25.05.2013
 */

package de.freese.agentportal.client.ejb;

import org.junit.Test;

/**
 * @author Thomas Freese
 */
public class TestClientEJB
{
	/**
	 * Erstellt ein neues {@link TestClientEJB} Object.
	 */
	public TestClientEJB()
	{
		super();
	}

	/**
	 * @throws Exception Falls was schief geht.
	 */
	@Test
	public void glassfishAccess() throws Exception
	{
		// TODO
		// glassfish/lib/appserv-rt.jar im ClassPath braucht keine Konfig für lokale Maschine.
		// Context ctx = new InitialContext();
		//
		// ISecretNewsService service =
		// (ISecretNewsService) ctx
		// .lookup("java:global/de.freese.agentportal.server/SecretNewsEJB!de.freese.agentportal.common.service.ISecretNewsService");
		// List<SecretNews> news = service.getAllSecretNews4High();
		//
		// for (SecretNews secretNews : news)
		// {
		// System.out.println(secretNews);
		// }
	}
}
