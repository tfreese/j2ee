/**
 * Created: 19.05.2012
 */

package de.freese.agentportal.client.rest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.model.SecretNewsList;

/**
 * @author Thomas Freese
 */
public class RESTUnsecureClient
{
	/**
	 *
	 */
	private static RestTemplate template = null;

	/**
	 * @throws Exception Falls was schief geht.
	 */
	@BeforeClass
	public static void setupClass() throws Exception
	{
		RESTUnsecureClient.template = new RestTemplate();
		RESTUnsecureClient.template.setRequestFactory(new SecureSimpleClientHttpRequestFactory("AgentK", "ak123"));

		// HttpComponentsClientHttpRequestFactory factory =
		// new HttpComponentsClientHttpRequestFactory();
		// DefaultHttpClient httpClient = (DefaultHttpClient) factory.getHttpClient();
		// httpClient.addRequestInterceptor(new PreemptiveAuthInterceptor("AgentK", "ak123"), 0);
		// RESTUnsecureClient.template.setRequestFactory(factory);

		// Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		// Map<String, Object> properties = new HashMap<>();
		// properties.put("jaxb.formatted.output", Boolean.TRUE);
		// jaxb2Marshaller.setMarshallerProperties(properties);
		// jaxb2Marshaller.setClassesToBeBound(SecretNewsEntity.class);
		//
		// MarshallingHttpMessageConverter jaxbConverter = new MarshallingHttpMessageConverter();
		// jaxbConverter.setMarshaller(jaxb2Marshaller);
		// jaxbConverter.setUnmarshaller(jaxb2Marshaller);
		//
		// List<HttpMessageConverter<?>> converters = new ArrayList<>();
		// converters.add(jaxbConverter);
		//
		// RESTUnsecureClient.template.setMessageConverters(converters);
	}

	/**
	 * @throws Exception Falls was schief geht.
	 */
	@AfterClass
	public static void shutdownClass() throws Exception
	{
		// Empty
	}

	/**
	 * Erstellt ein neues {@link RESTUnsecureClient} Object.
	 */
	public RESTUnsecureClient()
	{
		super();
	}

	/**
	 * @throws Exception Falls was schief geht.
	 */
	@Test
	public void testMultipleNews() throws Exception
	{
		// getLogger().info("testMultipleNews");

		try
		{
			SecretNewsList news = RESTUnsecureClient.template.getForObject("http://localhost:8080/secretnews/rest/news", SecretNewsList.class);

			Assert.assertTrue((news != null) && (news.getNews() != null) && !news.getNews().isEmpty());
		}
		catch (Exception ex)
		{
			Assert.assertFalse(true);
		}
	}

	/**
	 * @throws Exception Falls was schief geht.
	 */
	@Test
	public void testMultipleNewsJSON() throws Exception
	{
		// getLogger().info("testMultipleNewsJSON");

		HttpHeaders headers = new HttpHeaders();
		// headers.set( "User-Agent", "MyAgent");
		headers.set("Accept", "application/json");
		// headers.set("Accept", "application/xml");

		// Authorization: Basic BASE64Encoded[userName ':' password]
		// connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);

		try
		{
			ResponseEntity<SecretNewsList> news =
					RESTUnsecureClient.template.exchange("http://localhost:8080/secretnews/rest/news", HttpMethod.GET, new HttpEntity<String>(headers),
							SecretNewsList.class);

			Assert.assertTrue((news != null) && (news.getBody() != null) && (news.getBody().getNews() != null) && !news.getBody().getNews().isEmpty());
		}
		catch (Exception ex)
		{
			Assert.assertFalse(true);
		}
	}

	/**
	 * @throws Exception Falls was schief geht.
	 */
	@Test
	public void testOneNews() throws Exception
	{
		// getLogger().info("testOneNews");

		// RESTUnsecureClient.template.getForEntity("http://localhost:8080/secretnews/rest/news/3",
		// SecretNewsEntity.class);

		try
		{
			SecretNews news = RESTUnsecureClient.template.getForObject("http://localhost:8080/secretnews/rest/news/3", SecretNews.class);

			Assert.assertTrue(news != null);
		}
		catch (Exception ex)
		{
			Assert.assertFalse(true);
		}
	}
}
