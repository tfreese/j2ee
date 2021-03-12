/**
 * Created: 21.05.2013
 */
package de.freese.j2ee.jmx;

import java.lang.management.ManagementFactory;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * @author Thomas Freese
 */
@Startup
@Singleton
public class JmxBeanExtension
{
    // /**
    // *
    // */
    // @Inject
    // private LogInterceptor interceptor = null;

    /**
     *
     */
    @Inject
    @JMXBean
    // @Any
    private Instance<Object> jmxBeans;

    /**
     *
     */
    private MBeanServer mBeanServer;

    /**
     * @throws Exception Falls was schief geht.
     */
    @PostConstruct
    public void exportsBean() throws Exception
    {
        System.err.println("JmxBeanExtension.exportsBean()");

        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();

        if (this.jmxBeans == null)
        {
            return;
        }

        for (Object bean : this.jmxBeans)
        {
            Class<?> beanClass = bean.getClass();
            // Object bean = this.interceptor;
            // Class<?> beanClass = LogInterceptor.class;
            // String annotationValue = beanClass.getAnnotation(JMXBean.class).objectName();
            String annotationValue = beanClass.getSimpleName();
            ObjectName objectName = null;

            if ("".equals(annotationValue))
            {
                objectName = new ObjectName(beanClass.getName());
            }
            else
            {
                objectName = new ObjectName(annotationValue + ":type=" + beanClass.getName());
            }

            this.mBeanServer.registerMBean(bean, objectName);
            System.out.println("Registered " + objectName);
        }
    }
}
