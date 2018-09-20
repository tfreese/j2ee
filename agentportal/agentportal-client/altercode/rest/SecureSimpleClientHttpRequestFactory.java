/**
 * Created: 16.12.2012
 */

package de.freese.agentportal.client.rest;

import java.io.IOException;
import java.net.HttpURLConnection;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * @author Thomas Freese
 */
public class SecureSimpleClientHttpRequestFactory extends SimpleClientHttpRequestFactory
{
	/**
	 *
	 */
	private final String password;

	/**
	 *
	 */
	private final String user;

	/**
	 * Erstellt ein neues {@link SecureSimpleClientHttpRequestFactory} Object.
	 * 
	 * @param user String
	 * @param password char[]
	 */
	public SecureSimpleClientHttpRequestFactory(final String user, final String password)
	{
		super();

		this.user = user;
		this.password = password;
	}

	/**
 */
	@Override
	protected void prepareConnection(final HttpURLConnection connection, final String httpMethod) throws IOException
	{
		super.prepareConnection(connection, httpMethod);

		String token = this.user + ":" + this.password;

		// sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
		// String encodedAuthorization = enc.encode(token.getBytes());
		// QWdlbnRLOmFrMTIz

		String encodedAuthorization = new String(Base64.encodeBase64(token.getBytes()));

		connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
	}
}
