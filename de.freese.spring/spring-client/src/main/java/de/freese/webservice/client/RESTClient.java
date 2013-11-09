/**
 * Created: 04.04.2013
 */

package de.freese.webservice.client;

import de.freese.spring.common.model.MyEmployee;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author Thomas Freese
 */
public class RESTClient
{
	/**
	 * @param args String[]
	 */
	public static void main(final String[] args)
	{
		RestTemplate restTemplate = new RestTemplate();

		// Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		// Map<String, Object> properties = new HashMap<>();
		// properties.put("jaxb.formatted.output", Boolean.TRUE);
		// jaxb2Marshaller.setMarshallerProperties(properties);
		// jaxb2Marshaller.setClassesToBeBound(MyEmployee.class);
		//
		// MarshallingHttpMessageConverter jaxbConverter = new MarshallingHttpMessageConverter();
		// jaxbConverter.setMarshaller(jaxb2Marshaller);
		// jaxbConverter.setUnmarshaller(jaxb2Marshaller);

		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(new MappingJacksonHttpMessageConverter());

		restTemplate.setMessageConverters(converters);

		// List<MediaType> acceptableMediaTypes = new ArrayList<>();
		// acceptableMediaTypes.add(MediaType.APPLICATION_XML);

		HttpHeaders headers = new HttpHeaders();
		// headers.setAccept(acceptableMediaTypes);
		headers.set("Accept", "application/json");
		// headers.set("Accept", "application/xml");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<MyEmployee> result =
				restTemplate.exchange("http://localhost:8080/spring/rest/movie/emp/{name}", HttpMethod.GET, entity, MyEmployee.class, "test");
		System.out.println(result.toString());
		MyEmployee employee = result.getBody();

		System.out.println(employee);
	}
}
