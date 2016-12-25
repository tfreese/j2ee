/**
 * Created: 19.05.2012
 */

package de.freese.agentportal.client.ws;

import org.junit.BeforeClass;
import org.junit.Test;

import de.freese.agentportal.generated.SecretNewsEntity;
import de.freese.agentportal.generated.SecretNewsEntityArray;
import de.freese.agentportal.generated.SecretNewsWebService;
import de.freese.agentportal.generated.SecretNewsWebServiceService;

/**
 * @author Thomas Freese
 */
public class WSUnsecureClient
{
	/**
	 * 
	 */
	private static SecretNewsWebService port = null;

	/**
	 * @throws Exception Falls was schief geht.
	 */
	@BeforeClass
	public static void getService() throws Exception
	{
		// System.setProperty("javax.net.ssl.trustStore", "client.keystore");
		// System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		// // System.setProperty("java.security.auth.login.config",
		// // "src/main/resources/META-INF/auth.conf");
		//
		// HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
		// {
		// /**
		// * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
		// * javax.net.ssl.SSLSession)
		// */
		// @Override
		// public boolean verify(final String hostname, final SSLSession sslSession)
		// {
		// return hostname.equals("localhost");
		// }
		// });
		//
		SecretNewsWebServiceService service = new SecretNewsWebServiceService();
		WSUnsecureClient.port = service.getSecretNewsWebServicePort();
		//
		// BindingProvider bindingProvider = (BindingProvider) port;
		// bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "AgentK");
		// bindingProvider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "ak123");
	}

	/**
	 * Erstellt ein neues {@link WSUnsecureClient} Object.
	 */
	public WSUnsecureClient()
	{
		super();
	}

	/**
	 * @throws Exception Falls was schief geht.
	 */
	@Test
	public void testHigh() throws Exception
	{
		System.out.println("testHigh()");

		SecretNewsWebService service = WSUnsecureClient.port;

		SecretNewsEntityArray news = service.getAllSecretNews4High();

		for (SecretNewsEntity newsEntity : news.getItem())
		{
			System.out.println(toString(newsEntity));
		}

		System.out.println();
	}

	/**
	 * @throws Exception Falls was schief geht.
	 */
	@Test
	public void testLow() throws Exception
	{
		System.out.println("testLow()");

		SecretNewsWebService service = WSUnsecureClient.port;

		SecretNewsEntityArray news = service.getAllSecretNews4Low();

		for (SecretNewsEntity newsEntity : news.getItem())
		{
			System.out.println(toString(newsEntity));
		}

		System.out.println();
	}

	/**
	 * @param entity {@link SecretNewsEntity}
	 * @return String
	 */
	public String toString(final SecretNewsEntity entity)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("SecretNewsEntity [id=");
		builder.append(entity.getId());
		builder.append(", securitylevel=");
		builder.append(entity.getSecuritylevel());
		builder.append(", title=");
		builder.append(entity.getTitle());
		builder.append(", timestamp=");
		builder.append(entity.getTimestamp());
		builder.append(", text=");
		builder.append(entity.getText());
		builder.append("]");

		return builder.toString();
	}
}
