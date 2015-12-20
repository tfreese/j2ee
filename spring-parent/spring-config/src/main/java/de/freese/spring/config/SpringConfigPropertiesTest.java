/**
 * Created: 18.12.2011
 */

package de.freese.spring.config;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Thomas Freese
 */
@Configuration
@Profile("test")
public class SpringConfigPropertiesTest
{
	/**
	 * @return {@link Properties}
	 */
	@Bean
	public static Properties propertiesDB()
	{
		String db = "derby/test";

		Properties bean = new Properties();
		bean.put("jdbc.dbName", db);
		bean.put("jdbc.url", "jdbc:derby:" + db + ";create=true");
		bean.put("jdbc.username", "tommy");
		bean.put("jdbc.password", "tommy");

		return bean;
	}

	/**
	 * @return {@link PropertyPlaceholderConfigurer}
	 * @throws Exception Falls was schief geht.
	 */
	@Bean
	public static PropertyPlaceholderConfigurer propertyConfigurer() throws Exception
	{
		PropertyPlaceholderConfigurer bean = new PropertyPlaceholderConfigurer();
		bean.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_FALLBACK);
		bean.setSearchSystemEnvironment(true);
		bean.setProperties(propertiesDB());

		return bean;
	}

	/**
	 * Erstellt ein neues {@link SpringConfigPropertiesTest} Object.
	 */
	public SpringConfigPropertiesTest()
	{
		super();
	}
}
