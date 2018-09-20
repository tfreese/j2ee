/**
 * Created: 23.05.2013
 */

package de.freese.j2ee.client;

import de.freese.j2ee.model.Kunde;
import de.freese.j2ee.rest.IKundenService;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * @author Thomas Freese
 */
public class EJBClient
{
	/**
	 * @param args String[]
	 * @throws Exception Falls was schief geht.
	 */
	public static void main(final String[] args) throws Exception
	{
		// Properties props = new Properties();
		// props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
		// props.setProperty(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
		// props.setProperty(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
		// props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
		// props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

		// glassfish/lib/appserv-rt.jar im ClassPath braucht keine Konfig für lokale Maschine.
		Context ctx = new InitialContext();

		// IKundenService service = (IKundenService) ctx.lookup("java:global/de.freese.j2ee/RestService");
		IKundenService service = (IKundenService) ctx.lookup(IKundenService.class.getName());
		List<Kunde> kunden = service.getData();

		for (Kunde kunde : kunden)
		{
			System.out.println(kunde);
		}
	}
}
