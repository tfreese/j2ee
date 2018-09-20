/**
 * Created: 25.05.2013
 */

package de.freese.agentportal.client.rest;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.model.SecretNewsList;
import java.util.Date;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author Thomas Freese
 */
public class TestClientRestTemplate
{
	/**
	 * @param template {@link RestTemplate}
	 * @param oid long
	 * @throws Exception Falls was schief geht.
	 */
	private static void delete(final RestTemplate template, final long oid) throws Exception
	{
		System.err.println("TestClientREST.delete()");

		// URL url = new URL("http://localhost:8080/de.freese.agentportal.server/rest/news/" + oid);
		// HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// connection.setRequestMethod("DELETE");
		//
		// // if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		// // {
		// // throw new RuntimeException("Operation failed: " + connection.getResponseCode());
		// // }
		//
		// System.out.println("Content-Type = " + connection.getContentType());
		// System.out.println("Location: " + connection.getHeaderField("Location"));
		//
		// connection.disconnect();
	}

	/**
	 * @param template {@link RestTemplate}
	 * @param title String
	 * @param text String
	 * @throws Exception Falls was schief geht.
	 */
	private static void insert(final RestTemplate template, final String title, final String text) throws Exception
	{
		System.err.println("TestClientREST.insert()");

		// template.postForObject("http://localhost:8080/de.freese.agentportal.server/rest/news/?title={title}&text={text}", Void.class, title,text);
		//
		// URL url = new URL("http://localhost:8080/de.freese.agentportal.server/rest/news?title=" + title + "&text=" + text);
		// HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// connection.setRequestMethod("PUT");
		// connection.setRequestProperty("Content-Type", "text/plain");
		// // connection.setRequestProperty("Accept", "application/json");
		// // connection.setRequestProperty("Accept", "text/xml");
		// // connection.setRequestProperty("Accept", "text/plain");
		//
		// if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		// {
		// throw new RuntimeException("Operation failed: " + connection.getResponseCode());
		// }
		//
		// System.out.println("Content-Type = " + connection.getContentType());
		// System.out.println("Location: " + connection.getHeaderField("Location"));
		//
		// connection.disconnect();
	}

	/**
	 * @param args String[]
	 * @throws Exception Falls was schief geht.
	 */
	public static void main(final String[] args) throws Exception
	{
		RestTemplate template = new RestTemplate();

		selectOne(template, 1);
		update(template, 1, "Thomas Freese", "Update Test");
		selectAll(template);
		insert(template, "Thomas Freese", "Insert Test");
		delete(template, 1);
		selectAll(template);
	}

	/**
	 * @param template {@link RestTemplate}
	 * @throws Exception Falls was schief geht.
	 */
	private static void selectAll(final RestTemplate template) throws Exception
	{
		System.err.println("TestClientREST.selectAll()");

		// String response = template.getForObject("http://localhost:8080/de.freese.agentportal.server/rest/news", String.class);
		// System.out.println(response);

		HttpHeaders headers = new HttpHeaders();
		// headers.set("Accept", "application/json");
		headers.set("Accept", "application/xml");

		ResponseEntity<SecretNewsList> response =
				template.exchange("http://localhost:8080/de.freese.agentportal.server/rest/news", HttpMethod.GET, new HttpEntity<String>(headers),
						SecretNewsList.class);
		System.out.println(response);

		for (SecretNews news : response.getBody().getNews())
		{
			System.out.println(news);
		}
	}

	/**
	 * @param template {@link RestTemplate}
	 * @param oid long
	 * @throws Exception Falls was schief geht.
	 */
	private static void selectOne(final RestTemplate template, final long oid) throws Exception
	{
		System.err.println("TestClientRestTemplate.selectOne()");

		SecretNews news = template.getForObject("http://localhost:8080/de.freese.agentportal.server/rest/news/{id}", SecretNews.class, Long.valueOf(oid));
		System.out.println(news);
	}

	/**
	 * @param template {@link RestTemplate}
	 * @param id long
	 * @param title String
	 * @param text String
	 * @throws Exception Falls was schief geht.
	 */
	private static void update(final RestTemplate template, final long id, final String title, final String text) throws Exception
	{
		System.err.println("TestClientREST.update()");

		SecretNews news = new SecretNews();
		news.setId(Long.valueOf(id));
		news.setSecuritylevel(SecretNews.SECURITY_LEVEL_LOW);
		news.setTitle(title);
		news.setText(text);
		news.setTimestamp(new Date());

		// JAXBContext context = JAXBContext.newInstance(SecretNews.class);
		// Marshaller marshaller = context.createMarshaller();
		//
		// StringWriter writer = new StringWriter();
		// marshaller.marshal(news, new StreamResult(writer));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		// headers.setContentType(MediaType.APPLICATION_XML);

		HttpEntity<SecretNews> entity = new HttpEntity<>(news, headers);

		ResponseEntity<String> response =
				template.exchange("http://localhost:8080/de.freese.agentportal.server/rest/news", HttpMethod.POST, entity, String.class);
		System.out.println(response);
	}

	/**
	 * Erstellt ein neues {@link TestClientRestTemplate} Object.
	 */
	public TestClientRestTemplate()
	{
		super();
	}
}
