/**
 * 20.12.2006
 */
package de.freese.jpa.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author Thomas Freese
 */
public final class HibernateUtil
{
	/** 
     * 
     */
	private static final SessionFactory sessionFactory;

	static
	{
		try
		{
			// URL url = ClassLoader.getSystemResource("hibernate.properties");
			// InputStream inputStream = url.openStream();
			// Properties properties = new Properties();
			// properties.load(inputStream);

			Configuration configuration = new Configuration();
			// configuration.setProperties(properties);
			configuration.configure();

			ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(configuration.getProperties());

			sessionFactory = configuration.buildSessionFactory(serviceRegistryBuilder.buildServiceRegistry());
		}
		catch (Throwable th)
		{
			System.err.println("SessionFactory creation failed: " + th);

			throw new ExceptionInInitializerError(th);
		}
	}

	/**
	 * !
	 * 
	 * @return {@link SessionFactory}
	 */
	public static SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}
}
