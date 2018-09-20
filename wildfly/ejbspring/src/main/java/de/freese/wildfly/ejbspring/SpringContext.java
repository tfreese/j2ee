/**
 * Created: 17.12.2015
 */
package de.freese.wildfly.ejbspring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

/**
 * @author Thomas Freese
 */
public class SpringContext implements InitializingBean, ApplicationContextAware, ResourceLoaderAware
{
    /**
     *
     */
    private static final SpringContext INSTANCE = new SpringContext();

    /**
     * @return {@link ApplicationContext}
     */
    public static ApplicationContext getApplicationContext()
    {
        return getInstance().applicationContext;
    }

    /**
     * Liefert die Bean mit der betreffenden BeanID.
     *
     * @param <T>    Konkreter Typ des empfangenen Objects
     * @param beanID String
     *
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(final String beanID)
    {
        return (T) getApplicationContext().getBean(beanID);
    }

    /**
     * Liefert die Bean mit der betreffenden BeanID und erwartetem Typ.
     *
     * @param <T>          Konkreter Typ des empfangenen Objects
     * @param beanID       String
     * @param requiredType {@link Class}
     *
     * @return Object
     */
    public static <T> T getBean(final String beanID, final Class<T> requiredType)
    {
        return getApplicationContext().getBean(beanID, requiredType);
    }

    /**
     * @return {@link SpringContext}
     */
    public static SpringContext getInstance()
    {
        return INSTANCE;
    }

    /**
     * @param location String
     *
     * @return {@link Resource}
     *
     * @see ResourceLoader#getResource(String)
     */
    public static Resource getResource(final String location)
    {
        return getResourceLoader().getResource(location);
    }

    /**
     * @return {@link ResourceLoader}
     */
    public static ResourceLoader getResourceLoader()
    {
        return getInstance().resourceLoader;
    }

    /**
     *
     */
    private ApplicationContext applicationContext = null;

    /**
     *
     */
    private ResourceLoader resourceLoader = null;

    /**
     * Erstellt ein neues {@link SpringContext} Object.
     */
    public SpringContext()
    {
        super();
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception
    {
        Assert.notNull(this.applicationContext,
                       "An ApplicationContext is required. Use setApplicationContext(org.springframework.context.ApplicationContext) to provide one.");

        Assert.notNull(this.resourceLoader, "An ResourceLoader is required. Use setResourceLoader(org.springframework.core.io.ResourceLoader) to provide one.");
    }

    /**
     * Setzt den SpringContext auf null.
     */
    public void clear()
    {
        this.applicationContext = null;
        this.resourceLoader = null;
    }

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
    {
        if (applicationContext == null)
        {
            throw new NullPointerException("ApplicationContext ist null !");
        }

        if (this.applicationContext != null)
        {
            throw new IllegalStateException("ApplicationContext ist bereits gesetzt !");
        }

        this.applicationContext = applicationContext;

        if (this.applicationContext instanceof AbstractApplicationContext)
        {
            ((AbstractApplicationContext) this.applicationContext).registerShutdownHook();
        }
    }

    /**
     * @see org.springframework.context.ResourceLoaderAware#setResourceLoader(org.springframework.core.io.ResourceLoader)
     */
    @Override
    public void setResourceLoader(final ResourceLoader resourceLoader)
    {
        if (resourceLoader == null)
        {
            throw new NullPointerException("ResourceLoader ist null !");
        }

        if (this.resourceLoader != null)
        {
            throw new IllegalStateException("ResourceLoader ist bereits gesetzt !");
        }

        this.resourceLoader = resourceLoader;
    }
}
