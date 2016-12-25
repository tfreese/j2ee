/**
 * Created: 25.05.2013
 */

package de.freese.agentportal.client.rest;

import de.freese.agentportal.common.model.SecretNews;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * @author Thomas Freese
 */
public class TestClientREST
{
	/**
	 * @param oid long
	 * @throws Exception Falls was schief geht.
	 */
	private static void delete(final long oid) throws Exception
	{
		System.err.println("TestClientREST.delete()");

		URL url = new URL("http://localhost:8080/de.freese.agentportal.server/rest/news/" + oid);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("DELETE");

		// if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		// {
		// throw new RuntimeException("Operation failed: " + connection.getResponseCode());
		// }

		System.out.println("Content-Type = " + connection.getContentType());
		System.out.println("Location: " + connection.getHeaderField("Location"));

		connection.disconnect();
	}

	/**
	 * @param title String
	 * @param text String
	 * @throws Exception Falls was schief geht.
	 */
	private static void insert(final String title, final String text) throws Exception
	{
		System.err.println("TestClientREST.insert()");

		URL url = new URL("http://localhost:8080/de.freese.agentportal.server/rest/news?title=" + title + "&text=" + text);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Content-Type", "text/plain");
		// connection.setRequestProperty("Accept", "application/json");
		// connection.setRequestProperty("Accept", "text/xml");
		// connection.setRequestProperty("Accept", "text/plain");

		if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		{
			throw new RuntimeException("Operation failed: " + connection.getResponseCode());
		}

		System.out.println("Content-Type = " + connection.getContentType());
		System.out.println("Location: " + connection.getHeaderField("Location"));

		connection.disconnect();
	}

	/**
	 * @param args String[]
	 * @throws Exception Falls was schief geht.
	 */
	public static void main(final String[] args) throws Exception
	{
		// selectOne(1);
		// update(1, "Thomas Freese", "Update Test");
		selectAll();
		insert("Thomas Freese", "Insert Test");
		// delete(1);
		selectAll();
	}

	/**
	 * @throws Exception Falls was schief geht.
	 */
	private static void selectAll() throws Exception
	{
		System.err.println("TestClientREST.selectAll()");

		URL url = new URL("http://localhost:8080/de.freese.agentportal.server/rest/news");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		// connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Accept", "text/xml");

		if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		{
			throw new RuntimeException("Operation failed: " + connection.getResponseCode());
		}

		System.out.println("Content-Type = " + connection.getContentType());
		System.out.println("Location: " + connection.getHeaderField("Location"));

		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = reader.readLine();

		while (line != null)
		{
			System.out.println(line);
			line = reader.readLine();
		}

		connection.disconnect();
	}

	/**
	 * @param oid long
	 * @throws Exception Falls was schief geht.
	 */
	private static void selectOne(final long oid) throws Exception
	{
		System.err.println("TestClientREST.selectOne()");

		URL url = new URL("http://localhost:8080/de.freese.agentportal.server/rest/news/" + oid);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		// connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Accept", "text/xml");

		if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		{
			throw new RuntimeException("Operation failed: " + connection.getResponseCode());
		}

		System.out.println("Content-Type = " + connection.getContentType());
		System.out.println("Location: " + connection.getHeaderField("Location"));

		JAXBContext context = JAXBContext.newInstance(SecretNews.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		SecretNews news = (SecretNews) unmarshaller.unmarshal(connection.getInputStream());
		System.out.println(news);

		// BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		//
		// String line = reader.readLine();
		//
		// while(line != null)
		// {
		// System.out.println(line);
		// line=reader.readLine();
		// }

		connection.disconnect();
	}

	/**
	 * @param id long
	 * @param title String
	 * @param text String
	 * @throws Exception Falls was schief geht.
	 */
	private static void update(final long id, final String title, final String text) throws Exception
	{
		System.err.println("TestClientREST.update()");

		URL url = new URL("http://localhost:8080/de.freese.agentportal.server/rest/news");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "text/xml");

		OutputStream out = connection.getOutputStream();
		String xml = "<secretNews id=\"" + id + "\"><securitylevel>1</securitylevel><title>" + title + "</title><text>" + text + "</text></secretNews>";
		out.write(xml.getBytes());
		out.flush();

		if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		{
			throw new RuntimeException("Failed to update");
		}

		System.out.println("Content-Type = " + connection.getContentType());
		System.out.println("Location: " + connection.getHeaderField("Location"));

		connection.disconnect();
	}

	/**
	 * Erstellt ein neues {@link TestClientREST} Object.
	 */
	public TestClientREST()
	{
		super();
	}
}
